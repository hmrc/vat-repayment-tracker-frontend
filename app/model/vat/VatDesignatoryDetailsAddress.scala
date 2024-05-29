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

package model.vat

import play.api.libs.json._
import play.api.libs.functional.syntax._

final case class VatDesignatoryDetailsAddress(addressLine1: String,
                                              addressLine2: Option[String] = None,
                                              addressLine3: Option[String] = None,
                                              addressLine4: Option[String] = None,
                                              addressLine5: Option[String] = None,
                                              postcode:     String)

object VatDesignatoryDetailsAddress {

  implicit val reads: Reads[VatDesignatoryDetailsAddress] = (
    (JsPath \ "address" \ "addressLine1").read[String] and
    (JsPath \ "address" \ "addressLine2").readNullable[String] and
    (JsPath \ "address" \ "addressLine3").readNullable[String] and
    (JsPath \ "address" \ "addressLine4").readNullable[String] and
    (JsPath \ "address" \ "addressLine5").readNullable[String] and
    (JsPath \ "address" \ "postcode").read[String]
  ) (VatDesignatoryDetailsAddress.apply _)

}
