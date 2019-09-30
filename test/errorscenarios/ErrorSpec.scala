package errorscenarios

import model.{EnrolmentKeys, Vrn}
import support.{AuthWireMockResponses, DesWireMockResponses, ItSpec}

class ErrorSpec extends ItSpec{


  val vrn = Vrn("234567890")
  val path = s"""/vat-repayment-tracker-frontend/show-results/vrn/${vrn.value}"""

  "user is not logged in" in {
    setup("2027-12-12", "2027-11-12")
  }


  private def setup(toDate: String, receivedDate: String, useBankDetails: Boolean = true, ) = {
    AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
    DesWireMockResponses.financialsOkSingle(vrn)
    DesWireMockResponses.customerDataOkWithBankDetails(vrn)
    DesWireMockResponses.obligationsOk(vrn, toDate, receivedDate)
    goToViaPath(path)
  }

}
