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
import model.{Period, ViewProgress, Vrn, VrtRepaymentDetailData}
import model.des.{ADJUSTMENT_TO_TAX_DUE, REPAYMENT_APPROVED}
import play.api.mvc.{Request, Result, Results}
import views.Views
import req.RequestSupport

import scala.concurrent.ExecutionContext

@Singleton
class ViewProgressFormatter @Inject() (views:          Views,
                                       requestSupport: RequestSupport)(implicit ec: ExecutionContext) extends Results {

  import requestSupport._

  def computeViewProgress(vrn: Vrn, vrd: List[VrtRepaymentDetailData], period: Period)(implicit request: Request[_]): Result = {
    val estRepaymentDate = getEstimatedRepaymentDate(vrd(0).repaymentDetailsData.returnCreationDate, vrd(0).repaymentDetailsData.supplementDelayDays)
    val viewProgress: ViewProgress = ViewProgress(
      computeViewProgressTitle(estRepaymentDate, vrd),
      vrd(0).repaymentDetailsData.vatToPay_BOX5,
      vrd(0).repaymentDetailsData.returnCreationDate,
      estRepaymentDate,
      period.value,
      List())
    Ok(views.view_progress(vrn, viewProgress))
  }

  def computeViewProgressTitle(estRepaymentDate: LocalDate, vrd: List[VrtRepaymentDetailData])(implicit request: Request[_]): String = {
    if (vrd.filter(f => f.repaymentDetailsData.riskingStatus == REPAYMENT_APPROVED.value).size == 1)
      LangMessages.`Your repayment is complete`.show
    else if (vrd.filter(f => f.repaymentDetailsData.riskingStatus == ADJUSTMENT_TO_TAX_DUE.value).size == 1)
      LangMessages.`You must now pay some VAT`.show
    else if (estRepaymentDate isBefore LocalDate.now())
      LangMessages.`Your repayment is delayed`.show
    else
      LangMessages.`Your repayment is being processed`.show
  }

  private def getEstimatedRepaymentDate(returnCreationDate: LocalDate, supplementDelayDays: Int): LocalDate =
    {
      returnCreationDate.plusDays(supplementDelayDays + 30)
    }

}
