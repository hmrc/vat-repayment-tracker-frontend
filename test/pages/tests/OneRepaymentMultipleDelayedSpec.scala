/*
 * Copyright 2019 HM Revenue & Customs
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

/*
 * Copyright 2019 HM Revenue & Customs
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
import pages.OneRepaymentMultipleDelayed
import support.{AuthWireMockResponses, DesWireMockResponses, ItSpec}

class OneRepaymentMultipleDelayedSpec extends ItSpec {

  val vrn = Vrn("234567890")
  val path = s"""/vat-repayment-tracker-frontend/show-results/vrn/${vrn.value}"""

  // def frozenTimeString: String = "2027-11-02T16:33:51.880"

  "user authenticated, data found " in {
    setup("2027-10-01", "2027-12-12", "2027-10-01", "2027-12-12")
    OneRepaymentMultipleDelayed.assertPageIsDisplayed(vrn)
  }

  private def setup(delayedDate: String, currentDate: String, delayedToDate: String, currentToDate: String) = {
    AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
    DesWireMockResponses.financialsOkMultiple4(vrn)
    DesWireMockResponses.customerDataOkWithBankDetails(vrn)
    DesWireMockResponses.obligationsDataOkMultipleMix(vrn, delayedDate, currentDate, delayedToDate, currentToDate)
    goToViaPath(path)
  }

}
