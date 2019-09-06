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

package service.des

import model.Vrn
import model.des.ObligationDates
import support.{ITSpec, WireMockResponses}

class DesServiceSpec extends ITSpec {

  val desService = injector.instanceOf[DesService]
  "desService" - {

    "should find obligation detail for known VRN and Period  " in {

      WireMockResponses.obligationsOk(Vrn("968501689"))
      val futureResponse: ObligationDates = desService.getObligations(Vrn("968501689"), "18AA").futureValue

      futureResponse.inboundCorrespondenceDateReceived shouldBe "2018-04-15"
      futureResponse.estimatedPaymentDate shouldBe "2018-05-15"

    }

  }

  "obligations not found" in {

    WireMockResponses.obligationsNotFound
    val futureResponse = desService.getObligations(Vrn("968501689"), "18AA").futureValue
    futureResponse.inboundCorrespondenceDateReceived shouldBe ""
    futureResponse.estimatedPaymentDate shouldBe ""

  }

}
