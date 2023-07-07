package controllers

import model.des.RiskingStatus.INITIAL
import model.{EnrolmentKeys, PeriodKey, Vrn}
import play.api.http.Status
import play.api.test.Helpers._
import play.api.test.Helpers.status
import support.{AuditWireMockResponses, AuthWireMockResponses, ItSpec, PaymentsOrchestratorStub, VatRepaymentTrackerBackendWireMockResponses}

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

    }
  }
  "GET /view-repayment-account" - {

  }
  "GET /show-vrt" - {

  }

}
