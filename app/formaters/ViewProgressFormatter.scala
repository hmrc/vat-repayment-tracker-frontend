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

package formaters

import cats.data.NonEmptyList
import cats.kernel.Order
import config.ViewConfig
import model._
import model.des.RiskingStatus.{ADJUSMENT_TO_TAX_DUE, CLAIM_QUERIED, INITIAL, REPAYMENT_ADJUSTED, REPAYMENT_APPROVED, REPAYMENT_SUSPENDED, SENT_FOR_RISKING}
import model.des._
import play.api.Logger
import play.api.i18n.Messages
import play.api.mvc.{Request, Result, Results}

import java.time.LocalDate
import javax.inject.{Inject, Singleton}

@Singleton
class ViewProgressFormatter @Inject() (
    view_progress:   views.html.view_progress,
    desFormatter:    DesFormatter,
    viewConfig:      ViewConfig,
    periodFormatter: PeriodFormatter) extends Results {

  implicit val localDateOrdering: Order[LocalDate] = Order.fromOrdering(Ordering.fromLessThan(_ isAfter _))

  private val logger = Logger(this.getClass)

  def computeViewProgress(
      periodKey:     PeriodKey,
      vrd:           NonEmptyList[VrtRepaymentDetailData],
      customerData:  Option[CustomerInformation],
      financialData: Option[FinancialData]
  )(
      implicit
      request: Request[_], messages: Messages
  ): Result = {

    val bankDetailsExist = desFormatter.getBankDetailsExist(customerData)
    val bankDetails = desFormatter.getBankDetails(customerData)
    val addressDetails = desFormatter.getAddressDetails(customerData)
    val returnCreditChargeExists = desFormatter.getReturnCreditChargeExists(financialData, periodKey)
    val returnDebitChargeExists = desFormatter.getReturnDebitChargeExists(financialData, periodKey)
    val transactionForPeriodKey = desFormatter.getTransactionWithPeriodKey(financialData, periodKey)
    val latestUpdate =
      vrd.sortBy(repaymentData => (repaymentData.repaymentDetailsData.sorted, repaymentData.repaymentDetailsData.lastUpdateReceivedDate)).head
    val latestRiskingStatus = latestUpdate.repaymentDetailsData.riskingStatus

    val estRepaymentDate = getEstimatedRepaymentDate(latestUpdate.repaymentDetailsData.returnCreationDate, latestUpdate.repaymentDetailsData.supplementDelayDays)
    val viewProgress: ViewProgress = ViewProgress(
      if (latestRiskingStatus == CLAIM_QUERIED
        || latestRiskingStatus == REPAYMENT_APPROVED
        || latestRiskingStatus == INITIAL
        || latestRiskingStatus == SENT_FOR_RISKING) latestUpdate.repaymentDetailsData.originalPostingAmount
      else latestUpdate.repaymentDetailsData.vatToPay_BOX5,
      latestUpdate.repaymentDetailsData.returnCreationDate,
      estRepaymentDate,
      periodFormatter.formatPeriodKey(periodKey.value),
      computeWhatsHappenedSoFarList(estRepaymentDate, vrd, bankDetailsExist, returnCreditChargeExists, addressDetails, bankDetails, returnDebitChargeExists, transactionForPeriodKey))

    if (viewProgress.amount == 0 && latestRiskingStatus != CLAIM_QUERIED) {
      logger.warn(s"KNOZ: zero amount- riskingStatus: ${latestRiskingStatus.toString}, " +
        s"lst: ${
          vrd.map(a => s"[Status ${a.repaymentDetailsData.riskingStatus}, " +
            s"origAmt: ${a.repaymentDetailsData.originalPostingAmount} " +
            s"BOX5: ${a.repaymentDetailsData.vatToPay_BOX5}]").toList.mkString("[", ",", "]")
        }  " +
        s"viewProgress=$viewProgress")
    }

    Ok(
      view_progress(
        viewProgress,
        showEstimatedRepaymentDate(vrd),
        viewProgress.whatsHappenedSoFar.head.amountDescription,
        viewProgress.whatsHappenedSoFar.head.pageTitle,
        viewProgress.whatsHappenedSoFar.head.isComplete,
        showPayUrl(viewProgress.whatsHappenedSoFar.head),
        (viewProgress.amount * 100).longValue
      )
    )
  }

  private def showPayUrl(whatsHappenedSoFar: WhatsHappendSoFar): Boolean =
    if (whatsHappenedSoFar.isComplete) false
    else whatsHappenedSoFar.riskingStatus == ADJUSMENT_TO_TAX_DUE

  private def computeWhatsHappenedSoFarList(estRepaymentDate:         LocalDate,
                                            vrd:                      NonEmptyList[VrtRepaymentDetailData],
                                            bankDetailsExist:         Boolean,
                                            returnCreditChargeExists: Boolean,
                                            addressDetails:           Option[String],
                                            bankDetailsOption:        Option[BankDetails],
                                            returnDebitChargeExists:  Boolean,
                                            transaction:              Option[Transaction])(implicit messages: Messages): NonEmptyList[WhatsHappendSoFar] = {

    //If a row is now complete because the call to 1166 brings back data , we want to show the completed row and the non completed row.

    val nonCompleteRows: NonEmptyList[WhatsHappendSoFar] =
      desFormatter.addMissingStatus(vrd)
        .sortBy(s => (s.repaymentDetailsData.sorted, s.repaymentDetailsData.lastUpdateReceivedDate))
        .map (m => computeWhatsHappenedSoFar(estRepaymentDate, m, bankDetailsExist, bankDetailsOption))

    val completedCreditRows: List[WhatsHappendSoFar] = if (returnCreditChargeExists)
      vrd.filter(f => f.repaymentDetailsData.riskingStatus == REPAYMENT_ADJUSTED || f.repaymentDetailsData.riskingStatus == REPAYMENT_APPROVED)
        .map (m => computeWhatsHappenedSoFarCompleteCreditCharge(m, bankDetailsExist, addressDetails, bankDetailsOption, transaction))
    else List()

    val completedDebitRows: List[WhatsHappendSoFar] = if (returnDebitChargeExists) vrd.filter(f => f.repaymentDetailsData.riskingStatus == ADJUSMENT_TO_TAX_DUE)
      .map (m => computeWhatsHappenedSoFarCompleteDebitCharge(m, transaction))
    else List()

    nonCompleteRows.prependList(completedCreditRows ::: completedDebitRows)
  }

  private def computeWhatsHappenedSoFar(estRepaymentDate:       LocalDate,
                                        vrtRepaymentDetailData: VrtRepaymentDetailData,
                                        bankDetailsExist:       Boolean,
                                        bankDetailsOption:      Option[BankDetails])(implicit message: Messages): WhatsHappendSoFar =
    vrtRepaymentDetailData.repaymentDetailsData.riskingStatus match {

      //id:1
      case INITIAL => WhatsHappendSoFar(INITIAL,
                                        vrtRepaymentDetailData.repaymentDetailsData.lastUpdateReceivedDate.getOrElse(vrtRepaymentDetailData.repaymentDetailsData.returnCreationDate),
                                        Messages("view_progress_formatter.checking_amount"),
                                        Messages("view_progress_formatter.we_received_your_return"),
                                        Messages("view_progress_formatter.amount_you_claimed"),
                                        Messages("view_progress_formatter.repayment_being_processed")
      )

      case CLAIM_QUERIED =>

        val date = vrtRepaymentDetailData.repaymentDetailsData.lastUpdateReceivedDate.getOrElse(vrtRepaymentDetailData.repaymentDetailsData.returnCreationDate)

        if (estRepaymentDate isBefore LocalDate.now())
          //id:4
          WhatsHappendSoFar(CLAIM_QUERIED,
                            date,
                            Messages("view_progress_formatter.estimated_repayment_date_passed"),
                            Messages("view_progress_formatter.do_not_need_anything"),
                            Messages("view_progress_formatter.amount_you_claimed"),
                            Messages("view_progress_formatter.repayment_delayed")
          )
        else
          //id:2
          WhatsHappendSoFar(CLAIM_QUERIED,
                            date,
                            Messages("view_progress_formatter.sending_for_further_checks"),
                            Messages("view_progress_formatter.making_sure_right_amount"),
                            Messages("view_progress_formatter.amount_you_claimed"),
                            Messages("view_progress_formatter.repayment_being_processed")
          )

      case SENT_FOR_RISKING =>

        val date = vrtRepaymentDetailData.repaymentDetailsData.sentForRiskingDate.getOrElse(vrtRepaymentDetailData.repaymentDetailsData.lastUpdateReceivedDate.getOrElse(vrtRepaymentDetailData.repaymentDetailsData.returnCreationDate))

        if (estRepaymentDate isBefore LocalDate.now())
          //id:4
          WhatsHappendSoFar(SENT_FOR_RISKING,
                            date,
                            Messages("view_progress_formatter.estimated_repayment_date_passed"),
                            Messages("view_progress_formatter.do_not_need_anything"),
                            Messages("view_progress_formatter.amount_you_claimed"),
                            Messages("view_progress_formatter.repayment_delayed")
          )
        else
          //id:2
          WhatsHappendSoFar(SENT_FOR_RISKING,
                            date,
                            Messages("view_progress_formatter.sending_for_further_checks"),
                            Messages("view_progress_formatter.making_sure_right_amount"),
                            Messages("view_progress_formatter.amount_you_claimed"),
                            Messages("view_progress_formatter.repayment_being_processed")
          )

      case REPAYMENT_ADJUSTED =>
        //id: 3
        WhatsHappendSoFar(REPAYMENT_ADJUSTED,
                          vrtRepaymentDetailData.repaymentDetailsData.lastUpdateReceivedDate.getOrElse(vrtRepaymentDetailData.repaymentDetailsData.returnCreationDate),
                          Messages("view_progress_formatter.repayment_amount_changed"),
          if (bankDetailsExist) {
            bankDetailsOption match {
              case Some(_) => Messages("view_progress_formatter.claimed_vat_repayment_of", CommonFormatter.formatAmount(vrtRepaymentDetailData.repaymentDetailsData.originalPostingAmount), CommonFormatter.formatAmount(vrtRepaymentDetailData.repaymentDetailsData.vatToPay_BOX5), viewConfig.viewVatAccount)
              case None    => throw new RuntimeException("No Bank details")
            }
          } else {
            Messages("view_progress_formatter.claimed_vat_repayment_post", CommonFormatter.formatAmount(vrtRepaymentDetailData.repaymentDetailsData.originalPostingAmount), CommonFormatter.formatAmount(vrtRepaymentDetailData.repaymentDetailsData.vatToPay_BOX5), viewConfig.viewVatAccount)
          }, Messages("view_progress_formatter.amount_we_pay_you"),
                          Messages("view_progress_formatter.repayment_has_been_approved"))

      case ADJUSMENT_TO_TAX_DUE =>
        //id:5
        WhatsHappendSoFar(ADJUSMENT_TO_TAX_DUE,
                          vrtRepaymentDetailData.repaymentDetailsData.lastUpdateReceivedDate.getOrElse(vrtRepaymentDetailData.repaymentDetailsData.returnCreationDate),
                          Messages("view_progress_formatter.now_owe_hmrc"),
                          Messages("view_progress_formatter.calculated_original_amount_claimed", CommonFormatter.formatAmount(vrtRepaymentDetailData.repaymentDetailsData.originalPostingAmount),
                                                                                                 CommonFormatter.formatAmount(vrtRepaymentDetailData.repaymentDetailsData.vatToPay_BOX5)),
                          Messages("view_progress_formatter.amount_to_pay"), Messages("view_progress_formatter.need_to_make_vat_payment"))

      case REPAYMENT_APPROVED =>
        //id:6
        WhatsHappendSoFar(REPAYMENT_APPROVED,
                          vrtRepaymentDetailData.repaymentDetailsData.lastUpdateReceivedDate.getOrElse(vrtRepaymentDetailData.repaymentDetailsData.returnCreationDate),
                          Messages("view_progress_formatter.repayment_approved"),
          if (bankDetailsExist) Messages("view_progress_formatter.send_repayment_bank_account") else Messages("view_progress_formatter.send_cheque_to_business_address", viewConfig.viewVatAccount),
                          Messages("view_progress_formatter.amount_we_pay_you"), Messages("view_progress_formatter.your_repayment_approved"))
      case REPAYMENT_SUSPENDED =>
        WhatsHappendSoFar(REPAYMENT_SUSPENDED,
                          vrtRepaymentDetailData.repaymentDetailsData.lastUpdateReceivedDate.getOrElse(vrtRepaymentDetailData.repaymentDetailsData.returnCreationDate),
                          Messages("view_progress_formatter.must_submit_latest_vat_return"),
                          Messages("view_progress_formatter.cannot_process_your_repayment"),
                          Messages("view_progress_formatter.amount_to_pay"),
                          Messages("view_progress_formatter.repayment_suspended"),
        )
    }

  private def computeWhatsHappenedSoFarCompleteCreditCharge(vrtRepaymentDetailData: VrtRepaymentDetailData,
                                                            bankDetailsExist:       Boolean,
                                                            addressDetails:         Option[String],
                                                            bankDetailsOption:      Option[BankDetails],
                                                            transaction:            Option[Transaction])(implicit messages: Messages): WhatsHappendSoFar =
    vrtRepaymentDetailData.repaymentDetailsData.riskingStatus match {

      case REPAYMENT_ADJUSTED =>
        //id:7
        WhatsHappendSoFar(REPAYMENT_ADJUSTED,
                          desFormatter.getClearingDate(transaction).getOrElse(vrtRepaymentDetailData.repaymentDetailsData.returnCreationDate),
                          Messages("view_progress_formatter.repayment_complete"),
          if (bankDetailsExist) {
            bankDetailsOption match {
              case Some(bankDetails) => Messages("view_progress_formatter.repayment_complete_bank_details_adjusted", bankDetails.formatAccountHolderName, bankDetails.obscureBankAccountNumber, bankDetails.formatSortCode, CommonFormatter.formatAmount(vrtRepaymentDetailData.repaymentDetailsData.vatToPay_BOX5))
              case None              => throw new RuntimeException("No Bank details")
            }

          } else {
            Messages("view_progress_formatter.repayment_complete_address_adjusted", addressDetails.getOrElse(Messages("view_progress_formatter.address_not_available")), CommonFormatter.formatAmount(vrtRepaymentDetailData.repaymentDetailsData.vatToPay_BOX5))
          }, Messages("view_progress_formatter.amount_we_paid_you"),
                          Messages("view_progress_formatter.repayment_is_complete"), isComplete = true)
      case REPAYMENT_APPROVED =>
        //id: 9
        WhatsHappendSoFar(REPAYMENT_APPROVED,
                          desFormatter.getClearingDate(transaction).getOrElse(vrtRepaymentDetailData.repaymentDetailsData.returnCreationDate),
                          Messages("view_progress_formatter.repayment_complete"),
          if (bankDetailsExist) {
            bankDetailsOption match {
              case Some(bankDetails) => Messages("view_progress_formatter.repayment_complete_bank_details", bankDetails.formatAccountHolderName, bankDetails.obscureBankAccountNumber, bankDetails.formatSortCode, CommonFormatter.formatAmount(vrtRepaymentDetailData.repaymentDetailsData.originalPostingAmount))
              case None              => throw new RuntimeException("No Bank details")
            }

          } else {
            Messages("view_progress_formatter.repayment_complete_address", addressDetails.getOrElse(Messages("view_progress_formatter.address_not_available")), CommonFormatter.formatAmount(vrtRepaymentDetailData.repaymentDetailsData.originalPostingAmount))
          }, Messages("view_progress_formatter.amount_we_paid_you"), Messages("view_progress_formatter.repayment_is_complete"), isComplete = true)
      case status =>
        throw new RuntimeException(s"Illegal state reached: building completed repayment view for $status")
    }

  private def computeWhatsHappenedSoFarCompleteDebitCharge(vrtRepaymentDetailData: VrtRepaymentDetailData, transaction: Option[Transaction])(implicit messages: Messages): WhatsHappendSoFar =
    vrtRepaymentDetailData.repaymentDetailsData.riskingStatus match {
      case ADJUSMENT_TO_TAX_DUE =>
        //id:8 -- to complete
        WhatsHappendSoFar(ADJUSMENT_TO_TAX_DUE,
                          desFormatter.getClearingDate(transaction).getOrElse(vrtRepaymentDetailData.repaymentDetailsData.returnCreationDate),
                          Messages("view_progress_formatter.repayment_complete"),
                          Messages("view_progress_formatter.received_your_vat_payment"),
                          Messages("view_progress_formatter.amount_you_paid"),
                          Messages("view_progress_formatter.repayment_is_complete"), isComplete = true)
      case status =>
        throw new RuntimeException(s"Illegal state reached: building completed repayment view for $status")
    }

  private def getEstimatedRepaymentDate(returnCreationDate: LocalDate, supplementDelayDays: Option[Int]): LocalDate =
    returnCreationDate.plusDays(supplementDelayDays.getOrElse(0) + 30)

  private def showEstimatedRepaymentDate(vrd: NonEmptyList[VrtRepaymentDetailData]): Boolean =
    !vrd.exists(f => (f.repaymentDetailsData.riskingStatus == REPAYMENT_ADJUSTED ||
      f.repaymentDetailsData.riskingStatus == ADJUSMENT_TO_TAX_DUE) || f.repaymentDetailsData.riskingStatus == REPAYMENT_APPROVED)

}
