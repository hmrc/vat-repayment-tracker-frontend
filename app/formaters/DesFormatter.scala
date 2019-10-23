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
import model.{PeriodKey, VrtRepaymentDetailData}
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
    } yield (fdAllOption.financialTransactions.filter(f => (f.periodKey == periodKey && f.chargeType == "VAT Return Debit Charge")))

    transactionIterable match {
      case Some(x) => if (x.size > 0) true else false
      case None    => false
    }
  }

  def getReturnCreditChargeExists(financialData: Option[FinancialData], periodKey: PeriodKey): Boolean = {

    val transactionIterable = for {
      fdAllOption <- financialData
    } yield (fdAllOption.financialTransactions.filter(f => (f.periodKey == periodKey && f.chargeType == "VAT Return Credit Charge")))

    transactionIterable match {
      case Some(x) => if (x.size > 0) true else false
      case None    => false
    }
  }

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
    val quarter: String = periodKey.takeRight(2)

    val quarterDes = quarter match {
      case "AA" => LangMessages.period_AA.show
      case "AB" => LangMessages.period_AB.show
      case "AC" => LangMessages.period_AC.show
      case "AD" => LangMessages.period_AD.show
      case _    => throw new RuntimeException(s"Invalid periodkey : ${periodKey}")
    }

    val periodKeyDescription = s"""${quarterDes} ${year}"""

    Logger.debug(s"""Received ${periodKey} returning ${periodKeyDescription}""")
    periodKeyDescription
  }

}
