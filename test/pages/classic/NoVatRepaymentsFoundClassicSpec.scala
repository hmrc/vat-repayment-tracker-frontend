/*
 * Copyright 2020 HM Revenue & Customs
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

package pages.classic

import model.{EnrolmentKeys, Vrn}
import support._

class NoVatRepaymentsFoundClassicSpec extends ItSpec {

  val vrn = Vrn("234567890")
  val path = s"""/vat-repayment-tracker/show-vrt"""

  //Update to matcb classic changes later
  "1. Get ShowResults authorised but non-mtd vrn should show no results page" in {
    AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.vatVarEnrolmentKey)
    setup
    NoVatRepaymentsFoundClassicPage.assertPageIsDisplayed(vrn)
  }

  "2. Get ShowResults authorised but partially migrated vrn should show no results page" in {
    AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
    DesWireMockResponses.customerDataOkWithBankDetails(vrn, true)
    setup
    NoVatRepaymentsFoundClassicPage.assertPageIsDisplayed(vrn)
  }

  private def setup() = {

    AuditWireMockResponses.auditIsAvailable
    goToViaPath(path)
  }
}
