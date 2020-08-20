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

import javax.inject.{Inject, Singleton}
import model.des._
import model.{ChargeType, PeriodKey, VrtRepaymentDetailData}

@Singleton
class DesFormatter @Inject() (addressFormater: AddressFormatter) {

  def addMissingStatus(vrd: List[VrtRepaymentDetailData]): List[VrtRepaymentDetailData] = {

    if (vrd.exists(f => f.repaymentDetailsData.riskingStatus == INITIAL.value)) {
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
    } yield fdAllOption.financialTransactions.filter(f => financialTransactionsPredicate(f, ChargeType.vatReturnDebitCharge, periodKey))

    transactionIterable match {
      case Some(x) => if (x.nonEmpty) true else false
      case None    => false
    }
  }

  def getReturnCreditChargeExists(financialData: Option[FinancialData], periodKey: PeriodKey): Boolean = {

    val transactionIterable = for {
      fdAllOption <- financialData
    } yield fdAllOption.financialTransactions.filter(f => financialTransactionsPredicate(f, ChargeType.vatReturnCreditCharge, periodKey))

    transactionIterable match {
      case Some(x) => if (x.nonEmpty) true else false
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
    } yield bd
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
    } yield ai.addressExists

    maybeExists.getOrElse(false)

  }

  def getDDData(directDebitData: Option[DirectDebitData]): Option[BankDetails] = {

    val ddDetail: Option[DirectDebitDetails] = for {
      dd <- directDebitData
      ddDetailsOption <- dd.directDebitDetails
      ddDetails <- ddDetailsOption.headOption
    } yield ddDetails

    ddDetail.map(detail => BankDetails(Some(detail.accountHolderName), Some(detail.accountNumber), Some(detail.sortCode)))

  }

  def getAddressDetails(customerData: Option[CustomerInformation]): Option[String] = {
    for {
      cd <- customerData
      ai <- cd.approvedInformation
      ppob <- ai.PPOB
      ad <- ppob.address
    } yield addressFormater.getFormattedAddressMtd(ad)

  }

  def bankDetailsInFlight(customerData: Option[CustomerInformation]): Boolean = {
    val optBankDetails = for {
      cd <- customerData
      bankDetails <- cd.bankDetailsChangeIndicatorExists
    } yield bankDetails

    optBankDetails match {
      case Some(x) => x
      case None    => false
    }
  }

}
