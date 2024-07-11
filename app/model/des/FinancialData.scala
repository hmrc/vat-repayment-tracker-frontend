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

import play.api.libs.json.{Json, OFormat}

import java.time.LocalDate

/**
 * Case class representing a small subset of the DES 1166 API response
 * containing financial data for a VRN.
 */

final case class FinancialData(
    idType:                String,
    idNumber:              String,
    regimeType:            String,
    processingDate:        String,
    financialTransactions: Seq[Transaction])

object FinancialData {
  implicit val format: OFormat[FinancialData] = Json.format[FinancialData]
}

final case class Transaction(
    chargeType: String,
    periodKey:  Option[String],
    items:      Option[Seq[Item]])

object Transaction {
  implicit val format: OFormat[Transaction] = Json.format[Transaction]
}

final case class Item(clearingDate: Option[LocalDate])

@SuppressWarnings(Array("org.wartremover.warts.Any"))
object Item {
  implicit val format: OFormat[Item] = Json.format[Item]
}
