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
import play.api.mvc.PathBindable
import controllers.ValueClassBinder.valueClassBinder
import play.api.libs.functional.syntax._

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

  val bankDetailsExist = bankDetails.isDefined

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
    accountHolderName: AccountHolderName,
    bankAccountNumber: BankAccountNumber,
    sortCode:          SortCode
)
object BankDetails {
  implicit val format: OFormat[BankDetails] = Json.format[BankDetails]
}

final case class AccountHolderName(value: String)

object AccountHolderName {

  implicit val format: Format[AccountHolderName] = implicitly[Format[String]].inmap(AccountHolderName(_), _.value)
  implicit val vrnBinder: PathBindable[AccountHolderName] = valueClassBinder(_.value)

}

final case class BankAccountNumber(value: String)

object BankAccountNumber {

  implicit val format: Format[BankAccountNumber] = implicitly[Format[String]].inmap(BankAccountNumber(_), _.value)
  implicit val vrnBinder: PathBindable[BankAccountNumber] = valueClassBinder(_.value)

}

final case class SortCode(value: String)

object SortCode {

  implicit val format: Format[SortCode] = implicitly[Format[String]].inmap(SortCode(_), _.value)
  implicit val vrnBinder: PathBindable[SortCode] = valueClassBinder(_.value)

}

