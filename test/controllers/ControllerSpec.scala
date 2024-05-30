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
import model.EnrolmentKeys
import play.api.http.Status
import play.api.test.Helpers._
import play.api.test.Helpers.status
import support.{AuditWireMockResponses, AuthWireMockResponses, DeregisteredBehaviour, ItSpec, PaymentsOrchestratorStub, VatRepaymentTrackerBackendWireMockResponses, VatWireMockResponses}

import java.time.LocalDate

class ControllerSpec extends ItSpec with DeregisteredBehaviour {
  import support.VatData.{vrn, periodKey}

  val loginUrl: String = configMap("urls.login").toString

  val controller: Controller = injector.instanceOf[Controller]

  "GET /view-repayment-account" - {
    "authorised views repayment account" in {
      AuditWireMockResponses.auditIsAvailable
      AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
      PaymentsOrchestratorStub.customerDataOkWithBankDetails(vrn)
      val result = controller.viewRepaymentAccount(fakeRequest)
      status(result) shouldBe Status.OK
    }
    "deregistered redirected to 'vrt vat registration cancelled' page" in {
      assertDeregisteredRedirectedIn(controller.viewRepaymentAccount, vrn)
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
    "deregistered redirected to 'vrt vat registration cancelled' page" in {
      assertDeregisteredRedirectedIn(controller.showVrt, vrn)
    }

  }

  "GET /view-progress should" - {

    "redirect the user to /manage-or-track-vrt when no repayment data is found in the BE" in {
      AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString, vrn, EnrolmentKeys.mtdVatEnrolmentKey)
      PaymentsOrchestratorStub.customerDataOkWithBankDetails(vrn)
      PaymentsOrchestratorStub.repaymentDetailS1(vrn, LocalDate.now().toString, INITIAL, periodKey)
      VatRepaymentTrackerBackendWireMockResponses.noRepaymentDetails(vrn, periodKey)

      val result = controller.viewProgress(periodKey)(fakeRequest)
      status(result) shouldBe SEE_OTHER
      redirectLocation(result) shouldBe Some(routes.ManageOrTrackController.manageOrTrackVrt.url)

    }

  }

}
