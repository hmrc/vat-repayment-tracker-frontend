/*
 * Copyright 2019 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package formaters

import java.time.LocalDate

import javax.inject.{Inject, Singleton}
import langswitch.LangMessages
import model._
import model.des._
import play.api.mvc.{Request, Result, Results}
import views.Views
import req.RequestSupport

import scala.concurrent.ExecutionContext

@Singleton
class ViewProgressFormatter @Inject() (views:          Views,
                                       requestSupport: RequestSupport,
                                       desFormatter:   DesFormatter)(implicit ec: ExecutionContext) extends Results {

  import requestSupport._

  def computeViewProgress(vrn: Vrn, vrd: List[VrtRepaymentDetailData])(implicit request: Request[_]): Result = {
    val estRepaymentDate = getEstimatedRepaymentDate(vrd(0).repaymentDetailsData.returnCreationDate, vrd(0).repaymentDetailsData.supplementDelayDays)
    val viewProgress: ViewProgress = ViewProgress(
      computeViewProgressTitle(estRepaymentDate, vrd),
      vrd(0).repaymentDetailsData.vatToPay_BOX5,
      vrd(0).repaymentDetailsData.returnCreationDate,
      estRepaymentDate,
      desFormatter.formatPeriodKey(vrd(0).repaymentDetailsData.periodKey),
      computeWhatsHappenedSoFarList(estRepaymentDate, vrd))
    Ok(views.view_progress(vrn, viewProgress))
  }

  private def computeViewProgressTitle(estRepaymentDate: LocalDate, vrd: List[VrtRepaymentDetailData])(implicit request: Request[_]): String = {
    if (vrd.filter(f => f.repaymentDetailsData.riskingStatus == REPAYMENT_APPROVED.value).size == 1)
      LangMessages.`Your repayment is complete`.show
    else if (vrd.filter(f => f.repaymentDetailsData.riskingStatus == ADJUSTMENT_TO_TAX_DUE.value).size == 1)
      LangMessages.`You must now pay some VAT`.show
    else if (vrd.filter(f => f.repaymentDetailsData.riskingStatus == REPAYMENT_ADJUSTED.value).size == 1)
      LangMessages.`Your repayment has been approved`.show
    else if (estRepaymentDate isBefore LocalDate.now())
      LangMessages.`Your repayment is delayed`.show
    else
      LangMessages.`Your repayment is being processed`.show
  }

  private def computeWhatsHappenedSoFarList(estRepaymentDate: LocalDate, vrd: List[VrtRepaymentDetailData])(implicit request: Request[_]): List[WhatsHappendSoFar] = {
    implicit val localDateOrdering: Ordering[LocalDate] = Ordering.fromLessThan(_ isAfter _)

    vrd.sortBy(s => (s.repaymentDetailsData.sorted, s.repaymentDetailsData.lastUpdateReceivedDate)).map (m => computeWhatsHappenedSoFar(estRepaymentDate, m))
  }

  private def computeWhatsHappenedSoFar(estRepaymentDate: LocalDate, vrtRepaymentDetailData: VrtRepaymentDetailData)(implicit request: Request[_]): WhatsHappendSoFar = {
    vrtRepaymentDetailData.repaymentDetailsData.riskingStatus match {
      //id:1  (S001)
      case INITIAL.value          => WhatsHappendSoFar(vrtRepaymentDetailData.repaymentDetailsData.returnCreationDate, LangMessages.`Checking VAT repayment`.show, LangMessages.`We have received your VAT return and are now checking the repayment amount`.show)
      //id???
      case SENT_FOR_RISKING.value => WhatsHappendSoFar(vrtRepaymentDetailData.repaymentDetailsData.returnCreationDate, LangMessages.`Sending for further checks (risking)`.show, LangMessages.`We are making sure we pay you the right amount`.show)
      case CLAIM_QUERIED.value => if (estRepaymentDate isBefore LocalDate.now())
        //id:2
        WhatsHappendSoFar(vrtRepaymentDetailData.repaymentDetailsData.returnCreationDate, LangMessages.`Sending for further checks`.show, LangMessages.`We are making sure we pay you the right amount`.show)
      else
        //id:4
        WhatsHappendSoFar(vrtRepaymentDetailData.repaymentDetailsData.returnCreationDate, LangMessages.`Estimated repayment date has passed`.show, LangMessages.`You do not need to do anything right now`.show)
      //id:3
      case REPAYMENT_ADJUSTED.value => WhatsHappendSoFar(vrtRepaymentDetailData.repaymentDetailsData.lastUpdateReceivedDate.getOrElse(vrtRepaymentDetailData.repaymentDetailsData.returnCreationDate),
                                                         LangMessages.`Your VAT repayment amount changed`.show, LangMessages.`You claimed a VAT repayment of`(desFormatter.formatAmount(vrtRepaymentDetailData.repaymentDetailsData.originalPostingAmount), desFormatter.formatAmount(vrtRepaymentDetailData.repaymentDetailsData.vatToPay_BOX5)).show)
      //id:5
      case ADJUSTMENT_TO_TAX_DUE.value => WhatsHappendSoFar(vrtRepaymentDetailData.repaymentDetailsData.lastUpdateReceivedDate.getOrElse(vrtRepaymentDetailData.repaymentDetailsData.returnCreationDate),
                                                            LangMessages.`VAT payment due`.show, LangMessages.`We calculated that the original amount you claimed of`(desFormatter.formatAmount(vrtRepaymentDetailData.repaymentDetailsData.originalPostingAmount), desFormatter.formatAmount(vrtRepaymentDetailData.repaymentDetailsData.vatToPay_BOX5)).show)
      //id:6
      case REPAYMENT_APPROVED.value => WhatsHappendSoFar(vrtRepaymentDetailData.repaymentDetailsData.returnCreationDate, LangMessages.`Repayment approved`.show, LangMessages.`We will send this to your repayment bank account. Repayments are usually sent within 30 days of HMRC getting your VAT Return`.show)
    }
  }

  private def getEstimatedRepaymentDate(returnCreationDate: LocalDate, supplementDelayDays: Option[Int]): LocalDate =
    {
      returnCreationDate.plusDays(supplementDelayDays.getOrElse(0) + 30)
    }

}
