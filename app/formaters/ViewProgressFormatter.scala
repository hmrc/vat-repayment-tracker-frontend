/*
 * Copyright 2021 HM Revenue & Customs
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
import config.ViewConfig
import javax.inject.{Inject, Singleton}
import model._
import model.des.RiskingStatus.{ADJUSMENT_TO_TAX_DUE, CLAIM_QUERIED, INITIAL, REPAYMENT_ADJUSTED, REPAYMENT_APPROVED, REPAYMENT_SUSPENDED, SENT_FOR_RISKING}
import model.des._
import play.api.i18n.Messages
import play.api.libs.json.JsResult.Exception
import play.api.mvc.{Request, Result, Results}
import req.RequestSupport

import scala.concurrent.ExecutionContext

@Singleton
class ViewProgressFormatter @Inject() (
                                        view_progress: views.html.view_progress,
                                       requestSupport:  RequestSupport,
                                       desFormatter:    DesFormatter,
                                       viewConfig:      ViewConfig,
                                       periodFormatter: PeriodFormatter)(implicit ec: ExecutionContext) extends Results {

  def computeViewProgress(
      vrn:           Vrn,
      periodKey:     PeriodKey,
      vrd:           List[VrtRepaymentDetailData],
      customerData:  Option[CustomerInformation],
      financialData: Option[FinancialData]
  )(implicit request: Request[_], messages: Messages): Result = {

    val bankDetailsExist = desFormatter.getBankDetailsExist(customerData)
    val bankDetails = desFormatter.getBankDetails(customerData)
    val addressDetails = desFormatter.getAddressDetails(customerData)
    val returnCreditChargeExists = desFormatter.getReturnCreditChargeExists(financialData, periodKey)
    val returnDebitChargeExists = desFormatter.getReturnDebitChargeExists(financialData, periodKey)

    val estRepaymentDate = getEstimatedRepaymentDate(vrd(0).repaymentDetailsData.returnCreationDate, vrd(0).repaymentDetailsData.supplementDelayDays)
    val viewProgress: ViewProgress = ViewProgress(
      if (vrd(0).repaymentDetailsData.riskingStatus == CLAIM_QUERIED) vrd(0).repaymentDetailsData.originalPostingAmount else vrd(0).repaymentDetailsData.vatToPay_BOX5,
      vrd(0).repaymentDetailsData.returnCreationDate,
      estRepaymentDate,
      periodFormatter.formatPeriodKey(periodKey.value),
      computeWhatsHappenedSoFarList(estRepaymentDate, vrd, bankDetailsExist, returnCreditChargeExists, addressDetails, bankDetails, returnDebitChargeExists))

    Ok(view_progress(vrn, viewProgress, showEstimatedRepaymentDate(vrd), viewProgress.whatsHappenedSoFar(0).amountDescription, viewProgress.whatsHappenedSoFar(0).pageTitle,
                           viewProgress.whatsHappenedSoFar(0).isComplete, showPayUrl(viewProgress.whatsHappenedSoFar(0)), (viewProgress.amount * 100).longValue()))
  }

  private def showPayUrl(whatsHappendSoFar: WhatsHappendSoFar): Boolean = {
    if (whatsHappendSoFar.isComplete) false
    else if (whatsHappendSoFar.riskingStatus == ADJUSMENT_TO_TAX_DUE) true
    else false
  }

  private def computeWhatsHappenedSoFarList(estRepaymentDate:         LocalDate,
                                            vrd:                      List[VrtRepaymentDetailData],
                                            bankDetailsExist:         Boolean,
                                            returnCreditChargeExists: Boolean,
                                            addressDetails:           Option[String],
                                            bankDetailsOption:        Option[BankDetails],
                                            returnDebitChargeExists:  Boolean)(implicit request: Request[_], messages: Messages): List[WhatsHappendSoFar] = {
    implicit val localDateOrdering: Ordering[LocalDate] = Ordering.fromLessThan(_ isAfter _)

    //If a row is now complete because the call to 1166 brings back data , we want to show the completed row and the non completed row.

    val nonCompleteRows: List[WhatsHappendSoFar] = desFormatter.addMissingStatus(vrd).sortBy(s => (s.repaymentDetailsData.sorted, s.repaymentDetailsData.lastUpdateReceivedDate)).map (m => computeWhatsHappenedSoFar(estRepaymentDate, m, bankDetailsExist, addressDetails, bankDetailsOption))

    val completedCreditRows: List[WhatsHappendSoFar] = if (returnCreditChargeExists)
      vrd.filter(f => f.repaymentDetailsData.riskingStatus == REPAYMENT_ADJUSTED || f.repaymentDetailsData.riskingStatus == REPAYMENT_APPROVED)
        .map (m => computeWhatsHappenedSoFarCompleteCreditCharge(estRepaymentDate, m, bankDetailsExist, addressDetails, bankDetailsOption))
    else List()

    val completedDebitRows: List[WhatsHappendSoFar] = if (returnDebitChargeExists) vrd.filter(f => f.repaymentDetailsData.riskingStatus == ADJUSMENT_TO_TAX_DUE)
      .map (m => computeWhatsHappenedSoFarCompleteDebitCharge(estRepaymentDate, m))
    else List()

    completedCreditRows ::: completedDebitRows ::: nonCompleteRows

  }

  private def computeWhatsHappenedSoFar(estRepaymentDate:       LocalDate,
                                        vrtRepaymentDetailData: VrtRepaymentDetailData,
                                        bankDetailsExist:       Boolean,
                                        addressDetails:         Option[String],
                                        bankDetailsOption:      Option[BankDetails])(implicit request: Request[_], message: Messages): WhatsHappendSoFar = {

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
  }

  private def computeWhatsHappenedSoFarCompleteCreditCharge(estRepaymentDate:       LocalDate,
                                                            vrtRepaymentDetailData: VrtRepaymentDetailData,
                                                            bankDetailsExist:       Boolean,
                                                            addressDetails:         Option[String],
                                                            bankDetailsOption:      Option[BankDetails])(implicit messages: Messages, request: Request[_]): WhatsHappendSoFar = {

    vrtRepaymentDetailData.repaymentDetailsData.riskingStatus match {

      case REPAYMENT_ADJUSTED =>
        //id:7
        WhatsHappendSoFar(REPAYMENT_ADJUSTED,
                          vrtRepaymentDetailData.repaymentDetailsData.lastUpdateReceivedDate.getOrElse(vrtRepaymentDetailData.repaymentDetailsData.returnCreationDate),
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
                          vrtRepaymentDetailData.repaymentDetailsData.lastUpdateReceivedDate.getOrElse(vrtRepaymentDetailData.repaymentDetailsData.returnCreationDate),
                          Messages("view_progress_formatter.repayment_complete"),
          if (bankDetailsExist) {
            bankDetailsOption match {
              case Some(bankDetails) => Messages("view_progress_formatter.repayment_complete_bank_details", bankDetails.formatAccountHolderName, bankDetails.obscureBankAccountNumber, bankDetails.formatSortCode, CommonFormatter.formatAmount(vrtRepaymentDetailData.repaymentDetailsData.vatToPay_BOX5))
              case None              => throw new RuntimeException("No Bank details")
            }

          } else {
            Messages("view_progress_formatter.repayment_complete_address", addressDetails.getOrElse(Messages("view_progress_formatter.address_not_available")), CommonFormatter.formatAmount(vrtRepaymentDetailData.repaymentDetailsData.vatToPay_BOX5))
          }, Messages("view_progress_formatter.amount_we_paid_you"), Messages("view_progress_formatter.repayment_is_complete"), isComplete = true)
      case status =>
        throw new RuntimeException(s"Illegal state reached: building completed repayment view for $status")
    }

  }

  private def computeWhatsHappenedSoFarCompleteDebitCharge(estRepaymentDate:       LocalDate,
                                                           vrtRepaymentDetailData: VrtRepaymentDetailData)(implicit request: Request[_], messages:Messages): WhatsHappendSoFar = {

    vrtRepaymentDetailData.repaymentDetailsData.riskingStatus match {
      case ADJUSMENT_TO_TAX_DUE =>
        //id:8 -- to complete
        WhatsHappendSoFar(ADJUSMENT_TO_TAX_DUE,
                          vrtRepaymentDetailData.repaymentDetailsData.lastUpdateReceivedDate.getOrElse(vrtRepaymentDetailData.repaymentDetailsData.returnCreationDate),
                          Messages("view_progress_formatter.repayment_complete"),
                          Messages("view_progress_formatter.received_your_vat_payment"),
                          Messages("view_progress_formatter.amount_you_paid"),
                          Messages("view_progress_formatter.repayment_is_complete"), isComplete = true)
      case status =>
        throw new RuntimeException(s"Illegal state reached: building completed repayment view for $status")
    }

  }

  private def getEstimatedRepaymentDate(returnCreationDate: LocalDate, supplementDelayDays: Option[Int]): LocalDate =
    {
      returnCreationDate.plusDays(supplementDelayDays.getOrElse(0) + 30)
    }

  private def showEstimatedRepaymentDate(vrd: List[VrtRepaymentDetailData]): Boolean = {

    !vrd.exists(f => (f.repaymentDetailsData.riskingStatus == REPAYMENT_ADJUSTED ||
      f.repaymentDetailsData.riskingStatus == ADJUSMENT_TO_TAX_DUE) || f.repaymentDetailsData.riskingStatus == REPAYMENT_APPROVED)

  }

}
