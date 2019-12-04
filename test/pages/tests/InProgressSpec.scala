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

package pages.tests

import java.time.LocalDate

import model.des.{CLAIM_QUERIED, INITIAL}
import model.{EnrolmentKeys, PeriodKey, Vrn}
import pages.{ErrorPage, InProgress, NonMtdUser}
import support._

class InProgressSpec extends ItSpec {

  val vrn = Vrn("234567890")
  val path = s"""/vat-repayment-tracker/show-vrt"""

  val periodKey = PeriodKey("18AG")
  val ft_404: Int = 1
  val ft_credit: Int = 2
  val ft_debit: Int = 3

  val details: Map[String, String] = Map(
    ("inprogress_0", "returnCreationDate: 01 Jan 2001, periodKey: 18AA, amount: 6.56"),
    ("inprogress_1", "returnCreationDate: 01 Jan 2001, periodKey: 18AD, amount: 6.56"),
    ("inprogress_2", "returnCreationDate: 01 Jan 2001, periodKey: 18AJ, amount: 5.56")
  )

  "user is authorised and financial data found" in {
    setup()
    InProgress.assertPageIsDisplayed(vrn, amount = "£6.56", appender = "_inprogress")
    InProgress.uniqueToPage
    InProgress.checktabs
    InProgress.breadCrumbsExists
  }

  "user is authorised and financial data found, CLAIM_QUERIED" in {
    setup(status1 = CLAIM_QUERIED.value)
    InProgress.assertPageIsDisplayed(vrn, amount = "£0.00", appender = "_inprogress")
    InProgress.uniqueToPage
    InProgress.checktabs
    InProgress.breadCrumbsExists
  }

  "user is authorised and financial data found but partial" in {
    setup(true, true)
    InProgress.assertPageIsDisplayed(vrn, amount = "£6.56", partialAccount = true, appender = "_inprogress")
    InProgress.uniqueToPage
  }

  "click completed link" in {
    setup()
    InProgress.completedLink
  }

  "user is authorised and address data found" in {
    setup(false)
    InProgress.assertPageIsDisplayed(vrn, false, true, amount = "£6.56", appender = "_inprogress")
    InProgress.uniqueToPage
  }

  "Get ShowResults authorised, no enrolments" in {
    AuditWireMockResponses.auditIsAvailable
    AuthWireMockResponses.authOkNoEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString)
    goToViaPath(path)
    NonMtdUser.assertPageIsDisplayed
  }

  "check negative amount" in {
    AuditWireMockResponses.auditIsAvailable
    AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
    DesWireMockResponses.customerDataOkWithBankDetails(vrn)
    DesWireMockResponses.repaymentDetailS1(vrn, LocalDate.now().toString, INITIAL.value, periodKey)
    goToViaPath(path)
    InProgress.assertPageIsDisplayed(vrn, amount = "£6.56", appender = "_inprogress")
  }

  "multiple inprogress " in {
    setup(true, true, false)
    InProgress.uniqueToPage
    InProgress.completedLink
  }

  "click view progress " in {
    setup(true, true, false)
    InProgress.clickViewProgress("_inprogress")
  }

  "click clickManageAccount" in {
    BankAccountCocWireMockResponses.bankOk
    setup(ft              = ft_debit, useBankDetails = false, singleRepayment = false)
    InProgress.clickCallBac
    AuditWireMockResponses.bacWasAudited(details)
  }

  "click view repayment account then clickManageAccount" in {
    BankAccountCocWireMockResponses.bankOk
    setup(ft              = ft_debit, singleRepayment = false)
    InProgress.clickManageAccount
    InProgress.clickCallBac
    AuditWireMockResponses.bacWasAudited(details)
  }

  "Get ShowResults authorised but non-mtd vrn should show error page" in {
    setup(enrolmentIn = EnrolmentKeys.vatVarEnrolmentKey)
    goToViaPath(path)
    NonMtdUser.assertPageIsDisplayed
  }

  private def setup(
      useBankDetails:     Boolean = true,
      partialBankDetails: Boolean = false,
      singleRepayment:    Boolean = true,
      ft:                 Int     = ft_404,
      status1:            String  = INITIAL.value,
      enrolmentIn:        String  = EnrolmentKeys.mtdVatEnrolmentKey
  ) = {
    VatRepaymentTrackerBackendWireMockResponses.storeOk
    AuditWireMockResponses.auditIsAvailable
    AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = enrolmentIn)
    if (useBankDetails) {
      if (partialBankDetails)
        DesWireMockResponses.customerDataOkWithPartialBankDetails(vrn)
      else
        DesWireMockResponses.customerDataOkWithBankDetails(vrn)
    } else {
      DesWireMockResponses.customerDataOkWithoutBankDetails(vrn)
    }
    if (singleRepayment)
      DesWireMockResponses.repaymentDetailS1(vrn, LocalDate.now().toString, status1, periodKey)
    else
      DesWireMockResponses.repaymentDetailsMultipleInProgress(vrn)

    ft match {
      case `ft_404`    => DesWireMockResponses.financialsNotFound(vrn)
      case `ft_credit` => DesWireMockResponses.financialsOkCredit(vrn)
      case `ft_debit`  => DesWireMockResponses.financialsOkDebit(vrn)
    }

    goToViaPath(path)
  }

}
