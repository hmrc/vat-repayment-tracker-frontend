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

package pages

import org.openqa.selenium.By
import support.{ITSpec, WireMockResponses}

class NoVatRepaymentsFoundSpec extends ITSpec with CommonPage {

  val path = "/vat-repayment-tracker-frontend/show-results/vrn/234567890"

  "user is authorised and no financial data found" in {
    WireMockResponses.authOk(wireMockBaseUrlAsString = wireMockBaseUrlAsString)
    WireMockResponses.financialsNotFound
    goToViaPath(path)
    webDriver.getTitle shouldBe "Vat Repayment Tracker"
    probing(_.findElement(By.id("main-message")).getText) shouldBe "No VAT repayments in progress"
  }

}