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
import pages.NoVatRepaymentsFoundPage
import support.{AuditWireMockResponses, AuthWireMockResponses, BrowserSpec, PaymentsOrchestratorStub}

class NoVatRepaymentsFoundSpec extends BrowserSpec {

  val vrn: Vrn = Vrn("234567890")
  val path = s"""/vat-repayment-tracker/show-vrt"""

  "1. user is authorised and no financial data found" in {
    AuditWireMockResponses.auditIsAvailable
    AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
    PaymentsOrchestratorStub.financialsNotFound(vrn)
    PaymentsOrchestratorStub.customerDataOkWithBankDetails(vrn)
    PaymentsOrchestratorStub.repaymentDetailsNotFound(vrn)
    login()
    goToViaPath(path)
    NoVatRepaymentsFoundPage.assertPageIsDisplayed()

    AuditWireMockResponses.engagementStatusAudited("showVrt", Map("vrn" -> vrn.value, "engmtType" -> "none_in_progress"))

  }

}
