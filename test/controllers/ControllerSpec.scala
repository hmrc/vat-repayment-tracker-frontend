package controllers

import model.des.RiskingStatus.INITIAL
import model.{EnrolmentKeys, PeriodKey, Vrn}
import play.api.http.Status
import play.api.test.Helpers._
import play.api.test.Helpers.status
import support.{AuditWireMockResponses, AuthWireMockResponses, GgStub, ItSpec, PaymentsOrchestratorStub, VatRepaymentTrackerBackendWireMockResponses}

import java.time.LocalDate

class ControllerSpec extends ItSpec {
  val vrn: Vrn = Vrn("2345678890")
  val periodKey: PeriodKey = PeriodKey("18AG")

  val controller: Controller = injector.instanceOf[Controller]

  "GET /show-results/vrn/:vrn" - {
    "authorised shows results" in {
      AuditWireMockResponses.auditIsAvailable
      AuthWireMockResponses.authOkWithEnrolments(
        wireMockBaseUrlAsString = wireMockBaseUrlAsString,
        vrn = vrn,
        enrolment = EnrolmentKeys.mtdVatEnrolmentKey
      )
      PaymentsOrchestratorStub.customerDataOkWithBankDetails(vrn)
      PaymentsOrchestratorStub.repaymentDetailS1(vrn, LocalDate.now().toString, INITIAL, periodKey)
      PaymentsOrchestratorStub.financialsOkCredit(vrn)
      VatRepaymentTrackerBackendWireMockResponses.storeOk()
      val result = controller.showResults(vrn)(fakeRequest)
      status(result) shouldBe Status.OK
    }
    "not authorised redirects to login" in {
      AuthWireMockResponses.authFailed
      AuditWireMockResponses.auditIsAvailable
      GgStub.signInPage(19001, vrn)
      val result = controller.showResults(vrn)(fakeRequest)
//      result.body should include("Sign in using Government Gateway")
      status(result) shouldBe Status.SEE_OTHER
      redirectLocation(result) shouldBe Some("http://localhost:11111/auth-login-stub/gg-sign-in?continue=http%3A%2F%2Flocalhost%3A19001%2F&origin=pay-online")

    }
  }
  "GET /view-repayment-account" - {
    "authorised views repayment account" in {
      AuditWireMockResponses.auditIsAvailable
      AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
      PaymentsOrchestratorStub.customerDataOkWithBankDetails(vrn)
      val result = controller.viewRepaymentAccount(audit = true)(fakeRequest)
      status(result) shouldBe Status.OK
    }

  }
  "GET /show-vrt" - {
    "authorised show vrt" in {
      AuditWireMockResponses.auditIsAvailable
      AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
      PaymentsOrchestratorStub.customerDataOkWithBankDetails(vrn)
      PaymentsOrchestratorStub.repaymentDetailS1(vrn, LocalDate.now().toString, INITIAL, periodKey)
      VatRepaymentTrackerBackendWireMockResponses.storeOk()
      PaymentsOrchestratorStub.financialsOkCredit(vrn)
      val result = controller.showVrt(fakeRequest)
      status(result) shouldBe Status.OK
    }

  }

}
