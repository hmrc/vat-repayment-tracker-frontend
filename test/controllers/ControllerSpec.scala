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

package controllers

import model.des.RiskingStatus.INITIAL
import model.{EnrolmentKeys, PeriodKey, Vrn}
import play.api.http.Status
import play.api.test.Helpers._
import play.api.test.Helpers.status
import support.{AuditWireMockResponses, AuthWireMockResponses, GgStub, ItSpec, PaymentsOrchestratorStub, VatRepaymentTrackerBackendWireMockResponses, VatWireMockResponses}

import java.time.LocalDate

class ControllerSpec extends ItSpec {
  val vrn: Vrn = Vrn("2345678890")
  val periodKey: PeriodKey = PeriodKey("18AG")

  val loginUrl: String = configMap("urls.login").toString

  val controller: Controller = injector.instanceOf[Controller]

  "GET /show-results/vrn/:vrn" - {
    "authorised shows results" in {
      AuditWireMockResponses.auditIsAvailable
      AuthWireMockResponses.authOkWithEnrolments(
        wireMockBaseUrlAsString = wireMockBaseUrlAsString,
        vrn                     = vrn,
        enrolment               = EnrolmentKeys.mtdVatEnrolmentKey
      )
      PaymentsOrchestratorStub.customerDataOkWithBankDetails(vrn)
      PaymentsOrchestratorStub.repaymentDetailS1(vrn, LocalDate.now().toString, INITIAL, periodKey)
      PaymentsOrchestratorStub.financialsOkCredit(vrn)
      VatRepaymentTrackerBackendWireMockResponses.storeOk()
      val result = controller.showResults(vrn)(fakeRequest)
      status(result) shouldBe Status.OK
    }
    "not authorised redirects to login" in {
      AuthWireMockResponses.authFailed
      AuditWireMockResponses.auditIsAvailable
      GgStub.signInPage(19001, vrn)
      val result = controller.showResults(vrn)(fakeRequest)
      status(result) shouldBe Status.SEE_OTHER
      redirectLocation(result).getOrElse("None found") should include(loginUrl)
    }
  }
  "GET /view-repayment-account" - {
    "authorised views repayment account" in {
      AuditWireMockResponses.auditIsAvailable
      AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
      PaymentsOrchestratorStub.customerDataOkWithBankDetails(vrn)
      val result = controller.viewRepaymentAccount(audit = true)(fakeRequest)
      status(result) shouldBe Status.OK
    }

  }
  "GET /show-vrt" - {
    "authorised show vrt" in {
      AuditWireMockResponses.auditIsAvailable
      AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
      PaymentsOrchestratorStub.customerDataOkWithBankDetails(vrn)
      PaymentsOrchestratorStub.repaymentDetailS1(vrn, LocalDate.now().toString, INITIAL, periodKey)
      VatRepaymentTrackerBackendWireMockResponses.storeOk()
      PaymentsOrchestratorStub.financialsOkCredit(vrn)
      val result = controller.showVrt(fakeRequest)
      status(result) shouldBe Status.OK
    }
    "authorised no-mtd, no vat repayments" in {
      AuditWireMockResponses.auditIsAvailable
      AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.vatVarEnrolmentKey)
      PaymentsOrchestratorStub.customerDataOkWithBankDetails(vrn)
      PaymentsOrchestratorStub.repaymentDetailS1(vrn, LocalDate.now().toString, INITIAL, periodKey)
      VatRepaymentTrackerBackendWireMockResponses.storeOk()
      VatWireMockResponses.calendar404(vrn)
      VatWireMockResponses.designatoryDetailsOk(vrn)
      val result = controller.showVrt(fakeRequest)
      contentAsString(result) should include("No VAT repayments in progress")
      status(result) shouldBe Status.OK
    }

  }

}
