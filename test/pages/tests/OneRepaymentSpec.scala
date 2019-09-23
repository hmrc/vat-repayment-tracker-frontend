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

import model.des.{AccountHolderName, BankAccountNumber, SortCode}
import model.{EnrolmentKeys, Vrn}
import pages.{OneRepayment, ViewRepaymentAccount}
import support.{ItSpec, WireMockResponses}

class OneRepaymentSpec extends ItSpec {

  val vrn = Vrn("234567890")
  val path = s"""/vat-repayment-tracker-frontend/show-results/vrn/${vrn.value}"""

  "user is authorised and financial data found" in {
    setup()
    OneRepayment.assertPageIsDisplayed(vrn)

  }

  "click manager link" in {
    setup()
    OneRepayment.clickManageAccount
    ViewRepaymentAccount.assertPageIsDisplayed(AccountHolderName("Account holder"), BankAccountNumber("****2222"), SortCode("667788"), vrn)
  }

  private def setup() = {
    WireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
    WireMockResponses.financialsOkSingle(vrn)
    WireMockResponses.customerDataOkWithBankDetails(vrn)
    WireMockResponses.obligationsOk(vrn)
    goToViaPath(path)
  }

}
