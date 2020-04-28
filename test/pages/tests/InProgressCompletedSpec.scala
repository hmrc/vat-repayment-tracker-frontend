/*
 * Copyright 2020 HM Revenue & Customs
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

import java.time.LocalDate

import model.des.{ADJUSMENT_TO_TAX_DUE, INITIAL}
import model.{EnrolmentKeys, Vrn}
import pages._
import support._

class InProgressCompletedSpec extends ItSpec {

  val vrn: Vrn = Vrn("234567890")
  val path = s"""/vat-repayment-tracker/show-vrt"""

  val ft_404: Int = 1
  val ft_credit: Int = 2
  val ft_debit: Int = 3

  "1. user is authorised and financial data found" in {
    setup()
    InProgress.uniqueToPage
    InProgressCompleted.checktabs
    InProgressCompleted.breadCrumbsExists
  }
  "2. BAC shown" in {
    setup(useBankDetails = false)
    InProgressCompleted.containsBAC(result = true)
  }

  "3. BAC not shown" in {
    setup(useBankDetails = false, inflight = true)
    InProgressCompleted.containsBAC(result = false)
  }

  "4. click completed link" in {
    setup()
    InProgress.clickCompleted
    Completed.uniqueToPage
    InProgressCompleted.checktabs
  }

  "5. click completed link inpast but not completed" in {
    setup(inPast = true)
    InProgress.clickCompleted
    Completed.uniqueToPage
    InProgressCompleted.checktabs
  }

  "6. click completed link inpast completed" in {
    setup(inPast = true, ft = ft_debit)
    InProgressCompleted.checktabsInPast
    InProgress.completedLink
  }

  private def setup(
      useBankDetails:     Boolean = true,
      partialBankDetails: Boolean = false,
      ft:                 Int     = ft_404,
      inPast:             Boolean = false,
      inflight:           Boolean = false): Unit =
    {
      VatRepaymentTrackerBackendWireMockResponses.storeOk()
      AuditWireMockResponses.auditIsAvailable
      AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
      if (useBankDetails) {
        if (partialBankDetails)
          DesWireMockResponses.customerDataOkWithPartialBankDetails(vrn)
        else
          DesWireMockResponses.customerDataOkWithBankDetails(vrn)
      } else {
        if (inflight)
          DesWireMockResponses.customerDataOkWithoutBankDetailsInflight(vrn)
        else
          DesWireMockResponses.customerDataOkWithoutBankDetails(vrn)
      }

      if (inPast)
        DesWireMockResponses.repaymentDetails2DifferentPeriods(LocalDate.now().toString, LocalDate.now().minusDays(70).toString, INITIAL.value, ADJUSMENT_TO_TAX_DUE.value, vrn)
      else
        DesWireMockResponses.repaymentDetails3Inprogree1Completed(vrn)

      ft match {
        case `ft_404`    => DesWireMockResponses.financialsNotFound(vrn)
        case `ft_credit` => DesWireMockResponses.financialsOkCredit(vrn)
        case `ft_debit`  => DesWireMockResponses.financialsOkDebit(vrn)
      }

      goToViaPath(path)
    }

}

