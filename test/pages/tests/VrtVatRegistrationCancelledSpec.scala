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

import model.Language.Welsh
import model.{EnrolmentKeys, Vrn}
import org.scalatest.Assertion
import pages.VrtVatRegistrationCancelledPage
import support.{AuditWireMockResponses, AuthWireMockResponses, BrowserSpec, PaymentsOrchestratorStub}

class VrtVatRegistrationCancelledSpec extends BrowserSpec {

  val vrn: Vrn = Vrn("234567890")

  "When deregistered for VAT redirect to 'VRT VAT Registration Cancelled' page from" - {
    "GET /vat-repayment-tracker/view-repayment-account" in {
      testSetup("/vat-repayment-tracker/view-repayment-account")
      testPage()
    }
    "GET /vat-repayment-tracker/manage-or-track-vrt" in {
      testSetup("/vat-repayment-tracker/manage-or-track-vrt")
      testPage()
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

  private def testPage(): Assertion = {
    VrtVatRegistrationCancelledPage.clickOnWelshLink()
    VrtVatRegistrationCancelledPage.assertPageIsDisplayed(Welsh)
    VrtVatRegistrationCancelledPage.assertHyperLinkedTextDisplayed(Welsh)
    VrtVatRegistrationCancelledPage.clickOnEnglishLink()
    VrtVatRegistrationCancelledPage.assertPageIsDisplayed()
    VrtVatRegistrationCancelledPage.assertHyperLinkedTextDisplayed()
    VrtVatRegistrationCancelledPage.assertBackButtonRedirectsTo(viewConfig.viewVatAccount)
  }

}
