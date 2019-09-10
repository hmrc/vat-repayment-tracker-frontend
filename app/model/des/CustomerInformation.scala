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

import model.Vrn
import play.api.libs.json._
import play.api.mvc.PathBindable
import controllers.ValueClassBinder.valueClassBinder
import play.api.libs.functional.syntax._

final case class CustomerInformation(approvedInformation: Option[ApprovedInformation]) {
  def unWrap(vrn: Vrn): ApprovedInformation = {
    approvedInformation match {
      case None       => throw new RuntimeException(s"""No Customer data for VRN: ${vrn}""")
      case Some(data) => data
    }
  }
}

object CustomerInformation {
  implicit val format: OFormat[CustomerInformation] = Json.format[CustomerInformation]
}

final case class ApprovedInformation(bankDetails: Option[BankDetails]) {

  val bankDetailsExist = bankDetails.isDefined
}
object ApprovedInformation {

  implicit val format: OFormat[ApprovedInformation] = Json.format[ApprovedInformation]
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

