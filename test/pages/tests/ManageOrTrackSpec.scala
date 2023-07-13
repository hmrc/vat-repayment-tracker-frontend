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

import model.des.RiskingStatus.INITIAL

import java.time.LocalDate
import model.{EnrolmentKeys, PeriodKey, Vrn}
import pages.{InProgress, ManageOrTrack, ViewRepaymentAccount}
import support._

class ManageOrTrackSpec extends BrowserSpec {

  val vrn: Vrn = Vrn("234567890")
  val path = s"""/vat-repayment-tracker/manage-or-track-vrt"""
  val periodKey: PeriodKey = PeriodKey("18AG")

  val ft_404: Int = 1
  val ft_credit: Int = 2
  val ft_debit: Int = 3

  "1. user is authorised, bank dd option, manage bank option " in {
    setup()
    ManageOrTrack.assertPageIsDisplayed(vrn, ddDisplayed = true, bankDisplayed = true)
  }

  "2. user is authorised, manage dd option " in {
    setup(useBankDetails = false)
    ManageOrTrack.assertPageIsDisplayed(vrn, ddDisplayed = true, nobankDisplayed = true)
  }

  "3. user is authorised, manage bank option " in {
    setup(useDdDetails = false)
    ManageOrTrack.assertPageIsDisplayed(vrn, bankDisplayed = true, noddDisplayed = true)

  }

  "4. user is authorised, manage no bank or dd option " in {
    setup(useBankDetails = false, useDdDetails = false)
    ManageOrTrack.assertPageIsDisplayed(vrn, noddDisplayed = true, nobankDisplayed = true)
  }

  "5. user is authorised, manage no bank or dd option but inflight bank " in {
    setup(useBankDetails = false, useDdDetails = false, inflight = true)
    ManageOrTrack.assertPageIsDisplayed(vrn, noddDisplayed = true)
  }

  "6. click vrtLabel" in {
    setup()
    println(s"after set up - url : ${webDriver.getCurrentUrl}")
    ManageOrTrack.clickVrtLabel()
    println(s"after click vrt label - url : ${webDriver.getCurrentUrl}")
    ManageOrTrack.clickContinue()
    println(s"after click continue - url : ${webDriver.getCurrentUrl}")
    InProgress.assertPageIsDisplayed(amount = "£6.56")
  }

  "7. click bankLabel" in {
    BankAccountCocWireMockResponses.bankOk
    setup()
    ManageOrTrack.clickBankLabel()
    ManageOrTrack.clickContinue()
    ViewRepaymentAccount.assertPageIsDisplayed(vrn, """/vat-repayment-tracker/view-repayment-account""")
    ManageOrTrack.clickCallBac
    AuditWireMockResponses.bacWasNotAudited()
  }

  private def setup(
      useBankDetails: Boolean = true,
      useDdDetails:   Boolean = true,
      ft:             Int     = ft_404,
      inflight:       Boolean = false): Unit =
    {
      AuditWireMockResponses.auditIsAvailable
      VatRepaymentTrackerBackendWireMockResponses.storeOk()
      AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)

      if (inflight)
        PaymentsOrchestratorStub.customerDataOkWithoutBankDetailsInflight(vrn)
      else {
        if (useBankDetails)
          PaymentsOrchestratorStub.customerDataOkWithBankDetails(vrn)
        else
          PaymentsOrchestratorStub.customerDataOkWithoutBankDetails(vrn)
      }
      //Show dd radio button
      if (useDdDetails)
        PaymentsOrchestratorStub.ddOk(vrn)
      else
        PaymentsOrchestratorStub.ddNotFound(vrn)

      PaymentsOrchestratorStub.repaymentDetailS1(vrn, LocalDate.now().toString, INITIAL, periodKey)

      ft match {
        case `ft_404`    => PaymentsOrchestratorStub.financialsNotFound(vrn)
        case `ft_credit` => PaymentsOrchestratorStub.financialsOkCredit(vrn)
        case `ft_debit`  => PaymentsOrchestratorStub.financialsOkDebit(vrn)
      }

      login()
      goToViaPath(path)
    }

}
