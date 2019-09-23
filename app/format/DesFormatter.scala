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

package format

import javax.inject.{Inject, Singleton}
import model.des.{BankDetails, CustomerInformation, DirectDebitData, DirectDebitDetails}

@Singleton
class DesFormatter @Inject() (addressFormater: AddressFormter) {

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

    ddDetail match {
      case Some(detail) => Some(BankDetails(detail.accountHolderName, detail.accountNumber, detail.sortCode))
      case None         => None
    }

  }

  def getAddressDetails(customerData: Option[CustomerInformation]): Option[String] = {
    for {
      cd <- customerData
      ai <- cd.approvedInformation
      ppob <- ai.PPOB
      ad <- ppob.address
    } yield addressFormater.getFormattedAddress(ad)

  }

}
