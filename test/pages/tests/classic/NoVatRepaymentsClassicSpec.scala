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

package pages.tests.classic

import model.{EnrolmentKeys, Vrn}
import pages.classic.NoVatRepaymentsClassic
import support.{AuditWireMockResponses, AuthWireMockResponses, BrowserSpec, VatWireMockResponses}

class NoVatRepaymentsClassicSpec extends BrowserSpec {

  val vrn: Vrn = Vrn("234567890")
  val path = s"""/vat-repayment-tracker/show-vrt"""

  "1. user is authorised and no vat repayments found" in {
    AuditWireMockResponses.auditIsAvailable
    AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.vatDecEnrolmentKey)
    VatWireMockResponses.calendar404(vrn)
    VatWireMockResponses.designatoryDetailsOk(vrn)
    login()
    goToViaPath(path)
    NoVatRepaymentsClassic.assertPageIsDisplayed()
    NoVatRepaymentsClassic.readAddress shouldBe "1 Johnson Close\nStonesfield\nOxford\nOX29 8PP"

    AuditWireMockResponses.engagementStatusAudited("showVrt", Map("vrn" -> vrn.value, "engmtType" -> "none_in_progress"))

  }

}
