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

package controllers

import model.{EnrolmentKeys, Vrn}
import play.api.http.Status
import support.{AuthWireMockResponses, DDBackendWireMockResponses, DesWireMockResponses, ItSpec}

class ControllerSpec extends ItSpec {
  val vrn: Vrn = Vrn("2345678890")

  "Get ShowResults authorised" in {
    AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
    DesWireMockResponses.financialsOkSingle(vrn)
    DesWireMockResponses.customerDataOkWithBankDetails(vrn)
    DesWireMockResponses.obligationsOk(vrn, "2027-12-12", "2027-11-12")
    val result = connector.showResults(vrn).futureValue
    result.status shouldBe Status.OK
  }

  "Get view repayment account authorised" in {
    AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
    DesWireMockResponses.customerDataOkWithBankDetails(vrn)
    val result = connector.viewRepaymentAccount(vrn).futureValue
    result.status shouldBe Status.OK
  }

}
