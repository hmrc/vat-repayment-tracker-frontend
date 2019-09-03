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

import support.{ITSpec, WireMockResponses}

class ErrorPageSpec extends ITSpec {

  val path = "/vat-repayment-tracker-frontend/show-results/vrn/2345678900"

  "user is not authorised" in {
    WireMockResponses.authFailed
    goToViaPath(path)
    webDriver.getTitle shouldBe "You do not have access to this service"
  }
}