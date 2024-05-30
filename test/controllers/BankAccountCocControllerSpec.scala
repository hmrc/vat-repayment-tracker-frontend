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

package controllers

import model.{EnrolmentKeys, ReturnPage}
import play.api.http.Status
import play.api.test.Helpers._
import support._

class BankAccountCocControllerSpec extends ItSpec {
  import support.VatData.vrn

  val controller: BankAccountCocController = injector.instanceOf[BankAccountCocController]

  "GET /bank-account-coc/start-journey/manage-or-track" - {
    "authorised starts bank account change of circumstances journey" in {
      AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
      BankAccountCocWireMockResponses.bankOk
      PaymentsOrchestratorStub.customerDataOkWithBankDetails(vrn)
      AuditWireMockResponses.auditIsAvailable
      val result = controller.startBankAccountCocJourney(returnPage = ReturnPage("manage-or-track"))(fakeRequest)
      status(result) shouldBe Status.SEE_OTHER
      redirectLocation(result) shouldBe Some(BankAccountCocWireMockResponses.dummyNextUrl)

    }
  }

}
