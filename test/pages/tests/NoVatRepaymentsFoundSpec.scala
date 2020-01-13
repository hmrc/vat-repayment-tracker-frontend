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

package pages.tests

import model.{EnrolmentKeys, Vrn}
import pages.NoVatRepaymentsFoundPage
import support.{AuditWireMockResponses, AuthWireMockResponses, DesWireMockResponses, ItSpec}

class NoVatRepaymentsFoundSpec extends ItSpec {

  val vrn = Vrn("234567890")
  val path = s"""/vat-repayment-tracker/show-vrt"""

  "user is authorised and no financial data found" in {
    AuditWireMockResponses.auditIsAvailable
    AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
    DesWireMockResponses.financialsNotFound(vrn)
    DesWireMockResponses.customerDataOkWithBankDetails(vrn)
    goToViaPath(path)
    NoVatRepaymentsFoundPage.assertPageIsDisplayed(vrn)

  }

}
