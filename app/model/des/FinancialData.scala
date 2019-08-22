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

import java.time.LocalDate

import play.api.libs.json.{Json, OFormat, Reads}

/**
 * Case class representing a small subset of the DES 1166 API response
 * containing financial data for a VRN.
 */

final case class FinancialData(idType: Option[String], idNumber: Option[String], regimeType: Option[String], processingDate: Option[String], financialTransactions: Option[Seq[Transaction]])

object FinancialData {
  implicit val reads: Reads[FinancialData] = Json.reads[FinancialData]
  implicit val format: OFormat[FinancialData] = Json.format[FinancialData]
}

final case class Transaction(periodKey:            Option[String],
                             periodKeyDescription: Option[String],
                             taxPeriodFrom:        Option[LocalDate],
                             taxPeriodTo:          Option[LocalDate],
                             originalAmount:       Option[BigDecimal],
                             outstandingAmount:    Option[BigDecimal],
                             items:                Option[Seq[SubItem]])

object Transaction {

  implicit val reads: Reads[Transaction] = Json.reads[Transaction]
  implicit val format: OFormat[Transaction] = Json.format[Transaction]
}

final case class SubItem(subItem: Option[String], dueDate: Option[LocalDate], amount: Option[BigDecimal])

object SubItem {

  implicit val reads: Reads[SubItem] = Json.reads[SubItem]
  implicit val format: OFormat[SubItem] = Json.format[SubItem]
}
