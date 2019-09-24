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
import pages.{OneRepayment, ViewRepaymentAccount}
import support.{ItSpec, WireMockResponses}

class OneRepaymentSpec extends ItSpec {

  val vrn = Vrn("234567890")
  val path = s"""/vat-repayment-tracker-frontend/show-results/vrn/${vrn.value}"""

  "user is authorised and financial data found - to date" in {
    setup("2027-12-12", "2027-11-12")
    OneRepayment.assertPageIsDisplayed(vrn, "12 Nov 2027", "11 Jan 2028")
  }

  "user is authorised and financial data found - received date" in {
    setup("2027-11-12", "2027-12-12")
    OneRepayment.assertPageIsDisplayed(vrn, "12 Dec 2027", "11 Jan 2028")
  }

  "user is authorised and financial data found - same date" in {
    setup("2027-12-12", "2027-12-12")
    OneRepayment.assertPageIsDisplayed(vrn, "12 Dec 2027", "11 Jan 2028")
  }

  "click manager link" in {
    setup("2027-12-12", "2027-11-12")
    OneRepayment.clickManageAccount
    ViewRepaymentAccount.assertPageIsDisplayed("Account holder", "****2222", "667788", vrn)
  }

  private def setup(toDate: String, receivedDate: String) = {
    WireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
    WireMockResponses.financialsOkSingle(vrn)
    WireMockResponses.customerDataOkWithBankDetails(vrn)
    WireMockResponses.obligationsOk(vrn, toDate, receivedDate)
    goToViaPath(path)
  }

}
