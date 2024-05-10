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

/*
 * Copyright 2019 HM Revenue & Customs
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

import model.{EnrolmentKeys, Vrn}
import pages._
import support._

class CompletedSpec extends BrowserSpec {

  val vrn: Vrn = Vrn("234567890")
  val path = "/vat-repayment-tracker/show-vrt"

  val ft_404: Int = 1
  val ft_credit: Int = 2
  val ft_debit: Int = 3

  def expectEngagementStatusAudited() =
    AuditWireMockResponses.engagementStatusAudited("showVrt", Map("vrn" -> vrn.value, "engmtType" -> "one_in_progress_multiple_delayed"))

  "1. user is authorised and financial data found" in {
    setup()
    Completed.assertPageIsDisplayed(amount = "£6.56")
    Completed.uniqueToPage
    Completed.checktabs
    Completed.breadCrumbsExists
    Completed.containsBAC(result = false)

    expectEngagementStatusAudited()
  }

  "2. BAC shown" in {
    setup(useBankDetails = false)
    Completed.containsBAC(result = true)

    expectEngagementStatusAudited()
  }

  "3. BAC not shown" in {
    setup(useBankDetails = false, inflight = true)
    Completed.containsBAC(result = false)

    expectEngagementStatusAudited()
  }

  "4. user is authorised and financial data found but partial" in {
    setup(partialBankDetails = true)
    Completed.assertPageIsDisplayed(amount         = "£6.56", partialAccount = true)
    Completed.uniqueToPage

    expectEngagementStatusAudited()
  }

  "5. click in completed link" in {
    setup()
    Completed.viewProgressLink

    expectEngagementStatusAudited()
  }

  "6. multiple completed " in {
    setup(partialBankDetails = true, singleRepayment = false)
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

    expectEngagementStatusAudited()
  }

  "7. check audit" in {
    BankAccountCocWireMockResponses.bankOk
    setup(ft              = ft_debit, singleRepayment = false)
    InProgress.clickManageAccount
    InProgress.clickCallBac
    AuditWireMockResponses.bacWasAuditedNoDetails()

  }

  private def setup(
      useBankDetails:     Boolean = true,
      partialBankDetails: Boolean = false,
      singleRepayment:    Boolean = true,
      ft:                 Int     = ft_404,
      inflight:           Boolean = false): Unit =
    {
      VatRepaymentTrackerBackendWireMockResponses.storeOk()
      AuditWireMockResponses.auditIsAvailable

      AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
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
        PaymentsOrchestratorStub.repaymentDetailSingleCompleted(vrn)
      else
        PaymentsOrchestratorStub.repaymentDetailsMultipleCompleted(vrn)

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
