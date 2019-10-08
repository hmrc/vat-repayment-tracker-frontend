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

package model.des

import play.api.libs.json._

final case class CustomerInformation(approvedInformation: Option[ApprovedInformation]) {
  val approvedInformationExists = approvedInformation.isDefined
}

object CustomerInformation {
  implicit val format: OFormat[CustomerInformation] = Json.format[CustomerInformation]
}

final case class ApprovedInformation
  (
    bankDetails: Option[BankDetails],
    PPOB:        Option[PPOB]
) {

  val bankDetailsExist = bankDetails match {
    case Some(bde) => bde.detailsExist
    case None      => false
  }

  val addressExists = PPOB.isDefined

}

object ApprovedInformation {

  implicit val format: OFormat[ApprovedInformation] = Json.format[ApprovedInformation]
}

final case class PPOB(address: Option[Address])

object PPOB {
  implicit val format: OFormat[PPOB] = Json.format[PPOB]
}

final case class Address(
    line1:       Option[String],
    line2:       Option[String],
    line3:       Option[String],
    line4:       Option[String],
    postCode:    Option[String],
    countryCode: Option[String]
)

object Address {
  implicit val format: OFormat[Address] = Json.format[Address]
}

final case class BankDetails(
    accountHolderName: Option[String],
    bankAccountNumber: Option[String],
    sortCode:          Option[String]
) {

  val detailsExist: Boolean = {
    accountHolderName.isDefined || bankAccountNumber.isDefined || sortCode.isDefined
  }
  val formatAccountHolderName: String = {
    accountHolderName match {
      case Some(ahn) => ahn
      case None      => ""
    }
  }

  val obscureBankAccountNumber: String = {

    bankAccountNumber match {
      case Some(ban) => "****" + ban.drop(4)
      case None      => ""
    }

  }

  val formatSortCode: String = {

    sortCode match {
      case Some(sc) =>
        {}
        sc.length match {
          case 6   => s"${sc.substring(0, 2)} ${sc.substring(2, 4)} ${sc.substring(4, 6)}"
          case sor => throw new RuntimeException(s"Invalid sortcode length [$sor] for sortcode [$sortCode]")
        }
      case None => ""
    }

  }

}

object BankDetails {
  implicit val format: OFormat[BankDetails] = Json.format[BankDetails]
}
