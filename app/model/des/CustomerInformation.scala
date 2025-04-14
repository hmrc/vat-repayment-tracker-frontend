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

package model.des

import play.api.libs.json._

import java.time.LocalDate

final case class CustomerInformation(approvedInformation: Option[ApprovedInformation], inFlightInformation: Option[InFlightInformation]) {
  val approvedInformationExists: Boolean = approvedInformation.isDefined
  val inFlightInformationExists: Boolean = inFlightInformation.isDefined

  val bankDetailsChangeIndicatorExists: Option[Boolean] = {
    for {
      inflight <- inFlightInformation
      changeIndicators <- inflight.changeIndicators
      bankDetails <- changeIndicators.bankDetails
    } yield bankDetails

  }

  val PPOBDetailsChangeIndicatorExists: Option[Boolean] = {
    for {
      inflight <- inFlightInformation
      changeIndicators <- inflight.changeIndicators
      pPOBDetails <- changeIndicators.PPOBDetails
    } yield pPOBDetails
  }

  val isPartiallyMigrated: Boolean = {
    for {
      approved <- approvedInformation
      cd <- approved.customerDetails
      isPartialMigration <- cd.isPartialMigration
    } yield isPartialMigration
  }.getOrElse(false)

  val isDeregistered: Boolean = {
    for {
      approved <- approvedInformation
      deregistration <- approved.deregistration
    } yield deregistration.deregistrationReason.nonEmpty
  }.getOrElse(false)

  val inFlightDate: Option[String] = {
    for {
      inflight <- inFlightInformation
      inFlightChanges <- inflight.inFlightChanges
      bankDetails <- inFlightChanges.bankDetails
      formInformation <- bankDetails.formInformation
      dateReceived <- formInformation.dateReceived
    } yield dateReceived
  }

}

object CustomerInformation {
  implicit val format: OFormat[CustomerInformation] = Json.format[CustomerInformation]
}

final case class InFlightInformation(
    changeIndicators: Option[ChangeIndicators],
    inFlightChanges:  Option[InFlightChanges]
)

object InFlightInformation {
  implicit val format: OFormat[InFlightInformation] = Json.format[InFlightInformation]
}

final case class ChangeIndicators(bankDetails: Option[Boolean], PPOBDetails: Option[Boolean])

object ChangeIndicators {
  implicit val format: OFormat[ChangeIndicators] = Json.format[ChangeIndicators]
}

final case class InFlightChanges(bankDetails: Option[BankDetails])

object InFlightChanges {
  implicit val format: OFormat[InFlightChanges] = Json.format[InFlightChanges]
}

final case class FormInformation(dateReceived: Option[String])

object FormInformation {
  implicit val format: OFormat[FormInformation] = Json.format[FormInformation]
}

final case class ApprovedInformation
  (
    customerDetails: Option[CustomerDetails],
    bankDetails:     Option[BankDetails],
    PPOB:            Option[PPOB],
    deregistration:  Option[Deregistration]  = None
) {

  val bankDetailsExist: Boolean = bankDetails match {
    case Some(bde) => bde.detailsExist
    case None      => false
  }

  val addressExists: Boolean = PPOB.isDefined

}

final case class CustomerDetails(
    welshIndicator:     Option[Boolean],
    isPartialMigration: Option[Boolean]
)

object CustomerDetails {

  implicit val format: OFormat[CustomerDetails] = Json.format[CustomerDetails]

}

object ApprovedInformation {

  implicit val format: OFormat[ApprovedInformation] = Json.format[ApprovedInformation]
}

final case class PPOB(address: Option[Address])

object PPOB {
  implicit val format: OFormat[PPOB] = Json.format[PPOB]
}

final case class Deregistration(
    deregistrationReason:     Option[String],
    effectDateOfCancellation: Option[LocalDate],
    lastReturnDueDate:        Option[LocalDate]
)

object Deregistration {
  implicit val format: OFormat[Deregistration] = Json.format[Deregistration]
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
    sortCode:          Option[String],
    formInformation:   Option[FormInformation]
) {

  val detailsExist: Boolean =
    accountHolderName.isDefined || bankAccountNumber.isDefined || sortCode.isDefined

  val formatAccountHolderName: String = accountHolderName.getOrElse("")

  val obscureBankAccountNumber: String = bankAccountNumber match {
    case Some(ban) => "****" + ban.drop(4)
    case None      => ""
  }

  val formatSortCode: String = sortCode match {
    case Some(sc) =>
      sc.length match {
        case 6   => s"${sc.substring(0, 2)} ${sc.substring(2, 4)} ${sc.substring(4, 6)}"
        case sor => throw new RuntimeException(s"Invalid sortcode length [$sor] for sortcode [$sortCode]")
      }
    case None => ""
  }

}

object BankDetails {
  implicit val format: OFormat[BankDetails] = Json.format[BankDetails]
}
