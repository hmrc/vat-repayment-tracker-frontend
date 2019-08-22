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

import play.api.libs.json.{JsResult, JsValue, Reads}

/**
 * Case class representing a small subset of the DES 1363 API response
 * containing customer data for a VRN.  Currently, we only extract the Welsh Indicator,
 * but more data can be added in future
 */
final case class CustomerInformation(welshIndicator: Option[WelshIndicator])

object CustomerInformation {

  val empty: CustomerInformation = CustomerInformation(None)

  implicit val reads: Reads[CustomerInformation] = new Reads[CustomerInformation] {
    override def reads(json: JsValue): JsResult[CustomerInformation] = {
      for {
        welshIndicator <- (json \ "approvedInformation" \ "customerDetails" \ "welshIndicator").validateOpt[WelshIndicator]
      } yield {
        CustomerInformation(welshIndicator)
      }
    }
  }
}

