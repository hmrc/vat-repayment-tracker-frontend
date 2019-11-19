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

import model.des.INITIAL
import model.{EnrolmentKeys, PeriodKey, Vrn}
import play.api.Logger
import play.api.http.Status
import support._

class ControllerSpec extends ItSpec {
  val vrn: Vrn = Vrn("2345678890")
  val periodKey = PeriodKey("18AG")

  "Get ShowResults authorised" in {
    AuditWireMockResponses.auditIsAvailable
    AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
    DesWireMockResponses.customerDataOkWithBankDetails(vrn)
    DesWireMockResponses.repaymentDetailS1(vrn, LocalDate.now().toString, INITIAL.value, periodKey)
    VatRepaymentTrackerBackendWireMockResponses.storeOk
    val result = connector.showResults(vrn).futureValue
    result.status shouldBe Status.OK
  }

  "Get ShowResults not authorised redirect to login stub" in {
    AuthWireMockResponses.authFailed
    AuditWireMockResponses.auditIsAvailable
    GgStub.signInPage(9863, vrn)
    val result = connector.showResults(vrn).futureValue
    result.body should include ("Sign in using Government Gateway")
    result.status shouldBe Status.OK
  }

  "Get view repayment account authorised" in {
    AuditWireMockResponses.auditIsAvailable
    AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
    DesWireMockResponses.customerDataOkWithBankDetails(vrn)
    val result = connector.viewRepaymentAccount(vrn).futureValue
    result.status shouldBe Status.OK
  }

  "Get startBankAccountCocJourney authorised" in {
    AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
    BankAccountCocWireMockResponses.bankOk
    val result = connector.startBankAccountCocJourney(vrn).futureValue
    result.status shouldBe Status.OK
  }

}
