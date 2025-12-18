/*
 * Copyright 2024 HM Revenue & Customs
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

import connectors.VatRepaymentTrackerBackendConnector
import play.api.Logger
import play.api.mvc.Request
import formaters.{DesFormatter, PeriodFormatter}
import model._
import model.des.RiskingStatus
import model.des._
import play.api.i18n.Messages

import java.time.LocalDate
import javax.inject.{Inject, Singleton}

@Singleton
class VrtService @Inject() (
  vatRepaymentTrackerBackendConnector: VatRepaymentTrackerBackendConnector,
  periodFormatter:                     PeriodFormatter,
  desFormatter:                        DesFormatter
) {

  private val logger = Logger(this.getClass)

  def getAllRepaymentData(
    repaymentDetails: Option[Seq[RepaymentDetailData]],
    vrn:              Vrn,
    financialData:    Option[FinancialData],
    maybePeriodKey:   Option[PeriodKey] = None
  )(implicit request: Request[_], messages: Messages): AllRepaymentData =
    repaymentDetails match {
      case Some(rd) =>
        val vrd = maybePeriodKey
          .fold(rd) { periodKey =>
            rd.filter(_.periodKey == periodKey.value)
          }
          .map(VrtRepaymentDetailData(None, LocalDate.now(), vrn, _))

        for {
          v <- vrd
          _  = vatRepaymentTrackerBackendConnector.store(v)
        } yield logger.debug(s"cached vat repayment data for vrn : $vrn")

        val data: Seq[RepaymentData] = getRepaymentData(rd, financialData)

        def completeExistsWithoutClearingDate(periodKey: String) =
          data.exists(r => r.periodKey == periodKey && r.riskingStatus.complete && r.clearingDate.isEmpty)

        // use distinct as we don't want duplicate rows for the same period with different risking status.
        // Risking status is relevant for view progress but not the tabbed screens.
        val currentData: List[RepaymentDataNoRiskingStatus] = data
          .filter(r =>
            (r.riskingStatus.inProgress && !completeExistsWithoutClearingDate(
              r.periodKey
            )) || (r.riskingStatus.complete && r.clearingDate.isEmpty)
          )
          .map(m => RepaymentDataNoRiskingStatus(m.period, m.amount, m.returnCreationDate, m.periodKey, m.clearingDate))
          .toList
          .distinct

        val completed: List[RepaymentDataNoRiskingStatus] = data
          .filter(r => r.riskingStatus.complete && r.clearingDate.isDefined)
          .map(m => RepaymentDataNoRiskingStatus(m.period, m.amount, m.returnCreationDate, m.periodKey, m.clearingDate))
          .toList
          .distinct

        val latestUpdateForEachPayment = data
          .groupBy(_.periodKey)
          .map { case (_, lst) =>
            lst
              .sortBy(s => (s.sorted, s.lastUpdateReceivedDate))
              .head
          }

        // Used only to determine when to display repayment suspended warning on show-vrt
        val hasSuspendedPayment =
          latestUpdateForEachPayment.exists(_.riskingStatus == RiskingStatus.REPAYMENT_SUSPENDED)

        // if something is completed, remove from the current list
        AllRepaymentData(
          hasSuspendedPayment,
          currentData.filterNot(current =>
            completed.exists(comp =>
              current.periodKey == comp.periodKey && current.returnCreationDate == comp.returnCreationDate
            )
          ),
          completed
        )
      case None     => dealWithNoData
    }

  private def getRepaymentData(
    repaymentDetails: Seq[RepaymentDetailData],
    financialData:    Option[FinancialData]
  )(implicit messages: Messages): Seq[RepaymentData] =
    for {
      rd <- repaymentDetails
      if !outDatedPredicate(rd, financialData)
    } yield {
      val transactionForPeriodKey = desFormatter.getTransactionWithPeriodKey(financialData, PeriodKey(rd.periodKey))
      val clearingDate            = desFormatter.getClearingDate(transactionForPeriodKey)

      RepaymentData(
        periodFormatter.formatPeriodKey(rd.periodKey),
        if (
          rd.riskingStatus == RiskingStatus.CLAIM_QUERIED
          || rd.riskingStatus == RiskingStatus.REPAYMENT_APPROVED
          || rd.riskingStatus == RiskingStatus.SENT_FOR_RISKING
          || rd.riskingStatus == RiskingStatus.INITIAL
        ) rd.originalPostingAmount
        else rd.vatToPay_BOX5,
        rd.returnCreationDate,
        rd.lastUpdateReceivedDate,
        rd.riskingStatus,
        rd.periodKey,
        clearingDate
      )
    }

  private def outDatedPredicate(
    repaymentDetailData: RepaymentDetailData,
    financialData:       Option[FinancialData]
  ): Boolean = {
    val lastUpdateReceivedDate = repaymentDetailData.lastUpdateReceivedDate.getOrElse(LocalDate.now())

    // if in-progress must be within 9 months and if completed must be within 60 days
    lastUpdateReceivedDate.isBefore(LocalDate.now().minusMonths(9)) || (
      lastUpdateReceivedDate.isBefore(LocalDate.now().minusDays(60)) && (
        desFormatter.getReturnDebitChargeExists(
          financialData,
          PeriodKey(repaymentDetailData.periodKey)
        ) || desFormatter.getReturnCreditChargeExists(financialData, PeriodKey(repaymentDetailData.periodKey))
      )
    )
  }

  private def dealWithNoData: AllRepaymentData =
    AllRepaymentData(
      hasSuspendedPayment = false,
      List[RepaymentDataNoRiskingStatus](),
      List[RepaymentDataNoRiskingStatus]()
    )

}
