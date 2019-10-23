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
    val addressDetailsExist = desFormatter.getAddressDetailsExist(customerData)
    val returnCreditChargeExists = desFormatter.getReturnCreditChargeExists(financialData, periodKey)
    val returnDebitChargeExists = desFormatter.getReturnDebitChargeExists(financialData, periodKey)

    val estRepaymentDate = getEstimatedRepaymentDate(vrd(0).repaymentDetailsData.returnCreationDate, vrd(0).repaymentDetailsData.supplementDelayDays)
    val viewProgress: ViewProgress = ViewProgress(
      computeViewProgressTitle(estRepaymentDate, vrd),
      vrd(0).repaymentDetailsData.vatToPay_BOX5.getOrElse(0),
      vrd(0).repaymentDetailsData.returnCreationDate,
      estRepaymentDate,
      desFormatter.formatPeriodKey(periodKey.value),
      computeWhatsHappenedSoFarList(estRepaymentDate, vrd, bankDetailsExist, returnCreditChargeExists, addressDetails, bankDetails, returnDebitChargeExists))
    Ok(views.view_progress(vrn, viewProgress))
  }

  private def computeViewProgressTitle(
      estRepaymentDate: LocalDate,
      vrd:              List[VrtRepaymentDetailData])(implicit request: Request[_]): String = {
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

  private def computeWhatsHappenedSoFarList(estRepaymentDate:         LocalDate,
                                            vrd:                      List[VrtRepaymentDetailData],
                                            bankDetailsExist:         Boolean,
                                            returnCreditChargeExists: Boolean,
                                            addressDetails:           Option[String],
                                            bankDetailsOption:        Option[BankDetails],
                                            returnDebitChargeExists:  Boolean)(implicit request: Request[_]): List[WhatsHappendSoFar] = {
    implicit val localDateOrdering: Ordering[LocalDate] = Ordering.fromLessThan(_ isAfter _)

    val containsInitialSatus: Boolean = vrd.filter(f => f.repaymentDetailsData.riskingStatus == INITIAL.value).size > 0

    desFormatter.addMissingStatus(vrd).sortBy(s => (s.repaymentDetailsData.sorted, s.repaymentDetailsData.lastUpdateReceivedDate)).map (m => computeWhatsHappenedSoFar(estRepaymentDate, m, bankDetailsExist, returnCreditChargeExists, addressDetails, bankDetailsOption, returnDebitChargeExists))
  }

  private def computeWhatsHappenedSoFar(estRepaymentDate:         LocalDate,
                                        vrtRepaymentDetailData:   VrtRepaymentDetailData,
                                        bankDetailsExist:         Boolean,
                                        returnCreditChargeExists: Boolean,
                                        addressDetails:           Option[String],
                                        bankDetailsOption:        Option[BankDetails],
                                        returnDebitChargeExists:  Boolean)(implicit request: Request[_]): WhatsHappendSoFar = {

    vrtRepaymentDetailData.repaymentDetailsData.riskingStatus match {

      //id:1
      case INITIAL.value => WhatsHappendSoFar(
        vrtRepaymentDetailData.repaymentDetailsData.lastUpdateReceivedDate.getOrElse(vrtRepaymentDetailData.repaymentDetailsData.returnCreationDate),
        LangMessages.`Checking VAT repayment`.show,
        LangMessages.`We received your return`.show
      )

      case CLAIM_QUERIED.value | SENT_FOR_RISKING.value => {

        val date =
          if (vrtRepaymentDetailData.repaymentDetailsData.riskingStatus == SENT_FOR_RISKING.value) vrtRepaymentDetailData.repaymentDetailsData.sentForRiskingDate.getOrElse(vrtRepaymentDetailData.repaymentDetailsData.lastUpdateReceivedDate.getOrElse(vrtRepaymentDetailData.repaymentDetailsData.returnCreationDate))
          else vrtRepaymentDetailData.repaymentDetailsData.lastUpdateReceivedDate.getOrElse(vrtRepaymentDetailData.repaymentDetailsData.returnCreationDate)

        if (estRepaymentDate isBefore LocalDate.now())
          //id:2
          WhatsHappendSoFar(
            date,
            LangMessages.`Sending for further checks`.show,
            LangMessages.`We are making sure we pay you the right amount`.show
          )
        else
          //id:4
          WhatsHappendSoFar(
            date,
            LangMessages.`Estimated repayment date has passed`.show,
            LangMessages.`You do not need to do anything right now`.show
          )
      }

      case REPAYMENT_ADJUSTED.value => {
        if (returnCreditChargeExists) {
          //id:7
          WhatsHappendSoFar(
            vrtRepaymentDetailData.repaymentDetailsData.lastUpdateReceivedDate.getOrElse(vrtRepaymentDetailData.repaymentDetailsData.returnCreationDate),
            LangMessages.`Repayment complete`.show,
            if (bankDetailsExist) {
              bankDetailsOption match {
                case Some(bankDetails) => LangMessages.`repayment-complete-bank-details-adjusted`(bankDetails.formatAccountHolderName, bankDetails.obscureBankAccountNumber, bankDetails.formatSortCode, desFormatter.formatAmount(vrtRepaymentDetailData.repaymentDetailsData.vatToPay_BOX5.getOrElse(0))).show
                case None              => throw new RuntimeException("No Bank details")
              }

            } else {
              LangMessages.`repayment-complete-address-adjusted`(addressDetails.getOrElse(LangMessages.addressNotAvailable.show), desFormatter.formatAmount(vrtRepaymentDetailData.repaymentDetailsData.vatToPay_BOX5.getOrElse(0))).show
            }
          )
        } else
          //id: 3
          WhatsHappendSoFar(
            vrtRepaymentDetailData.repaymentDetailsData.lastUpdateReceivedDate.getOrElse(vrtRepaymentDetailData.repaymentDetailsData.returnCreationDate),
            LangMessages.`Your VAT repayment amount changed`.show,
            if (bankDetailsExist) {
              bankDetailsOption match {
                case Some(bankDetails) => LangMessages.`You claimed a VAT repayment of`(desFormatter.formatAmount(vrtRepaymentDetailData.repaymentDetailsData.originalPostingAmount), desFormatter.formatAmount(vrtRepaymentDetailData.repaymentDetailsData.vatToPay_BOX5.getOrElse(0))).show
                case None              => throw new RuntimeException("No Bank details")
              }
            } else {
              LangMessages.`We will send a cheque to your business address`.show
            }
          )
      }

      case ADJUSTMENT_TO_TAX_DUE.value => {
        if (returnDebitChargeExists)
          //id:8 -- to complete
          WhatsHappendSoFar(
            vrtRepaymentDetailData.repaymentDetailsData.lastUpdateReceivedDate.getOrElse(vrtRepaymentDetailData.repaymentDetailsData.returnCreationDate),
            LangMessages.`Repayment complete`.show,
            LangMessages.`We received your VAT payment`.show)
        else {
          //id:5
          WhatsHappendSoFar(
            vrtRepaymentDetailData.repaymentDetailsData.lastUpdateReceivedDate.getOrElse(vrtRepaymentDetailData.repaymentDetailsData.returnCreationDate),
            LangMessages.`You now owe HMRC`.show,
            LangMessages.`We calculated that the original amount you claimed of`(desFormatter.formatAmount(vrtRepaymentDetailData.repaymentDetailsData.originalPostingAmount), desFormatter.formatAmount(vrtRepaymentDetailData.repaymentDetailsData.vatToPay_BOX5.getOrElse(0))).show)
        }
      }

      case REPAYMENT_APPROVED.value =>
        if (returnCreditChargeExists) {
          //id: 9
          WhatsHappendSoFar(
            vrtRepaymentDetailData.repaymentDetailsData.lastUpdateReceivedDate.getOrElse(vrtRepaymentDetailData.repaymentDetailsData.returnCreationDate),
            LangMessages.`Repayment complete`.show,
            if (bankDetailsExist) {
              bankDetailsOption match {
                case Some(bankDetails) => LangMessages.`repayment-complete-bank-details`(bankDetails.formatAccountHolderName, bankDetails.obscureBankAccountNumber, bankDetails.formatSortCode, desFormatter.formatAmount(vrtRepaymentDetailData.repaymentDetailsData.vatToPay_BOX5.getOrElse(0))).show
                case None              => throw new RuntimeException("No Bank details")
              }

            } else {
              LangMessages.`repayment-complete-address`(addressDetails.getOrElse(LangMessages.addressNotAvailable.show), desFormatter.formatAmount(vrtRepaymentDetailData.repaymentDetailsData.vatToPay_BOX5.getOrElse(0))).show
            }
          )
        } else {
          //id:6
          WhatsHappendSoFar(
            vrtRepaymentDetailData.repaymentDetailsData.lastUpdateReceivedDate.getOrElse(vrtRepaymentDetailData.repaymentDetailsData.returnCreationDate),
            LangMessages.`Repayment approved`.show,
            if (bankDetailsExist) LangMessages.`We will send this to your repayment bank account`.show else LangMessages.`We will send a cheque to your business address`.show)
        }
    }
  }

  private def getEstimatedRepaymentDate(returnCreationDate: LocalDate, supplementDelayDays: Option[Int]): LocalDate =
    {
      returnCreationDate.plusDays(supplementDelayDays.getOrElse(0) + 30)
    }

}
