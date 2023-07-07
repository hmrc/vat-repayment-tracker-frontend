package controllers

import model.{EnrolmentKeys, ReturnPage, Vrn}
import play.api.http.Status
import play.api.test.Helpers._
import play.api.test.Helpers.status
import support.{AuditWireMockResponses, AuthWireMockResponses, BankAccountCocWireMockResponses, ItSpec, PaymentsOrchestratorStub}

class BankAccountCocControllerSpec extends ItSpec {
  val vrn: Vrn = Vrn("2345678890")

  val controller: BankAccountCocController = injector.instanceOf[BankAccountCocController]

  "GET /bank0account-coc/start-journey/manage-or-track" - {
    "authorised starts bank account change of circumstances journey" in {
      AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
      BankAccountCocWireMockResponses.bankOk
      PaymentsOrchestratorStub.customerDataOkWithBankDetails(vrn)
      AuditWireMockResponses.auditIsAvailable
      val result = controller.startBankAccountCocJourney(returnPage = ReturnPage("manage-or-track"), audit = false)(fakeRequest)
      status(result) shouldBe Status.SEE_OTHER
      redirectLocation(result) shouldBe Some("https://www.development.tax.service.gov.uk/change-bank-account/5d8c93b52300000b00271aed")


    }
  }

}
