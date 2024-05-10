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
import pages.classic.VatRepaymentsClassic
import support.{AuditWireMockResponses, AuthWireMockResponses, BrowserSpec, VatWireMockResponses}

class VatRepaymentsClassicSpec extends BrowserSpec {

  val vrn: Vrn = Vrn("234567890")
  val path = s"""/vat-repayment-tracker/show-vrt"""

  "1. user is authorised and vat repayments found (past period)" in {
    AuditWireMockResponses.auditIsAvailable
    AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.vatDecEnrolmentKey)
    VatWireMockResponses.calendarOk(vrn, isCurrent = false)
    VatWireMockResponses.designatoryDetailsOk(vrn)
    login()
    goToViaPath(path)
    VatRepaymentsClassic.assertPageIsDisplayed()
    VatRepaymentsClassic.readAddress shouldBe "1 Johnson Close\nStonesfield\nOxford\nOX29 8PP"
    VatRepaymentsClassic.readReceivedOnDate shouldBe "05 Apr 2016"

    AuditWireMockResponses.engagementStatusAudited("showVrt", Map("vrn" -> vrn.value, "engmtType" -> "in_progress_classic"))
  }

  "2. user is authorised and vat repayments found (current period)" in {
    AuditWireMockResponses.auditIsAvailable
    AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.vatDecEnrolmentKey)
    VatWireMockResponses.calendarOk(vrn)
    VatWireMockResponses.designatoryDetailsOk(vrn)
    login()
    goToViaPath(path)
    VatRepaymentsClassic.readReceivedOnDate shouldBe "05 Jul 2016"

    AuditWireMockResponses.engagementStatusAudited("showVrt", Map("vrn" -> vrn.value, "engmtType" -> "in_progress_classic"))
  }

  "3. user is authorised and vat repayments found (current period), 404 when getting address data" in {
    AuditWireMockResponses.auditIsAvailable
    AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.vatDecEnrolmentKey)
    VatWireMockResponses.calendarOk(vrn)
    VatWireMockResponses.designatoryDetails404(vrn)
    login()
    goToViaPath(path)
    VatRepaymentsClassic.afterAddress404AssertPageIsDisplayed()

  }

}
