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

import java.time.LocalDate

import model.{EnrolmentKeys, Vrn}
import model.des.{AccountHolderName, BankAccountNumber, BankDetails, SortCode}
import play.api.http.Status
import support.{ItSpec, WireMockResponses}

class ControllerSpec extends ItSpec {
  val vrn: Vrn = Vrn("2345678890")

  "Get ShowResults authorised" in {
    WireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
    WireMockResponses.financialsOkSingle(vrn)
    WireMockResponses.customerDataOkWithBankDetails(vrn)
    WireMockResponses.obligationsOk(vrn)
    val result = connector.showResults(vrn).futureValue
    result.status shouldBe Status.OK
  }

  "Get view repayment account authorised" in {
    WireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
    val result = connector.viewRepaymentAccount(AccountHolderName("*********"), BankAccountNumber("****2490"), SortCode("40****"), vrn).futureValue
    result.status shouldBe Status.OK
  }

}
