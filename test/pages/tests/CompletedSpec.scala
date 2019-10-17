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

import model.{EnrolmentKeys, Vrn}
import pages._
import support.{AuthWireMockResponses, DesWireMockResponses, ItSpec}

class CompletedSpec extends ItSpec {

  val vrn = Vrn("234567890")
  val path = s"""/vat-repayment-tracker-frontend/show-results/vrn/${vrn.value}"""

  "user is authorised and financial data found" in {
    setup()
    Completed.assertPageIsDisplayed(vrn, amount = "£5.56", appender = "_completed")
    Completed.uniqueToPage
    Completed.checktabs
  }

  "user is authorised and financial data found but partial" in {
    setup(true, true)
    Completed.assertPageIsDisplayed(vrn, amount = "£5.56", partialAccount = true, appender = "_completed")
    Completed.uniqueToPage
  }

  "click in completed link" in {
    setup()
    Completed.viewProgressLink
  }

  "multiple completed " in {
    setup(true, true, false)
    Completed.uniqueToPage
    Completed.viewProgressLink
  }

  private def setup(useBankDetails: Boolean = true, partialBankDetails: Boolean = false, singleRepayment: Boolean = true) = {
    AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
    if (singleRepayment)
      DesWireMockResponses.financialsOkSingle(vrn)
    else
      DesWireMockResponses.financialsOkMultiple4(vrn)
    if (useBankDetails) {
      if (partialBankDetails)
        DesWireMockResponses.customerDataOkWithPartialBankDetails(vrn)
      else
        DesWireMockResponses.customerDataOkWithBankDetails(vrn)
    } else {
      DesWireMockResponses.customerDataOkWithoutBankDetails(vrn)
    }
    if (singleRepayment)
      DesWireMockResponses.repaymentDetailSingleCompleted(vrn)
    else
      DesWireMockResponses.repaymentDetailsMultipleCompleted(vrn)
    goToViaPath(path)
  }

}
