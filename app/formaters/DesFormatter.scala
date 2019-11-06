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

import java.text.DecimalFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

import javax.inject.{Inject, Singleton}
import langswitch.LangMessages
import model.des._
import model.{ChargeType, PeriodKey, VrtRepaymentDetailData}
import play.api.Logger
import play.api.mvc.Request
import req.RequestSupport

@Singleton
class DesFormatter @Inject() (addressFormater: AddressFormter, requestSupport: RequestSupport) {

  import requestSupport._

  def addMissingStatus(vrd: List[VrtRepaymentDetailData]): List[VrtRepaymentDetailData] = {

    if (vrd.filter(f => f.repaymentDetailsData.riskingStatus == INITIAL.value).size > 0) {
      vrd
    } else {

      val rdd: RepaymentDetailData = vrd(0).repaymentDetailsData.copy(riskingStatus          = INITIAL.value, lastUpdateReceivedDate = Some(vrd(0).repaymentDetailsData.returnCreationDate))
      val vrtRepaymentDetailData: VrtRepaymentDetailData = vrd(0).copy(repaymentDetailsData = rdd)
      vrtRepaymentDetailData :: vrd
    }
  }

  def getReturnDebitChargeExists(financialData: Option[FinancialData], periodKey: PeriodKey): Boolean = {

    val transactionIterable = for {
      fdAllOption <- financialData
    } yield (fdAllOption.financialTransactions.filter(f => financialTransactionsPredicate(f, ChargeType.vatReturnDebitCharge, periodKey)))

    transactionIterable match {
      case Some(x) => if (x.size > 0) true else false
      case None    => false
    }
  }

  def getReturnCreditChargeExists(financialData: Option[FinancialData], periodKey: PeriodKey): Boolean = {

    val transactionIterable = for {
      fdAllOption <- financialData
    } yield (fdAllOption.financialTransactions.filter(f => financialTransactionsPredicate(f, ChargeType.vatReturnCreditCharge, periodKey)))

    transactionIterable match {
      case Some(x) => if (x.size > 0) true else false
      case None    => false
    }
  }

  private def financialTransactionsPredicate(transaction: Transaction, chargeType: String, periodKey: PeriodKey): Boolean =
    transaction.periodKey.getOrElse("ZZ") == periodKey.value && transaction.chargeType == chargeType

  def getBankDetailsExist(customerData: Option[CustomerInformation]): Boolean = {

    val maybeBankDetailsExist = for {
      cd <- customerData
      ai <- cd.approvedInformation
    } yield ai.bankDetailsExist

    maybeBankDetailsExist.getOrElse(false)

  }

  def getBankDetails(customerData: Option[CustomerInformation]): Option[BankDetails] = {

    for {
      cd <- customerData
      ai <- cd.approvedInformation
      bd <- ai.bankDetails
    } yield (bd)
  }

  def getAddressDetailsExist(customerData: Option[CustomerInformation]): Boolean = customerData match {
    case Some(cd) => cd.approvedInformation match {
      case Some(ai) => ai.addressExists
      case None     => false
    }
    case None => false
  }

  def getAddressDetailsExist2(customerData: Option[CustomerInformation]): Boolean = {

    val maybeExists = for {
      cd <- customerData
      ai <- cd.approvedInformation
    } yield (ai.addressExists)

    maybeExists.getOrElse(false)

  }

  def getDDData(directDebitData: Option[DirectDebitData]): Option[BankDetails] = {

    val ddDetail: Option[DirectDebitDetails] = for {
      dd <- directDebitData
      ddDetailsOption <- dd.directDebitDetails
      ddDetails <- ddDetailsOption.headOption
    } yield (ddDetails)

    ddDetail.map(detail => BankDetails(Some(detail.accountHolderName), Some(detail.accountNumber), Some(detail.sortCode)))

  }

  def getAddressDetails(customerData: Option[CustomerInformation]): Option[String] = {
    for {
      cd <- customerData
      ai <- cd.approvedInformation
      ppob <- ai.PPOB
      ad <- ppob.address
    } yield addressFormater.getFormattedAddress(ad)

  }

  def formatAmount(amount: BigDecimal): String = {
    val df = new DecimalFormat("#,###.00")
    df.format(amount.abs)
  }

  def formatDate(date: LocalDate): String = {
    val pattern1 = DateTimeFormatter.ofPattern("dd MMM yyyy")
    date.format(pattern1)
  }

  def formatPeriodKey(periodKey: String)(implicit request: Request[_]) = {
    val year: String = "20" + periodKey.take(2)
    val month: String = periodKey.takeRight(1)

    val monthDes = month match {
      case "A" => LangMessages.period_1.show
      case "B" => LangMessages.period_1.show
      case "C" => LangMessages.period_1.show
      case "D" => LangMessages.period_2.show
      case "E" => LangMessages.period_2.show
      case "F" => LangMessages.period_2.show
      case "G" => LangMessages.period_3.show
      case "H" => LangMessages.period_3.show
      case "I" => LangMessages.period_3.show
      case "J" => LangMessages.period_4.show
      case "K" => LangMessages.period_4.show
      case "L" => LangMessages.period_4.show
      case _ => {
        Logger.warn(s"invalid periodKey, could not match month: ${periodKey}")
        ""
      }
    }

    val periodKeyDescription = s"""${monthDes} ${year}"""

    Logger.debug(s"""Received ${periodKey} returning ${periodKeyDescription}""")
    periodKeyDescription
  }

}
