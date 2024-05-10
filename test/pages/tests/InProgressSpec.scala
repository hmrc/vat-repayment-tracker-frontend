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
import model.des.RiskingStatus.{CLAIM_QUERIED, INITIAL}

import java.time.LocalDate
import model.{EnrolmentKeys, PeriodKey, Vrn}
import org.scalatest.concurrent.PatienceConfiguration.Timeout
import org.scalatest.time.{Seconds, Span}
import pages.{InProgress, NonMtdUser}
import support._

class InProgressSpec extends BrowserSpec {

  val vrn: Vrn = Vrn("234567890")
  val path = "/vat-repayment-tracker/show-vrt"

  val periodKey: PeriodKey = PeriodKey("18AG")
  val ft_404: Int = 1
  val ft_credit: Int = 2
  val ft_debit: Int = 3

  val details: Map[String, String] = Map(
    ("inprogress_0", "returnCreationDate: 01 Jan 2001, periodKey: 18AA, amount: 5.56"),
    ("inprogress_1", "returnCreationDate: 01 Jan 2001, periodKey: 18AD, amount: 5.56"),
    ("inprogress_2", "returnCreationDate: 01 Jan 2001, periodKey: 18AJ, amount: 5.56"),
    ("vrn", s"${vrn.value}")
  )

  def expectEngagementStatusAudited() =
    AuditWireMockResponses.engagementStatusAudited("showVrt", Map("vrn" -> vrn.value, "engmtType" -> "one_in_progress_multiple_delayed"))

  "1. user is authorised and financial data found" in {
    setup()
    InProgress.assertPageIsDisplayed(amount = "£0.00")
    InProgress.uniqueToPage
    InProgress.checktabs
    InProgress.breadCrumbsExists

    expectEngagementStatusAudited()
  }

  "2. user is authorised and financial data found, CLAIM_QUERIED" in {
    setup(status1 = CLAIM_QUERIED)
    InProgress.assertPageIsDisplayed(amount = "£0.00")
    InProgress.uniqueToPage
    InProgress.checktabs
    InProgress.breadCrumbsExists

    expectEngagementStatusAudited()
  }

  "3. user is authorised and financial data found but partial" in {
    setup(partialBankDetails = true)
    InProgress.assertPageIsDisplayed(amount         = "£0.00", partialAccount = true)
    InProgress.uniqueToPage

    expectEngagementStatusAudited()
  }

  "4. click completed link" in {
    setup()
    InProgress.completedLink

    expectEngagementStatusAudited()
  }

  "5. user is authorised and address data found" in {
    setup(useBankDetails = false)
    InProgress.assertPageIsDisplayed(checkBank    = false, checkAddress = true, amount = "£0.00")
    InProgress.uniqueToPage

    expectEngagementStatusAudited()
  }

  "6. Get ShowResults authorised, no enrolments" in {
    AuditWireMockResponses.auditIsAvailable
    AuthWireMockResponses.authOkNoEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString)

    login()
    goToViaPath(path)

    eventually(Timeout(Span(5, Seconds))) {
      NonMtdUser.assertPageIsDisplayed
      AuditWireMockResponses.engagementStatusAudited("nonMtdUser", Map("engmtType" -> "none_in_progress"))
    }
  }

  "7. check negative amount" in {
    AuditWireMockResponses.auditIsAvailable
    AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
    PaymentsOrchestratorStub.customerDataOkWithBankDetails(vrn)
    PaymentsOrchestratorStub.repaymentDetailS1(vrn, LocalDate.now().toString, INITIAL, periodKey, negativeAmt = true)
    PaymentsOrchestratorStub.financialsOkCredit(vrn)
    VatRepaymentTrackerBackendWireMockResponses.storeOk()
    goToViaPath(path)
    InProgress.assertPageIsDisplayed(amount = "£0.00")

    expectEngagementStatusAudited()
  }

  "8. multiple inprogress " in {
    setup(partialBankDetails = true, singleRepayment = false)
    InProgress.uniqueToPage
    InProgress.completedLink

    expectEngagementStatusAudited()
  }

  "9. click view progress " in {
    setup(partialBankDetails = true, singleRepayment = false)
    InProgress.clickViewProgress()
  }

  "10. click clickManageAccount" in {
    BankAccountCocWireMockResponses.bankOk
    setup(ft              = ft_debit, useBankDetails = false, singleRepayment = false)
    InProgress.clickCallBac
    AuditWireMockResponses.bacWasAudited(details)
  }

  "11. click view repayment account then clickManageAccount" in {
    BankAccountCocWireMockResponses.bankOk
    setup(ft              = ft_debit, singleRepayment = false)
    InProgress.clickManageAccount
    InProgress.clickCallBac
    AuditWireMockResponses.bacWasAudited(details)
  }

  "12. display Submit VAT Return CTA if repayments are suspended" in {
    BankAccountCocWireMockResponses.bankOk
    setup(ft      = ft_debit, status1 = RiskingStatus.REPAYMENT_SUSPENDED)
    InProgress.containsSuspendedWarning(result = true)
  }

  "13. don't display Submit VAT Return CTA if no repayments suspended" in {
    BankAccountCocWireMockResponses.bankOk
    setup(ft              = ft_debit, singleRepayment = false)
    InProgress.containsSuspendedWarning(result = false)
  }

  private def setup(
      useBankDetails:     Boolean       = true,
      partialBankDetails: Boolean       = false,
      singleRepayment:    Boolean       = true,
      ft:                 Int           = ft_404,
      status1:            RiskingStatus = INITIAL,
      enrolmentIn:        String        = EnrolmentKeys.mtdVatEnrolmentKey,
      inflight:           Boolean       = false
  ): Unit = {
    VatRepaymentTrackerBackendWireMockResponses.storeOk()
    AuditWireMockResponses.auditIsAvailable
    AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = enrolmentIn)
    if (useBankDetails) {
      if (partialBankDetails)
        PaymentsOrchestratorStub.customerDataOkWithPartialBankDetails(vrn)
      else
        PaymentsOrchestratorStub.customerDataOkWithBankDetails(vrn)
    } else {
      if (inflight)
        PaymentsOrchestratorStub.customerDataOkWithoutBankDetailsInflight(vrn)
      else
        PaymentsOrchestratorStub.customerDataOkWithoutBankDetails(vrn)
    }

    if (singleRepayment)
      PaymentsOrchestratorStub.repaymentDetailS1(vrn, LocalDate.now().toString, status1, periodKey)
    else
      PaymentsOrchestratorStub.repaymentDetailsMultipleInProgress(vrn)

    ft match {
      case `ft_404`    => PaymentsOrchestratorStub.financialsNotFound(vrn)
      case `ft_credit` => PaymentsOrchestratorStub.financialsOkCredit(vrn)
      case `ft_debit`  => PaymentsOrchestratorStub.financialsOkDebit(vrn)
      case other       => throw new IllegalArgumentException(s"no ft match for $other")
    }

    login()
    goToViaPath(path)
  }

}
