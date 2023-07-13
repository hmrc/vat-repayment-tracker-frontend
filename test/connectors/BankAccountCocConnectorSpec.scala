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

package connectors

import model.{ReturnPage, Vrn}
import support.{AuditWireMockResponses, BankAccountCocWireMockResponses, ItSpec}

class BankAccountCocConnectorSpec extends ItSpec {
  val vrn: Vrn = Vrn("2345678890")

  val bankAccountCocConnector: BankAccountCocConnector = injector.instanceOf[BankAccountCocConnector]

  "Start Journey" in {
    BankAccountCocWireMockResponses.bankOk
    AuditWireMockResponses.auditIsAvailable
    val result = bankAccountCocConnector.startJourney(vrn, ReturnPage("manage-or-track-vrt")).futureValue
    result.nextUrl shouldBe BankAccountCocWireMockResponses.dummyNextUrl
  }

}
