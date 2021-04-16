/*
 * Copyright 2021 HM Revenue & Customs
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

package langswitch

import model.Vrn

object ErrorMessages {

  val `You do not have access to this service`: Message = Message(
    english = "You do not have access to this service",
    welsh   = "Nid oes gennych fynediad at y gwasanaeth hwn"
  )
  val `choose an option`: Message = Message(
    english = "Select whether to manage your accounts or track a VAT repayment",
    welsh   = "Dewiswch a ydych am reoli’ch cyfrifon neu olrhain ad-daliad TAW"
  )

  val `general error title`: Message = Message(
    english = "Sorry, there is a problem with the service",
    welsh   = "Mae’n ddrwg gennym – mae problem gyda’r gwasanaeth"
  )

  val `try again later`: Message = Message(
    english = "Try again later.",
    welsh   = "Rhowch gynnig arall arni yn nes ymlaen."
  )

  def `You do not have access to this service`(vrn: Vrn): Message = Message(
    english = s"""You do not have access to this service for VRN : ${vrn.value}""",
    welsh   = s"Nid oes gennych fynediad at y gwasanaeth hwn ar gyfer Rhif Cofrestru TAW: ${vrn.value}"
  )

}
