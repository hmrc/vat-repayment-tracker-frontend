/*
 * Copyright 2020 HM Revenue & Customs
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

package service

import java.time.LocalDate

import connectors.VatRepaymentTrackerBackendConnector
import formaters.{DesFormatter, PeriodFormatter}
import javax.inject.{Inject, Singleton}
import model.des._
import model._
import play.api.Logger
import play.api.mvc.Request
import views.Views

import scala.concurrent.ExecutionContext

@Singleton
class VrtService @Inject() (
    views:                               Views,
    vatRepaymentTrackerBackendConnector: VatRepaymentTrackerBackendConnector,
    periodFormatter:                     PeriodFormatter,
    desFormatter:                        DesFormatter
)
  (implicit ec: ExecutionContext) {

  def getAllRepaymentData(repaymentDetails: Option[Seq[RepaymentDetailData]], vrn: Vrn, financialData: Option[FinancialData])(implicit request: Request[_]): AllRepaymentData = {

    repaymentDetails match {
      case Some(rd) =>
        for {
          r <- rd
          vrtRepaymentDetailData = VrtRepaymentDetailData(None, LocalDate.now(), vrn, r)
          _ = vatRepaymentTrackerBackendConnector.store(vrtRepaymentDetailData)
        } yield Logger.debug(s"cached vat repayment data for vrn : $vrn")

        val data = getRepaymentData(rd, vrn, financialData)
        // use distinct as we don't want duplicate rows for the same period with different risking status.  Risking status is relevant for view progress but not the tabbed screens.
        val currentData: List[RepaymentDataNoRiskingStatus] = data.filter(f => f.riskingStatus == INITIAL.value || f.riskingStatus == SENT_FOR_RISKING.value || f.riskingStatus == CLAIM_QUERIED.value).map(m =>
          RepaymentDataNoRiskingStatus(m.period, m.amount, m.returnCreationDate, m.periodKey)).toList.distinct
        val completed: List[RepaymentDataNoRiskingStatus] = data.filter(f => f.riskingStatus == REPAYMENT_ADJUSTED.value || f.riskingStatus == ADJUSMENT_TO_TAX_DUE.value || f.riskingStatus == REPAYMENT_APPROVED.value).map(m =>
          RepaymentDataNoRiskingStatus(m.period, m.amount, m.returnCreationDate, m.periodKey)).toList.distinct

        //if something is completed, remove from the current list
        AllRepaymentData(
          currentData.filterNot(completed.contains(_)),
          completed
        )
      case None => dealWithNodata
    }

  }

  private def getRepaymentData(repaymentDetails: Seq[RepaymentDetailData], vrn: Vrn, financialData: Option[FinancialData])(implicit request: Request[_]): Seq[RepaymentData] = {

    for {
      rd <- repaymentDetails
      if !outDatedPredicate(rd, financialData)
    } yield RepaymentData(periodFormatter.formatPeriodKey(rd.periodKey), if (rd.riskingStatus == CLAIM_QUERIED.value) rd.originalPostingAmount else rd.vatToPay_BOX5_InPounds, rd.returnCreationDate, rd.riskingStatus, rd.periodKey)

  }

  private def outDatedPredicate(repaymentDetailData: RepaymentDetailData, financialData: Option[FinancialData]): Boolean = {
    repaymentDetailData.lastUpdateReceivedDate.getOrElse(LocalDate.now()).isBefore(LocalDate.now().minusDays(60)) && (
      desFormatter.getReturnDebitChargeExists(financialData, PeriodKey(repaymentDetailData.periodKey)) || desFormatter.getReturnCreditChargeExists(financialData, PeriodKey(repaymentDetailData.periodKey))
    )
  }

  private def dealWithNodata: AllRepaymentData = {
    AllRepaymentData(List[RepaymentDataNoRiskingStatus](), List[RepaymentDataNoRiskingStatus]())
  }

}
