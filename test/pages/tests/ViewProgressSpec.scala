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

package pages.tests

import model.des.RiskingStatus
import model.des.RiskingStatus._

import java.time.LocalDate
import model.{EnrolmentKeys, PeriodKey, Vrn}
import org.openqa.selenium.WebDriver
import pages.{InProgress, ViewProgress}
import support._

class ViewProgressSpec extends BrowserSpec {

  val vrn: Vrn = Vrn("234567890")
  val path = s"""/vat-repayment-tracker/show-vrt"""
  val ft_404: Int = 1
  val ft_credit: Int = 2
  val ft_debit: Int = 3

  "start" - {

    "id: 1 click view progress basic" in {
      setup(rdsp      = 1, periodKey = PeriodKey("18AG"), ft = ft_404)
      InProgress.clickViewProgress()
      ViewProgress.checkAmount("£6.56")
      ViewProgress.checkEstimatedRepaymentDate(6)
      ViewProgress.checkStatusExists(List(INITIAL))
      ViewProgress.checkStatusNotPresent(List(SENT_FOR_RISKING, CLAIM_QUERIED, REPAYMENT_ADJUSTED, ADJUSMENT_TO_TAX_DUE, REPAYMENT_APPROVED))
      ViewProgress.checkMainMessage("Your repayment is being processed")
      ViewProgress.backExists()
    }

    "id: 2 , add in INITIAL status (CLAIM QUERIED)" in {
      setup(rdsp      = 1, periodKey = PeriodKey("18AG"), ft = ft_404, status1 = CLAIM_QUERIED)
      goToViaPath(path)
      //        InProgress.clickViewProgress()
      ViewProgress.checkAmount("£0.00")
      ViewProgress.checkEstimatedRepaymentDate(6)
      ViewProgress.checkStatusExists(List(CLAIM_QUERIED, INITIAL))
      ViewProgress.checkStatusNotPresent(List(SENT_FOR_RISKING, REPAYMENT_ADJUSTED, ADJUSMENT_TO_TAX_DUE, REPAYMENT_APPROVED))
      ViewProgress.checkMainMessage("Your repayment is being processed")
      ViewProgress.payUrl(expectedValue = false)
      ViewProgress.historyUrl(expectedValue = false)
    }

    "id: 2 , add in INITIAL status (SENT_FOR_RISKING)" in {
      setup(rdsp      = 1, periodKey = PeriodKey("18AG"), ft = ft_404, status1 = SENT_FOR_RISKING)
      InProgress.clickViewProgress()
      ViewProgress.checkAmount("£6.56")
      ViewProgress.checkEstimatedRepaymentDate(6)
      ViewProgress.checkStatusExists(List(SENT_FOR_RISKING, INITIAL))
      ViewProgress.checkStatusNotPresent(List(CLAIM_QUERIED, REPAYMENT_ADJUSTED, ADJUSMENT_TO_TAX_DUE, REPAYMENT_APPROVED))
      ViewProgress.checkMainMessage("Your repayment is being processed")
      ViewProgress.payUrl(expectedValue = false)
      ViewProgress.historyUrl(expectedValue = false)
    }

    "id: 4 , add in INITIAL status (CLAIM QUERIED) in past" in {
      setup(inPast    = true, rdsp = 1, periodKey = PeriodKey("18AG"), ft = ft_404, status1 = CLAIM_QUERIED)
      InProgress.clickViewProgress()
      ViewProgress.checkAmount("£0.00")
      ViewProgress.checkEstimatedRepaymentDate(-44) //50 -6
      ViewProgress.checkStatusExists(List(CLAIM_QUERIED, INITIAL))
      ViewProgress.checkStatusNotPresent(List(SENT_FOR_RISKING, REPAYMENT_ADJUSTED, ADJUSMENT_TO_TAX_DUE, REPAYMENT_APPROVED))
      ViewProgress.checkMainMessage("Your repayment is delayed")
      ViewProgress.payUrl(expectedValue = false)
      ViewProgress.historyUrl(expectedValue = false)
    }

    "id: 3 , REPAYMENT_ADJUSTED" in {
      setup(rdsp      = 2, periodKey = PeriodKey("18AG"), ft = ft_404, status2 = REPAYMENT_ADJUSTED)
      InProgress.clickViewProgress()
      ViewProgress.checkAmount("£6.56")
      ViewProgress.checkEstimatedRepeaymentDateNotPresent
      ViewProgress.checkStatusExists(List(REPAYMENT_ADJUSTED, INITIAL))
      ViewProgress.checkStatusNotPresent(List(CLAIM_QUERIED, SENT_FOR_RISKING, ADJUSMENT_TO_TAX_DUE, REPAYMENT_APPROVED))
      ViewProgress.checkStatusNotPresent(List(REPAYMENT_ADJUSTED), completed = true)
      ViewProgress.checkMainMessage("Your repayment has been approved")
      ViewProgress.payUrl(expectedValue = false)
      ViewProgress.historyUrl(expectedValue = false)
    }

    "id: 5, ADJUSMENT_TO_TAX_DUE" in {
      setup(rdsp      = 2, periodKey = PeriodKey("18AG"), ft = ft_404, status2 = ADJUSMENT_TO_TAX_DUE)
      InProgress.clickViewProgress()
      ViewProgress.checkAmount("£6.56")
      ViewProgress.checkEstimatedRepeaymentDateNotPresent
      ViewProgress.checkStatusExists(List(ADJUSMENT_TO_TAX_DUE, INITIAL))
      ViewProgress.checkStatusNotPresent(List(CLAIM_QUERIED, SENT_FOR_RISKING, REPAYMENT_ADJUSTED, REPAYMENT_APPROVED))
      ViewProgress.checkStatusNotPresent(List(ADJUSMENT_TO_TAX_DUE), completed = true)
      ViewProgress.checkMainMessage("You need to make a VAT payment")
      ViewProgress.payUrl(expectedValue = true)
      ViewProgress.historyUrl(expectedValue = false)
    }

    "id: 6, REPAYMENT_APPROVED" in {
      setup(rdsp      = 2, periodKey = PeriodKey("18AG"), ft = ft_404, status2 = REPAYMENT_APPROVED)
      InProgress.clickViewProgress()
      ViewProgress.checkAmount("£6.56")
      ViewProgress.checkEstimatedRepeaymentDateNotPresent
      ViewProgress.checkStatusExists(List(REPAYMENT_APPROVED, INITIAL))
      ViewProgress.checkStatusNotPresent(List(CLAIM_QUERIED, SENT_FOR_RISKING, REPAYMENT_ADJUSTED, ADJUSMENT_TO_TAX_DUE))
      ViewProgress.checkStatusNotPresent(List(REPAYMENT_APPROVED), completed = true)
      ViewProgress.checkMainMessage("Your repayment has been approved")
      ViewProgress.payUrl(expectedValue = false)
      ViewProgress.historyUrl(expectedValue = false)
    }

    "id: 7, REPAYMENT_ADJUSTED AND Credit Charge Exists" in {
      setup(rdsp      = 2, periodKey = PeriodKey("18AG"), ft = ft_credit, status2 = REPAYMENT_ADJUSTED)
      InProgress.clickViewProgress()
      ViewProgress.checkAmount("£6.56")
      ViewProgress.checkEstimatedRepeaymentDateNotPresent
      ViewProgress.checkStatusExists(List(REPAYMENT_ADJUSTED, INITIAL))
      ViewProgress.checkStatusExists(List(REPAYMENT_ADJUSTED), completed = true)
      ViewProgress.checkStatusNotPresent(List(CLAIM_QUERIED, SENT_FOR_RISKING, REPAYMENT_APPROVED, ADJUSMENT_TO_TAX_DUE))
      ViewProgress.checkMainMessage("Your repayment is complete")
      ViewProgress.payUrl(expectedValue = false)
      ViewProgress.historyUrl(expectedValue = true)
    }

    "id: 8, ADJUSMENT_TO_TAX_DUE AND Debit Charge Exists" in {
      setup(rdsp      = 2, periodKey = PeriodKey("18AG"), ft = ft_debit, status2 = ADJUSMENT_TO_TAX_DUE)
      InProgress.clickViewProgress()
      ViewProgress.checkAmount("£6.56")
      ViewProgress.checkEstimatedRepeaymentDateNotPresent
      ViewProgress.checkStatusExists(List(ADJUSMENT_TO_TAX_DUE, INITIAL))
      ViewProgress.checkStatusExists(List(ADJUSMENT_TO_TAX_DUE), completed = true)
      ViewProgress.checkStatusNotPresent(List(CLAIM_QUERIED, SENT_FOR_RISKING, REPAYMENT_APPROVED, REPAYMENT_ADJUSTED))
      ViewProgress.checkMainMessage("Your repayment is complete")
      ViewProgress.payUrl(expectedValue = false)
      ViewProgress.historyUrl(expectedValue = true)
    }

    "id: 9, REPAYMENT_APPROVED AND Credit Charge Exists" in {
      setup(rdsp      = 2, periodKey = PeriodKey("18AG"), ft = ft_credit, status2 = REPAYMENT_APPROVED)
      InProgress.clickViewProgress()
      ViewProgress.checkAmount("£6.56")
      ViewProgress.checkEstimatedRepeaymentDateNotPresent
      ViewProgress.checkStatusExists(List(REPAYMENT_APPROVED, INITIAL))
      ViewProgress.checkStatusExists(List(REPAYMENT_APPROVED), completed = true)
      ViewProgress.checkStatusNotPresent(List(CLAIM_QUERIED, SENT_FOR_RISKING, REPAYMENT_ADJUSTED, ADJUSMENT_TO_TAX_DUE))
      ViewProgress.checkMainMessage("Your repayment is complete")
      ViewProgress.payUrl(expectedValue = false)
      ViewProgress.historyUrl(expectedValue = true)
    }

    "check 3 status" in {
      setup(rdsp      = 3, periodKey = PeriodKey("18AG"), ft = ft_credit, status3 = REPAYMENT_APPROVED)
      InProgress.clickViewProgress()
      ViewProgress.checkAmount("£6.56")
      ViewProgress.checkEstimatedRepeaymentDateNotPresent
      ViewProgress.checkStatusExists(List(REPAYMENT_APPROVED, INITIAL, CLAIM_QUERIED))
      ViewProgress.checkStatusExists(List(REPAYMENT_APPROVED), completed = true)
      ViewProgress.checkStatusNotPresent(List(SENT_FOR_RISKING, REPAYMENT_ADJUSTED, ADJUSMENT_TO_TAX_DUE))
      ViewProgress.checkMainMessage("Your repayment is complete")
      ViewProgress.payUrl(expectedValue = false)
      ViewProgress.historyUrl(expectedValue = true)

    }

    "id: 2 , add in INITIAL status (SENT_FOR_RISKING) , #001 status" in {
      setup(rdsp             = 1, periodKey = PeriodKey("#001"), ft = ft_404, status1 = SENT_FOR_RISKING, periodKeyBackend = PeriodKey("%23001"))
      InProgress.clickViewProgress()
      ViewProgress.checkAmount("£6.56")
      ViewProgress.checkEstimatedRepaymentDate(6)
      ViewProgress.checkStatusExists(List(SENT_FOR_RISKING, INITIAL))
      ViewProgress.checkStatusNotPresent(List(CLAIM_QUERIED, REPAYMENT_ADJUSTED, ADJUSMENT_TO_TAX_DUE, REPAYMENT_APPROVED))
      ViewProgress.checkMainMessage("Your repayment is being processed")
      ViewProgress.payUrl(expectedValue = false)
      ViewProgress.historyUrl(expectedValue = false)
    }

    "display action required messaging if payment is suspended" in {
      setup(rdsp             = 1, periodKey = PeriodKey("#001"), ft = ft_404, status1 = REPAYMENT_SUSPENDED, periodKeyBackend = PeriodKey("%23001"))
      InProgress.clickViewProgress()

      ViewProgress.checkActionRequired(result = true)

      ViewProgress.amount shouldBe None
      ViewProgress.estimatedDate shouldBe None
    }

    "don't display action required messaging if payment is not suspended" in {
      setup(rdsp             = 1, periodKey = PeriodKey("#001"), ft = ft_404, status1 = CLAIM_QUERIED, periodKeyBackend = PeriodKey("%23001"))
      InProgress.clickViewProgress()

      ViewProgress.checkActionRequired(result = false)

      ViewProgress.amount.isDefined shouldBe true
      ViewProgress.estimatedDate.isDefined shouldBe true
    }

    // not a good test :-(  You have to look in logfile for 'KNOZ' msgs, remove once issue has been fixed ( OPS-8163 )
    "check zero amount logger" in {
      setup(rdsp      = 4, periodKey = PeriodKey("18AG"), ft = ft_credit, status3 = REPAYMENT_APPROVED)
      InProgress.clickViewProgress()
      ViewProgress.checkAmount("£0.00")
      ViewProgress.checkEstimatedRepeaymentDateNotPresent
      ViewProgress.checkStatusExists(List(REPAYMENT_APPROVED, INITIAL, CLAIM_QUERIED))
      ViewProgress.checkStatusExists(List(REPAYMENT_APPROVED), completed = false)
      ViewProgress.checkStatusNotPresent(List(SENT_FOR_RISKING, REPAYMENT_ADJUSTED, ADJUSMENT_TO_TAX_DUE))
      ViewProgress.checkMainMessage("Your repayment is complete")
      ViewProgress.payUrl(expectedValue = false)
      ViewProgress.historyUrl(expectedValue = true)

    }
  }

  private def setup(
      useBankDetails: Boolean       = true,
      inPast:         Boolean       = false,
      status1:        RiskingStatus = INITIAL,
      status2:        RiskingStatus = CLAIM_QUERIED,
      status3:        RiskingStatus = REPAYMENT_ADJUSTED, rdsp: Int,
      periodKey:        PeriodKey,
      ft:               Int,
      periodKeyBackend: PeriodKey = PeriodKey("18AG")): Unit =
    {
      VatRepaymentTrackerBackendWireMockResponses.storeOk()
      AuditWireMockResponses.auditIsAvailable
      AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
      if (useBankDetails) {
        PaymentsOrchestratorStub.customerDataOkWithBankDetails(vrn)
      } else {
        PaymentsOrchestratorStub.customerDataOkWithoutBankDetails(vrn)
      }
      val date = if (inPast) LocalDate.now().minusDays(50).toString else LocalDate.now().toString
      rdsp match {
        case 1 =>
          PaymentsOrchestratorStub.repaymentDetailS1(vrn, date.toString, status1, periodKey)
          VatRepaymentTrackerBackendWireMockResponses.repaymentDetailS1(vrn, date.toString, status1, periodKeyBackend)
        case 2 =>
          PaymentsOrchestratorStub.repaymentDetailS2(vrn, date.toString, status1, status2)
          VatRepaymentTrackerBackendWireMockResponses.repaymentDetailS2(vrn, date.toString, status1, status2, periodKey)
        case 3 =>
          PaymentsOrchestratorStub.repaymentDetailS3(vrn, date.toString, status1, status2, status3)
          VatRepaymentTrackerBackendWireMockResponses.repaymentDetailS3(vrn, date.toString, status1, status2, status3, periodKey)
        case 4 =>
          PaymentsOrchestratorStub.repaymentDetailS3(vrn, date.toString, status1, status2, status3)
          VatRepaymentTrackerBackendWireMockResponses.repaymentDetailS4(vrn, date.toString, status1, status2, status3, periodKey)
      }

      ft match {
        case `ft_404`    => PaymentsOrchestratorStub.financialsNotFound(vrn)
        case `ft_credit` => PaymentsOrchestratorStub.financialsOkCredit(vrn)
        case `ft_debit`  => PaymentsOrchestratorStub.financialsOkDebit(vrn)
        case _           => throw new IllegalArgumentException("no ft match")
      }

      webDriver.manage().getCookieNamed("mdtp") match {
        case null =>
          login()
          goToViaPath(path)
        case _ => goToViaPath(path)
      }
    }
}
