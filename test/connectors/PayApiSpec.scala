/*
 * Copyright 2023 HM Revenue & Customs
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

package connectors

import model.{EnrolmentKeys, Vrn}
import support.{AuditWireMockResponses, AuthWireMockResponses, ItSpec, PayApiWireMockResponses}

class PayApiSpec extends ItSpec {

  val vrn: Vrn = Vrn("2345678890")

  val payApiConnector: PayApiConnector = injector.instanceOf[PayApiConnector]

  "check call to pay-api" in {
    AuditWireMockResponses.auditIsAvailable
    AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
    PayApiWireMockResponses.payOk
    val response = payApiConnector.startJourney(10000, vrn).futureValue
    response.nextUrl shouldBe "https://www.tax.service.gov.uk/pay/TestJourneyId-44f9-ad7f-01e1d3d8f151/start"
  }
}
