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
import support.{AuditWireMockResponses, AuthWireMockResponses, ItSpec, PaymentsOrchestratorStub, VatRepaymentTrackerBackendWireMockResponses}
import play.api.http.Status
import play.api.test.Helpers._
import play.api.test.Helpers.status

import java.time.LocalDate

class ManageOrTrackControllerSpec extends ItSpec {
  val vrn: Vrn = Vrn("2345678890")
  val periodKey: PeriodKey = PeriodKey("18AG")

  val controller: ManageOrTrackController = injector.instanceOf[ManageOrTrackController]

  "GET /vat-repayment-tracker/manage-or-track-vrt" - {
    "authorised gets 'Manage or track VRT'" in {
      AuditWireMockResponses.auditIsAvailable
      AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
      PaymentsOrchestratorStub.customerDataOkWithBankDetails(vrn)
      PaymentsOrchestratorStub.repaymentDetailS1(vrn, LocalDate.now().toString, INITIAL, periodKey)
      VatRepaymentTrackerBackendWireMockResponses.storeOk()
      PaymentsOrchestratorStub.ddOk(vrn)
      val result = controller.manageOrTrackVrt(fakeRequest)
      status(result) shouldBe Status.OK
    }
  }
  "GET /manage-or-track/vrn/:vrn" - {
    "shows vrt for 'Manage or track' no-mtd" in {
      AuditWireMockResponses.auditIsAvailable
      AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.vatVarEnrolmentKey)
      PaymentsOrchestratorStub.customerDataOkWithBankDetails(vrn)
      PaymentsOrchestratorStub.repaymentDetailS1(vrn, LocalDate.now().toString, INITIAL, periodKey)
      VatRepaymentTrackerBackendWireMockResponses.storeOk()
      PaymentsOrchestratorStub.ddOk(vrn)
      val result = controller.manageOrTrack(vrn)(fakeRequest)
      status(result) shouldBe Status.SEE_OTHER
      redirectLocation(result) shouldBe Some("http://localhost:19001/vat-repayment-tracker/non-mtd-user")

    }
  }

}
