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

import model.des.RiskingStatus.{ADJUSMENT_TO_TAX_DUE, INITIAL}

import java.time.LocalDate
import model.{EnrolmentKeys, Vrn}
import pages._
import support._

class InProgressCompletedSpec extends BrowserSpec {

  val vrn: Vrn = Vrn("234567890")
  val path = "/vat-repayment-tracker/show-vrt"

  val ft_404: Int = 1
  val ft_credit: Int = 2
  val ft_debit: Int = 3

  def expectEngagementStatusAudited() =
    AuditWireMockResponses.engagementStatusAudited("showVrt", Map("vrn" -> vrn.value, "engmtType" -> "one_in_progress_multiple_delayed"))

  "1. user is authorised and financial data found" in {
    setup(ft                      = ft_debit, financialDataPeriodKeys = Seq("18AJ"))
    InProgress.uniqueToPage
    InProgressCompleted.checktabs
    InProgressCompleted.breadCrumbsExists

    expectEngagementStatusAudited()
  }
  "2. User has no bank details set up and no bank details in flight" in {
    setup(useBankDetails = false)
    InProgressCompleted.containsNewBankDetailsText(false)
    InProgressCompleted.containsBAC(result = true)
    InProgressCompleted.containsBankDetails(result = false)
    InProgressCompleted.containsBankWarning(result = false)

    expectEngagementStatusAudited()
  }

  "3. User has no bank details set up and bank details in flight" in {
    setup(useBankDetails = false, inflight = true)
    InProgressCompleted.containsNewBankDetailsText(true)
    InProgressCompleted.containsBAC(result = false)
    InProgressCompleted.containsBankDetails(result = false)
    InProgressCompleted.containsBankWarning(result = false)

    expectEngagementStatusAudited()
  }
  "4. User has bank details set up and no bank details in flight" in {
    setup()
    InProgressCompleted.containsNewBankDetailsText(false)
    InProgressCompleted.containsBAC(result = false)
    InProgressCompleted.containsBankDetails(result = true)
    InProgressCompleted.containsBankWarning(result = false)

    expectEngagementStatusAudited()
  }

  "5. User has bank details set up and bank details in flight" in {
    setup(inflight = true)
    InProgressCompleted.containsNewBankDetailsText(false)
    InProgressCompleted.containsBAC(result = false)
    InProgressCompleted.containsBankDetails(result = true)
    InProgressCompleted.containsBankWarning(result = true)

    expectEngagementStatusAudited()
  }

  "6. click completed link" in {
    setup(ft                      = ft_debit, financialDataPeriodKeys = Seq("18AJ"))
    InProgress.clickCompleted
    Completed.uniqueToPage
    InProgressCompleted.checktabs

    expectEngagementStatusAudited()
  }

  "7. click completed link inpast completed" in {
    setup(inPast = true, ft = ft_debit)
    InProgressCompleted.checktabsInPast
    InProgress.completedLink

    expectEngagementStatusAudited()
  }

  private def setup(
      useBankDetails:          Boolean     = true,
      partialBankDetails:      Boolean     = false,
      ft:                      Int         = ft_404,
      inPast:                  Boolean     = false,
      inflight:                Boolean     = false,
      financialDataPeriodKeys: Seq[String] = Seq("18AG")): Unit =
    {
      VatRepaymentTrackerBackendWireMockResponses.storeOk()
      AuditWireMockResponses.auditIsAvailable
      AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
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

      if (inPast)
        PaymentsOrchestratorStub.repaymentDetails2DifferentPeriods(
          LocalDate.now().toString,
          LocalDate.now().minusDays(70).toString,
          INITIAL,
          ADJUSMENT_TO_TAX_DUE,
          vrn
        )
      else
        PaymentsOrchestratorStub.repaymentDetails3Inprogree1Completed(vrn, LocalDate.now())

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

