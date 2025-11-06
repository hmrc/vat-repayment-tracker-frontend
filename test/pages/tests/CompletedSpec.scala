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

import model.{EnrolmentKeys, Vrn}
import pages._
import support._

import java.time.LocalDate

class CompletedSpec extends BrowserSpec {

  val vrn: Vrn = Vrn("234567890")
  val path = "/vat-repayment-tracker/show-vrt"

  val ft_404: Int = 1
  val ft_credit: Int = 2
  val ft_debit: Int = 3

  def expectViewRepaymentStatusAudited(): Unit =
    AuditWireMockResponses.viewRepaymentStatusAudited("showVrt", vrn.value)

  "1. user is authorised and financial data found" in {
    setup()
    Completed.assertPageIsDisplayed(amount = "£6.56")
    Completed.uniqueToPage
    Completed.checktabs
    Completed.breadCrumbsExists
    Completed.containsBAC(result = false)

    expectViewRepaymentStatusAudited()
  }

  "2. User has no bank details set up and no bank details in flight" in {
    setup(useBankDetails = false)
    Completed.containsNewBankDetailsText(result = false)
    Completed.containsBAC(result = true)
    Completed.containsBankDetails(result = false)
    Completed.containsBankWarning(result = false)

    expectViewRepaymentStatusAudited()
  }

  "3. User has no bank details set up and bank details in flight" in {
    setup(useBankDetails = false, inflight = true)
    Completed.containsNewBankDetailsText(result = true)
    Completed.containsBAC(result = false)
    Completed.containsBankDetails(result = false)
    Completed.containsBankWarning(result = false)

    expectViewRepaymentStatusAudited()
  }

  "4. User has bank details set up and no bank details in flight" in {
    setup()
    Completed.containsNewBankDetailsText(result = false)
    Completed.containsBAC(result = false)
    Completed.containsBankDetails(result = true)
    Completed.containsBankWarning(result = false)

    expectViewRepaymentStatusAudited()
  }

  "5. User has bank details set up and bank details in flight" in {
    setup(inflight = true)
    Completed.containsNewBankDetailsText(result = false)
    Completed.containsBAC(result = false)
    Completed.containsBankDetails(result = true)
    Completed.containsBankWarning(result = true)

    expectViewRepaymentStatusAudited()
  }

  "6. user is authorised and financial data found but partial" in {
    setup(partialBankDetails = true)
    Completed.assertPageIsDisplayed(amount         = "£6.56", partialAccount = true)
    Completed.uniqueToPage

    expectViewRepaymentStatusAudited()
  }

  "7. click in completed link" in {
    setup()
    Completed.viewProgressLink

    expectViewRepaymentStatusAudited()
  }

  "8. multiple completed " in {
    setup(partialBankDetails      = true, singleRepayment = false, financialDataPeriodKeys = Seq("18AA", "18AD", "18AG", "18AJ"))
    Completed.assertPageIsDisplayed(amount         = "£6.56", partialAccount = true, period = "1 January to 31 January 2018")

    Completed.readRowForPeriod("18AA") shouldBe Seq(
      "01 Jan 2001", "1 January to 31 January 2018", "£6.56", "View history View history for accounting period 1 January to 31 January 2018"
    )
    Completed.readRowForPeriod("18AD") shouldBe Seq(
      "01 Jan 2001", "1 April to 30 April 2018", "£6.56", "View history View history for accounting period 1 April to 30 April 2018"
    )
    Completed.readRowForPeriod("18AG") shouldBe Seq(
      "01 Jan 2001", "1 July to 31 July 2018", "£6.56", "View history View history for accounting period 1 July to 31 July 2018"
    )
    Completed.readRowForPeriod("18AJ") shouldBe Seq(
      "01 Jan 2001", "1 October to 31 October 2018", "£5.56", "View history View history for accounting period 1 October to 31 October 2018"
    )

    Completed.uniqueToPage
    Completed.viewProgressLink

    expectViewRepaymentStatusAudited()
  }

  "9. check audit" in {
    BankAccountCocWireMockResponses.bankOk
    setup(ft              = ft_debit, singleRepayment = false)
    InProgress.clickManageAccount
    InProgress.clickCallBac
    AuditWireMockResponses.bacWasAuditedNoDetails()
  }

  private def setup(
      useBankDetails:          Boolean     = true,
      partialBankDetails:      Boolean     = false,
      singleRepayment:         Boolean     = true,
      ft:                      Int         = ft_debit,
      inflight:                Boolean     = false,
      financialDataPeriodKeys: Seq[String] = Seq("18AG")
  ): Unit =
    {
      VatRepaymentTrackerBackendWireMockResponses.storeOk()
      AuditWireMockResponses.auditIsAvailable

      AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString, vrn, EnrolmentKeys.mtdVatEnrolmentKey)

      if (useBankDetails) {
        if (partialBankDetails)
          PaymentsOrchestratorStub.customerDataOkWithPartialBankDetails(vrn)
        else if (inflight)
          PaymentsOrchestratorStub.customerDataOkWithBankDetailsInflight(vrn)
        else
          PaymentsOrchestratorStub.customerDataOkWithBankDetails(vrn)
      } else {
        if (inflight)
          PaymentsOrchestratorStub.customerDataOkWithoutBankDetailsInflight(vrn)
        else
          PaymentsOrchestratorStub.customerDataOkWithoutBankDetails(vrn)
      }

      if (singleRepayment)
        PaymentsOrchestratorStub.repaymentDetailSingleCompleted(vrn, LocalDate.now())
      else
        PaymentsOrchestratorStub.repaymentDetailsMultipleCompleted(vrn, LocalDate.now())

      ft match {
        case `ft_404`    => PaymentsOrchestratorStub.financialsNotFound(vrn)
        case `ft_credit` => PaymentsOrchestratorStub.financialsOkCredit(vrn, financialDataPeriodKeys)
        case `ft_debit`  => PaymentsOrchestratorStub.financialsOkDebit(vrn, financialDataPeriodKeys)
        case other       => throw new IllegalArgumentException(s"no ft match for $other")
      }

      login()
      goToViaPath(path)
    }

}
