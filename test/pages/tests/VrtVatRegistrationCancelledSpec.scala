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

package pages.tests

import model.{EnrolmentKeys, Vrn}
import pages.VrtVatRegistrationCancelledPage
import support.{AuditWireMockResponses, AuthWireMockResponses, BrowserSpec, PaymentsOrchestratorStub}

class VrtVatRegistrationCancelledSpec extends BrowserSpec {

  val vrn: Vrn = Vrn("234567890")

  "When deregistered for VAT redirect to 'VRT VAT Registration Cancelled' page from" - {
    "GET /vat-repayment-tracker-frontend/show-results/vrn/:vrn" in {
      testSetup(s"/vat-repayment-tracker-frontend/show-results/vrn/$vrn")
      VrtVatRegistrationCancelledPage.assertPageIsDisplayed
    }
    "GET /vat-repayment-tracker-frontend/manage-or-track/vrn/:vrn" in {
      testSetup(s"/vat-repayment-tracker-frontend/manage-or-track/vrn/$vrn")
      VrtVatRegistrationCancelledPage.assertPageIsDisplayed
    }
    "GET /vat-repayment-tracker/view-repayment-account" in {
      testSetup("/vat-repayment-tracker/view-repayment-account")
      VrtVatRegistrationCancelledPage.assertPageIsDisplayed
    }
    "GET /vat-repayment-tracker/manage-or-track-vrt" in {
      testSetup("/vat-repayment-tracker/manage-or-track-vrt")
      VrtVatRegistrationCancelledPage.assertPageIsDisplayed
    }

  }

  private def testSetup(path: String): Unit = {
    AuditWireMockResponses.auditIsAvailable
    AuthWireMockResponses.authOkWithEnrolments(
      wireMockBaseUrlAsString = wireMockBaseUrlAsString,
      vrn                     = vrn,
      enrolment               = EnrolmentKeys.mtdVatEnrolmentKey
    )
    PaymentsOrchestratorStub.customerDataOkDeregistered(vrn)
    login()
    goToViaPath(path)
  }

}
