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

import java.time.LocalDate

import model.des.RiskingStatus.REPAYMENT_APPROVED
import model.{EnrolmentKeys, PeriodKey, Vrn}
import pages.NoVatRepaymentsFoundPage
import support.{AuditWireMockResponses, AuthWireMockResponses, BrowserSpec, PaymentsOrchestratorStub}

class NoVatRepaymentsFoundSpec extends BrowserSpec:

  val vrn: Vrn = Vrn("234567890")
  val path     = s"""/vat-repayment-tracker/show-vrt"""

  "1. user is authorised and no financial data found" in {
    AuditWireMockResponses.auditIsAvailable
    AuthWireMockResponses.authOkWithEnrolments(
      wireMockBaseUrlAsString = wireMockBaseUrlAsString,
      vrn = vrn,
      enrolment = EnrolmentKeys.mtdVatEnrolmentKey
    )
    PaymentsOrchestratorStub.financialsNotFound(vrn)
    PaymentsOrchestratorStub.customerDataOkWithBankDetails(vrn)
    PaymentsOrchestratorStub.repaymentDetailsNotFound(vrn)
    login()
    goToViaPath(path)
    NoVatRepaymentsFoundPage.assertPageIsDisplayed()

    AuditWireMockResponses.viewRepaymentStatusAudited("showVrt", vrn.value, noRepaymentsFound = true)
  }

  "2. User has no bank details set up and no bank details in flight" in {
    AuditWireMockResponses.auditIsAvailable
    AuthWireMockResponses.authOkWithEnrolments(
      wireMockBaseUrlAsString = wireMockBaseUrlAsString,
      vrn = vrn,
      enrolment = EnrolmentKeys.mtdVatEnrolmentKey
    )
    PaymentsOrchestratorStub.financialsNotFound(vrn)
    PaymentsOrchestratorStub.customerDataOkWithoutBankDetails(vrn)
    PaymentsOrchestratorStub.repaymentDetailsNotFound(vrn)
    login()
    goToViaPath(path)

    NoVatRepaymentsFoundPage.containsNewBankDetailsText(false)
    NoVatRepaymentsFoundPage.containsBAC(result = true)
    NoVatRepaymentsFoundPage.containsBankDetails(result = false)
    NoVatRepaymentsFoundPage.containsBankWarning(result = false)

    AuditWireMockResponses.viewRepaymentStatusAudited("showVrt", vrn.value, noRepaymentsFound = true)
  }

  "3. User has no bank details set up and bank details in flight" in {
    AuditWireMockResponses.auditIsAvailable
    AuthWireMockResponses.authOkWithEnrolments(
      wireMockBaseUrlAsString = wireMockBaseUrlAsString,
      vrn = vrn,
      enrolment = EnrolmentKeys.mtdVatEnrolmentKey
    )
    PaymentsOrchestratorStub.financialsNotFound(vrn)
    PaymentsOrchestratorStub.customerDataOkWithoutBankDetailsInflight(vrn)
    PaymentsOrchestratorStub.repaymentDetailsNotFound(vrn)
    login()
    goToViaPath(path)

    NoVatRepaymentsFoundPage.containsNewBankDetailsText(true)
    NoVatRepaymentsFoundPage.containsBAC(result = false)
    NoVatRepaymentsFoundPage.containsBankDetails(result = false)
    NoVatRepaymentsFoundPage.containsBankWarning(result = false)

    AuditWireMockResponses.viewRepaymentStatusAudited("showVrt", vrn.value, noRepaymentsFound = true)
  }

  "4. User has bank details set up and no bank details in flight" in {
    AuditWireMockResponses.auditIsAvailable
    AuthWireMockResponses.authOkWithEnrolments(
      wireMockBaseUrlAsString = wireMockBaseUrlAsString,
      vrn = vrn,
      enrolment = EnrolmentKeys.mtdVatEnrolmentKey
    )
    PaymentsOrchestratorStub.financialsNotFound(vrn)
    PaymentsOrchestratorStub.customerDataOkWithBankDetails(vrn)
    PaymentsOrchestratorStub.repaymentDetailsNotFound(vrn)
    login()
    goToViaPath(path)

    NoVatRepaymentsFoundPage.containsNewBankDetailsText(false)
    NoVatRepaymentsFoundPage.containsBAC(result = false)
    NoVatRepaymentsFoundPage.containsBankDetails(result = true)
    NoVatRepaymentsFoundPage.containsBankWarning(result = false)

    AuditWireMockResponses.viewRepaymentStatusAudited("showVrt", vrn.value, noRepaymentsFound = true)
  }

  "5. User has bank details set up and bank details in flight" in {
    AuditWireMockResponses.auditIsAvailable
    AuthWireMockResponses.authOkWithEnrolments(
      wireMockBaseUrlAsString = wireMockBaseUrlAsString,
      vrn = vrn,
      enrolment = EnrolmentKeys.mtdVatEnrolmentKey
    )
    PaymentsOrchestratorStub.financialsNotFound(vrn)
    PaymentsOrchestratorStub.customerDataOkWithBankDetailsInflight(vrn)
    PaymentsOrchestratorStub.repaymentDetailsNotFound(vrn)
    login()
    goToViaPath(path)

    NoVatRepaymentsFoundPage.containsNewBankDetailsText(false)
    NoVatRepaymentsFoundPage.containsBAC(result = false)
    NoVatRepaymentsFoundPage.containsBankDetails(result = true)
    NoVatRepaymentsFoundPage.containsBankWarning(result = true)

    AuditWireMockResponses.viewRepaymentStatusAudited("showVrt", vrn.value, noRepaymentsFound = true)
  }

  "6. user has payment over 9 months old" in {
    AuditWireMockResponses.auditIsAvailable
    AuthWireMockResponses.authOkWithEnrolments(
      wireMockBaseUrlAsString = wireMockBaseUrlAsString,
      vrn = vrn,
      enrolment = EnrolmentKeys.mtdVatEnrolmentKey
    )
    PaymentsOrchestratorStub.financialsNotFound(vrn)
    PaymentsOrchestratorStub.customerDataOkWithBankDetails(vrn)
    PaymentsOrchestratorStub.repaymentDetailS1(
      vrn,
      LocalDate.now().minusMonths(9).minusDays(1).toString,
      REPAYMENT_APPROVED,
      PeriodKey("18AG")
    )
    login()
    goToViaPath(path)
    NoVatRepaymentsFoundPage.assertPageIsDisplayed()

    AuditWireMockResponses.viewRepaymentStatusAudited("showVrt", vrn.value)
  }
