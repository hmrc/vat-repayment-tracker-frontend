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

import model.Vrn
import pages.NoVatRepaymentsFoundPage
import support.{ItSpec, WireMockResponses}

class NoVatRepaymentsFoundSpec extends ItSpec {

  val vrn = Vrn("234567890")
  val path = s"""/vat-repayment-tracker-frontend/show-results/vrn/${vrn.value}"""

  "user is authorised and no financial data found" in {
    WireMockResponses.authOk(wireMockBaseUrlAsString = wireMockBaseUrlAsString)
    WireMockResponses.financialsNotFound(vrn)
    WireMockResponses.customerDataOkWithBankDetails(vrn)
    WireMockResponses.obligationsOk(vrn)
    goToViaPath(path)
    NoVatRepaymentsFoundPage.assertPageIsDisplayed(vrn)

  }

}
