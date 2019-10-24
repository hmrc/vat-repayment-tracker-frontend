package pages.tests

import java.time.LocalDate

import model.des.{CLAIM_QUERIED, INITIAL}
import model.{EnrolmentKeys, PeriodKey, Vrn}
import pages.{InProgress, ViewProgress}
import support.{AuthWireMockResponses, DesWireMockResponses, ItSpec, VatRepaymentTrackerBackendWireMockResponses}

class ViewProgressSpec  extends ItSpec{

  val vrn = Vrn("234567890")
  val path = s"""/vat-repayment-tracker-frontend/show-results/vrn/${vrn.value}"""
  val ft_404 : Int= 1
  val ft_credit: Int = 2
  val ft_debit: Int = 3


  "click view progress " in {
    setup(useBankDetails =true, inPast =false, rdsp = 1, periodKey = PeriodKey("18AC"), ft=ft_404)
    InProgress.clickViewProgress("_inprogress")

    ViewProgress.checkAmount("£5.56")
  }


  private def setup(useBankDetails: Boolean = true, inPast : Boolean = false,
                    status1: String = INITIAL.value, status2: String = CLAIM_QUERIED.value, status3 : String = "", rdsp: Int, periodKey: PeriodKey,ft:Int) = {
    VatRepaymentTrackerBackendWireMockResponses.storeOk
    AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
    if (useBankDetails) {
        DesWireMockResponses.customerDataOkWithBankDetails(vrn)
    } else {
      DesWireMockResponses.customerDataOkWithoutBankDetails(vrn)
    }
    val date = if (inPast) LocalDate.now().minusDays(50).toString else LocalDate.now().toString
    rdsp match {
      case 1 => {
        DesWireMockResponses.repaymentDetailS1(vrn,date.toString,status1)
        VatRepaymentTrackerBackendWireMockResponses.repaymentDetailS1(vrn,date.toString,status1,periodKey)
      }
      case 2 => {
        DesWireMockResponses.repaymentDetailS2(vrn,date.toString,status1,status2)
        VatRepaymentTrackerBackendWireMockResponses.repaymentDetailS2(vrn,date.toString,status1,status2,periodKey)
      }
      case 3 => {
        DesWireMockResponses.repaymentDetailS3(vrn, date.toString, status1, status2,status3)
        VatRepaymentTrackerBackendWireMockResponses.repaymentDetailS3(vrn,date.toString,status1,status2,status3,periodKey)
      }
    }

    ft match{
      case `ft_404` => DesWireMockResponses.financialsNotFound(vrn)
      case `ft_credit` => DesWireMockResponses.financialsOkCredit(vrn)
      case `ft_debit` => DesWireMockResponses.financialsOkDebit(vrn)
    }
    goToViaPath(path)
  }
}
