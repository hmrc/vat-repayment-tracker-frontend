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

package formaters

import java.time.LocalDate

import config.ViewConfig
import javax.inject.{Inject, Singleton}
import langswitch.LangMessages
import model._
import model.des._
import play.api.mvc.{Request, Result, Results}
import views.Views
import req.RequestSupport

import scala.concurrent.ExecutionContext

@Singleton
class ViewProgressFormatter @Inject() (views:           Views,
                                       requestSupport:  RequestSupport,
                                       desFormatter:    DesFormatter,
                                       viewConfig:      ViewConfig,
                                       periodFormatter: PeriodFormatter)(implicit ec: ExecutionContext) extends Results {

  import requestSupport._

  def computeViewProgress(
      vrn:           Vrn,
      periodKey:     PeriodKey,
      vrd:           List[VrtRepaymentDetailData],
      customerData:  Option[CustomerInformation],
      financialData: Option[FinancialData]
  )(implicit request: Request[_]): Result = {

    val bankDetailsExist = desFormatter.getBankDetailsExist(customerData)
    val bankDetails = desFormatter.getBankDetails(customerData)
    val addressDetails = desFormatter.getAddressDetails(customerData)
    val returnCreditChargeExists = desFormatter.getReturnCreditChargeExists(financialData, periodKey)
    val returnDebitChargeExists = desFormatter.getReturnDebitChargeExists(financialData, periodKey)

    val estRepaymentDate = getEstimatedRepaymentDate(vrd(0).repaymentDetailsData.returnCreationDate, vrd(0).repaymentDetailsData.supplementDelayDays)
    val viewProgress: ViewProgress = ViewProgress(
      if (vrd(0).repaymentDetailsData.riskingStatus == CLAIM_QUERIED.value) vrd(0).repaymentDetailsData.originalPostingAmount else vrd(0).repaymentDetailsData.vatToPay_BOX5_InPounds,
      vrd(0).repaymentDetailsData.returnCreationDate,
      estRepaymentDate,
      periodFormatter.formatPeriodKey(periodKey.value),
      computeWhatsHappenedSoFarList(estRepaymentDate, vrd, bankDetailsExist, returnCreditChargeExists, addressDetails, bankDetails, returnDebitChargeExists))

    Ok(views.view_progress(vrn, viewProgress, showEstimatedRepaymentDate(vrd), viewProgress.whatsHappenedSoFar(0).amountDescription, viewProgress.whatsHappenedSoFar(0).pageTitle,
                           viewProgress.whatsHappenedSoFar(0).isComplete, showPayUrl(viewProgress.whatsHappenedSoFar(0)), (viewProgress.amount * 100).longValue()))
  }

  private def showPayUrl(whatsHappendSoFar: WhatsHappendSoFar): Boolean = {
    if (whatsHappendSoFar.isComplete) false
    else if (whatsHappendSoFar.riskingStatus == ADJUSMENT_TO_TAX_DUE.value) true
    else false
  }

  private def computeWhatsHappenedSoFarList(estRepaymentDate:         LocalDate,
                                            vrd:                      List[VrtRepaymentDetailData],
                                            bankDetailsExist:         Boolean,
                                            returnCreditChargeExists: Boolean,
                                            addressDetails:           Option[String],
                                            bankDetailsOption:        Option[BankDetails],
                                            returnDebitChargeExists:  Boolean)(implicit request: Request[_]): List[WhatsHappendSoFar] = {
    implicit val localDateOrdering: Ordering[LocalDate] = Ordering.fromLessThan(_ isAfter _)

    //If a row is now complete because the call to 1166 brings back data , we want to show the completed row and the non completed row.

    val nonCompleteRows: List[WhatsHappendSoFar] = desFormatter.addMissingStatus(vrd).sortBy(s => (s.repaymentDetailsData.sorted, s.repaymentDetailsData.lastUpdateReceivedDate)).map (m => computeWhatsHappenedSoFar(estRepaymentDate, m, bankDetailsExist, addressDetails, bankDetailsOption))

    val completedCreditRows: List[WhatsHappendSoFar] = if (returnCreditChargeExists)
      vrd.filter(f => f.repaymentDetailsData.riskingStatus == REPAYMENT_ADJUSTED.value || f.repaymentDetailsData.riskingStatus == REPAYMENT_APPROVED.value)
        .map (m => computeWhatsHappenedSoFarCompleteCreditCharge(estRepaymentDate, m, bankDetailsExist, addressDetails, bankDetailsOption))
    else List()

    val completedDebitRows: List[WhatsHappendSoFar] = if (returnDebitChargeExists) vrd.filter(f => f.repaymentDetailsData.riskingStatus == ADJUSMENT_TO_TAX_DUE.value)
      .map (m => computeWhatsHappenedSoFarCompleteDebitCharge(estRepaymentDate, m))
    else List()

    completedCreditRows ::: completedDebitRows ::: nonCompleteRows

  }

  private def computeWhatsHappenedSoFar(estRepaymentDate:       LocalDate,
                                        vrtRepaymentDetailData: VrtRepaymentDetailData,
                                        bankDetailsExist:       Boolean,
                                        addressDetails:         Option[String],
                                        bankDetailsOption:      Option[BankDetails])(implicit request: Request[_]): WhatsHappendSoFar = {

    vrtRepaymentDetailData.repaymentDetailsData.riskingStatus match {

      //id:1
      case INITIAL.value => WhatsHappendSoFar(INITIAL.value,
                                              vrtRepaymentDetailData.repaymentDetailsData.lastUpdateReceivedDate.getOrElse(vrtRepaymentDetailData.repaymentDetailsData.returnCreationDate),
                                              LangMessages.`Checking amount`.show,
                                              LangMessages.`We received your return`.show,
                                              LangMessages.`Amount you claimed`.show,
                                              LangMessages.`Your repayment is being processed`.show
      )

      case CLAIM_QUERIED.value =>

        val date = vrtRepaymentDetailData.repaymentDetailsData.lastUpdateReceivedDate.getOrElse(vrtRepaymentDetailData.repaymentDetailsData.returnCreationDate)

        if (estRepaymentDate isBefore LocalDate.now())
          //id:4
          WhatsHappendSoFar(CLAIM_QUERIED.value,
                            date,
                            LangMessages.`Estimated repayment date has passed`.show,
                            LangMessages.`You do not need to do anything right now`.show,
                            LangMessages.`Amount you claimed`.show,
                            LangMessages.`Your repayment is delayed`.show
          )
        else
          //id:2
          WhatsHappendSoFar(CLAIM_QUERIED.value,
                            date,
                            LangMessages.`Sending for further checks`.show,
                            LangMessages.`We are making sure we pay you the right amount`.show,
                            LangMessages.`Amount you claimed`.show,
                            LangMessages.`Your repayment is being processed`.show
          )

      case SENT_FOR_RISKING.value =>

        val date = vrtRepaymentDetailData.repaymentDetailsData.sentForRiskingDate.getOrElse(vrtRepaymentDetailData.repaymentDetailsData.lastUpdateReceivedDate.getOrElse(vrtRepaymentDetailData.repaymentDetailsData.returnCreationDate))

        if (estRepaymentDate isBefore LocalDate.now())
          //id:4
          WhatsHappendSoFar(SENT_FOR_RISKING.value,
                            date,
                            LangMessages.`Estimated repayment date has passed`.show,
                            LangMessages.`You do not need to do anything right now`.show,
                            LangMessages.`Amount you claimed`.show,
                            LangMessages.`Your repayment is delayed`.show
          )
        else
          //id:2
          WhatsHappendSoFar(SENT_FOR_RISKING.value,
                            date,
                            LangMessages.`Sending for further checks`.show,
                            LangMessages.`We are making sure we pay you the right amount`.show,
                            LangMessages.`Amount you claimed`.show,
                            LangMessages.`Your repayment is being processed`.show
          )

      case REPAYMENT_ADJUSTED.value =>
        //id: 3
        WhatsHappendSoFar(REPAYMENT_ADJUSTED.value,
                          vrtRepaymentDetailData.repaymentDetailsData.lastUpdateReceivedDate.getOrElse(vrtRepaymentDetailData.repaymentDetailsData.returnCreationDate),
                          LangMessages.`Repayment amount changed`.show,
          if (bankDetailsExist) {
            bankDetailsOption match {
              case Some(_) => LangMessages.`You claimed a VAT repayment of`(CommonFormatter.formatAmount(vrtRepaymentDetailData.repaymentDetailsData.originalPostingAmount), CommonFormatter.formatAmount(vrtRepaymentDetailData.repaymentDetailsData.vatToPay_BOX5_InPounds), viewConfig.viewVatAccount).show
              case None    => throw new RuntimeException("No Bank details")
            }
          } else {
            LangMessages.`You claimed a VAT repayment of post`(CommonFormatter.formatAmount(vrtRepaymentDetailData.repaymentDetailsData.originalPostingAmount), CommonFormatter.formatAmount(vrtRepaymentDetailData.repaymentDetailsData.vatToPay_BOX5_InPounds), viewConfig.viewVatAccount).show
          }, LangMessages.`Amount we'll pay you`.show,
                          LangMessages.`Your repayment has been approved`.show)

      case ADJUSMENT_TO_TAX_DUE.value =>
        //id:5
        WhatsHappendSoFar(ADJUSMENT_TO_TAX_DUE.value,
                          vrtRepaymentDetailData.repaymentDetailsData.lastUpdateReceivedDate.getOrElse(vrtRepaymentDetailData.repaymentDetailsData.returnCreationDate),
                          LangMessages.`You now owe HMRC`.show,
                          LangMessages.`We calculated that the original amount you claimed of`(CommonFormatter.formatAmount(vrtRepaymentDetailData.repaymentDetailsData.originalPostingAmount),
                                                                                               CommonFormatter.formatAmount(vrtRepaymentDetailData.repaymentDetailsData.vatToPay_BOX5_InPounds)).show,
                          LangMessages.`Amount to pay`.show, LangMessages.`You need to make a VAT payment`.show)

      case REPAYMENT_APPROVED.value =>
        //id:6
        WhatsHappendSoFar(REPAYMENT_APPROVED.value,
                          vrtRepaymentDetailData.repaymentDetailsData.lastUpdateReceivedDate.getOrElse(vrtRepaymentDetailData.repaymentDetailsData.returnCreationDate),
                          LangMessages.`Repayment approved`.show,
          if (bankDetailsExist) LangMessages.`We will send this to your repayment bank account`.show else LangMessages.`We will send a cheque to your business address`(viewConfig.viewVatAccount).show,
                          LangMessages.`Amount we'll pay you`.show, LangMessages.`Your repayment has been approved`.show)
    }
  }

  private def computeWhatsHappenedSoFarCompleteCreditCharge(estRepaymentDate:       LocalDate,
                                                            vrtRepaymentDetailData: VrtRepaymentDetailData,
                                                            bankDetailsExist:       Boolean,
                                                            addressDetails:         Option[String],
                                                            bankDetailsOption:      Option[BankDetails])(implicit request: Request[_]): WhatsHappendSoFar = {

    vrtRepaymentDetailData.repaymentDetailsData.riskingStatus match {

      case REPAYMENT_ADJUSTED.value =>
        //id:7
        WhatsHappendSoFar(REPAYMENT_ADJUSTED.value,
                          vrtRepaymentDetailData.repaymentDetailsData.lastUpdateReceivedDate.getOrElse(vrtRepaymentDetailData.repaymentDetailsData.returnCreationDate),
                          LangMessages.`Repayment complete`.show,
          if (bankDetailsExist) {
            bankDetailsOption match {
              case Some(bankDetails) => LangMessages.`repayment-complete-bank-details-adjusted`(bankDetails.formatAccountHolderName, bankDetails.obscureBankAccountNumber, bankDetails.formatSortCode, CommonFormatter.formatAmount(vrtRepaymentDetailData.repaymentDetailsData.vatToPay_BOX5_InPounds)).show
              case None              => throw new RuntimeException("No Bank details")
            }

          } else {
            LangMessages.`repayment-complete-address-adjusted`(addressDetails.getOrElse(LangMessages.addressNotAvailable.show), CommonFormatter.formatAmount(vrtRepaymentDetailData.repaymentDetailsData.vatToPay_BOX5_InPounds)).show
          }, LangMessages.`Amount we paid you`.show,
                          LangMessages.`Your repayment is complete`.show, "_Y")
      case REPAYMENT_APPROVED.value =>
        //id: 9
        WhatsHappendSoFar(REPAYMENT_APPROVED.value,
                          vrtRepaymentDetailData.repaymentDetailsData.lastUpdateReceivedDate.getOrElse(vrtRepaymentDetailData.repaymentDetailsData.returnCreationDate),
                          LangMessages.`Repayment complete`.show,
          if (bankDetailsExist) {
            bankDetailsOption match {
              case Some(bankDetails) => LangMessages.`repayment-complete-bank-details`(bankDetails.formatAccountHolderName, bankDetails.obscureBankAccountNumber, bankDetails.formatSortCode, CommonFormatter.formatAmount(vrtRepaymentDetailData.repaymentDetailsData.vatToPay_BOX5_InPounds)).show
              case None              => throw new RuntimeException("No Bank details")
            }

          } else {
            LangMessages.`repayment-complete-address`(addressDetails.getOrElse(LangMessages.addressNotAvailable.show), CommonFormatter.formatAmount(vrtRepaymentDetailData.repaymentDetailsData.vatToPay_BOX5_InPounds)).show
          }, LangMessages.`Amount we paid you`.show, LangMessages.`Your repayment is complete`.show, "_Y")
    }

  }

  private def computeWhatsHappenedSoFarCompleteDebitCharge(estRepaymentDate:       LocalDate,
                                                           vrtRepaymentDetailData: VrtRepaymentDetailData)(implicit request: Request[_]): WhatsHappendSoFar = {

    vrtRepaymentDetailData.repaymentDetailsData.riskingStatus match {
      case ADJUSMENT_TO_TAX_DUE.value =>
        //id:8 -- to complete
        WhatsHappendSoFar(ADJUSMENT_TO_TAX_DUE.value,
                          vrtRepaymentDetailData.repaymentDetailsData.lastUpdateReceivedDate.getOrElse(vrtRepaymentDetailData.repaymentDetailsData.returnCreationDate),
                          LangMessages.`Repayment complete`.show,
                          LangMessages.`We received your VAT payment`.show,
                          LangMessages.`Amount you paid`.show,
                          LangMessages.`Your repayment is complete`.show, "_Y")
    }

  }

  private def getEstimatedRepaymentDate(returnCreationDate: LocalDate, supplementDelayDays: Option[Int]): LocalDate =
    {
      returnCreationDate.plusDays(supplementDelayDays.getOrElse(0) + 30)
    }

  private def showEstimatedRepaymentDate(vrd: List[VrtRepaymentDetailData]): Boolean = {

    !vrd.exists(f => (f.repaymentDetailsData.riskingStatus == REPAYMENT_ADJUSTED.value ||
      f.repaymentDetailsData.riskingStatus == ADJUSMENT_TO_TAX_DUE.value) || f.repaymentDetailsData.riskingStatus == REPAYMENT_APPROVED.value)

  }

}
