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
import java.util.Date

import play.api.libs.json._

final case class VatObligations(obligations: Seq[VatObligation])

object VatObligations {
  implicit val format: OFormat[VatObligations] = Json.format[VatObligations]
}

final case class VatObligation(obligationDetails: Seq[ObligationDetail])

object VatObligation {
  implicit val format: OFormat[VatObligation] = Json.format[VatObligation]
}

final case class ObligationDetail(status:                            String,
                                  inboundCorrespondenceFromDate:     LocalDate,
                                  inboundCorrespondenceToDate:       LocalDate,
                                  inboundCorrespondenceDateReceived: LocalDate,
                                  inboundCorrespondenceDueDate:      LocalDate,
                                  periodKey:                         String)

object ObligationDetail {

  implicit val reads: Reads[ObligationDetail] = Json.reads[ObligationDetail]
  implicit val format: OFormat[ObligationDetail] = Json.format[ObligationDetail]

}

final case class ObligationDates(inboundCorrespondenceDateReceived: String, estimatedPaymentDate: String)
