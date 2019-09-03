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

final case class CustomerInformation(approvedInformation: Option[ApprovedInformation])

object CustomerInformation {
  implicit val reads: Reads[CustomerInformation] = Json.reads[CustomerInformation]
  implicit val format: OFormat[CustomerInformation] = Json.format[CustomerInformation]
}

final case class ApprovedInformation(PPOB: Option[PPOB], bankDetails: Option[BankDetails])
object ApprovedInformation {

  implicit val reads: Reads[ApprovedInformation] = Json.reads[ApprovedInformation]
  implicit val format: OFormat[ApprovedInformation] = Json.format[ApprovedInformation]
}

final case class PPOB(address: Address)
object PPOB {
  implicit val reads: Reads[PPOB] = Json.reads[PPOB]
  implicit val format: OFormat[PPOB] = Json.format[PPOB]
}

final case class Address(line1: String, line2: String, line3: String, line4: String, postCode: String, countryCode: String)
object Address {
  implicit val reads: Reads[Address] = Json.reads[Address]
  implicit val format: OFormat[Address] = Json.format[Address]
}

final case class BankDetails(accountHolderName: String, bankAccountNumber: String, sortCode: String)
object BankDetails {
  implicit val reads: Reads[BankDetails] = Json.reads[BankDetails]
  implicit val format: OFormat[BankDetails] = Json.format[BankDetails]
}
