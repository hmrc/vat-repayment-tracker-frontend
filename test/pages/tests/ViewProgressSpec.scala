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

import model.des._
import model.{EnrolmentKeys, PeriodKey, Vrn}
import pages.{InProgress, ViewProgress}
import support.{AuthWireMockResponses, DesWireMockResponses, ItSpec, VatRepaymentTrackerBackendWireMockResponses}

class ViewProgressSpec extends ItSpec {

  val vrn = Vrn("234567890")
  val path = s"""/vat-repayment-tracker-frontend/show-results/vrn/${vrn.value}"""
  val ft_404: Int = 1
  val ft_credit: Int = 2
  val ft_debit: Int = 3

  "id: 1 click view progress basic" in {
    setup(useBankDetails = true, inPast = false, rdsp = 1, periodKey = PeriodKey("18AC"), ft = ft_404)
    InProgress.clickViewProgress("_inprogress")
    ViewProgress.checkAmount("£6.56")
    ViewProgress.checkEstimatedRepaymentDate(6)
    ViewProgress.checkStatusExists(List(INITIAL.value))
    ViewProgress.checkStatusNotPresent(List(SENT_FOR_RISKING.value, CLAIM_QUERIED.value, REPAYMENT_ADJUSTED.value, ADJUSTMENT_TO_TAX_DUE.value, REPAYMENT_APPROVED.value))
    ViewProgress.checkMainMessage("Your repayment is being processed")
  }

  "id: 2 , add in INITIAL status (CLAIM QUERIED)" in {
    setup(useBankDetails = true, inPast = false, rdsp = 1, periodKey = PeriodKey("18AC"), ft = ft_404, status1 = CLAIM_QUERIED.value)
    InProgress.clickViewProgress("_inprogress")
    ViewProgress.checkAmount("£6.56")
    ViewProgress.checkEstimatedRepaymentDate(6)
    ViewProgress.checkStatusExists(List(CLAIM_QUERIED.value, INITIAL.value))
    ViewProgress.checkStatusNotPresent(List(SENT_FOR_RISKING.value, REPAYMENT_ADJUSTED.value, ADJUSTMENT_TO_TAX_DUE.value, REPAYMENT_APPROVED.value))
    ViewProgress.checkMainMessage("Your repayment is being processed")
  }

  "id: 2 , add in INITIAL status (SENT_FOR_RISKING)" in {
    setup(useBankDetails = true, inPast = false, rdsp = 1, periodKey = PeriodKey("18AC"), ft = ft_404, status1 = SENT_FOR_RISKING.value)
    InProgress.clickViewProgress("_inprogress")
    ViewProgress.checkAmount("£6.56")
    ViewProgress.checkEstimatedRepaymentDate(6)
    ViewProgress.checkStatusExists(List(SENT_FOR_RISKING.value, INITIAL.value))
    ViewProgress.checkStatusNotPresent(List(CLAIM_QUERIED.value, REPAYMENT_ADJUSTED.value, ADJUSTMENT_TO_TAX_DUE.value, REPAYMENT_APPROVED.value))
    ViewProgress.checkMainMessage("Your repayment is being processed")
  }

  "id: 4 , add in INITIAL status (CLAIM QUERIED) in past" in {
    setup(useBankDetails = true, inPast = true, rdsp = 1, periodKey = PeriodKey("18AC"), ft = ft_404, status1 = CLAIM_QUERIED.value)
    InProgress.clickViewProgress("_inprogress")
    ViewProgress.checkAmount("£6.56")
    ViewProgress.checkEstimatedRepaymentDate(-44) //50 -6
    ViewProgress.checkStatusExists(List(CLAIM_QUERIED.value, INITIAL.value))
    ViewProgress.checkStatusNotPresent(List(SENT_FOR_RISKING.value, REPAYMENT_ADJUSTED.value, ADJUSTMENT_TO_TAX_DUE.value, REPAYMENT_APPROVED.value))
    ViewProgress.checkMainMessage("Your repayment is delayed")
  }

  "id: 3 , REPAYMENT_ADJUSTED" in {
    setup(useBankDetails = true, inPast = false, rdsp = 2, periodKey = PeriodKey("18AC"), ft = ft_404, status2 = REPAYMENT_ADJUSTED.value)
    InProgress.clickViewProgress("_completed")
    ViewProgress.checkAmount("£6.56")
    ViewProgress.checkEstimatedRepeaymentDateNotPresent
    ViewProgress.checkStatusExists(List(REPAYMENT_ADJUSTED.value, INITIAL.value))
    ViewProgress.checkStatusNotPresent(List(CLAIM_QUERIED.value, SENT_FOR_RISKING.value, ADJUSTMENT_TO_TAX_DUE.value, REPAYMENT_APPROVED.value))
    ViewProgress.checkMainMessage("Your repayment has been approved")
  }

  "id: 5, ADJUSTMENT_TO_TAX_DUE" in {
    setup(useBankDetails = true, inPast = false, rdsp = 2, periodKey = PeriodKey("18AC"), ft = ft_404, status2 = ADJUSTMENT_TO_TAX_DUE.value)
    InProgress.clickViewProgress("_completed")
    ViewProgress.checkAmount("£6.56")
    ViewProgress.checkEstimatedRepeaymentDateNotPresent
    ViewProgress.checkStatusExists(List(ADJUSTMENT_TO_TAX_DUE.value, INITIAL.value))
    ViewProgress.checkStatusNotPresent(List(CLAIM_QUERIED.value, SENT_FOR_RISKING.value, REPAYMENT_ADJUSTED.value, REPAYMENT_APPROVED.value))
    ViewProgress.checkMainMessage("You need to make a VAT payment")
  }

  "id: 6, REPAYMENT_APPROVED" in {
    setup(useBankDetails = true, inPast = false, rdsp = 2, periodKey = PeriodKey("18AC"), ft = ft_404, status2 = REPAYMENT_APPROVED.value)
    InProgress.clickViewProgress("_completed")
    ViewProgress.checkAmount("£6.56")
    ViewProgress.checkEstimatedRepeaymentDateNotPresent
    ViewProgress.checkStatusExists(List(REPAYMENT_APPROVED.value, INITIAL.value))
    ViewProgress.checkStatusNotPresent(List(CLAIM_QUERIED.value, SENT_FOR_RISKING.value, REPAYMENT_ADJUSTED.value, ADJUSTMENT_TO_TAX_DUE.value))
    ViewProgress.checkMainMessage("Your repayment has been approved")
  }

  "id: 7, REPAYMENT_ADJUSTED AND Credit Charge Exists" in {
    setup(useBankDetails = true, inPast = false, rdsp = 2, periodKey = PeriodKey("18AC"), ft = ft_credit, status2 = REPAYMENT_ADJUSTED.value)
    InProgress.clickViewProgress("_completed")
    ViewProgress.checkAmount("£6.56")
    ViewProgress.checkEstimatedRepeaymentDateNotPresent
    ViewProgress.checkStatusExists(List(REPAYMENT_ADJUSTED.value, INITIAL.value))
    ViewProgress.checkStatusNotPresent(List(CLAIM_QUERIED.value, SENT_FOR_RISKING.value, REPAYMENT_APPROVED.value, ADJUSTMENT_TO_TAX_DUE.value))
    ViewProgress.checkMainMessage("Your repayment is complete")
  }

  "id: 8, ADJUSTED_TO_TAX_DUE AND Debit Charge Exists" in {
    setup(useBankDetails = true, inPast = false, rdsp = 2, periodKey = PeriodKey("18AC"), ft = ft_debit, status2 = ADJUSTMENT_TO_TAX_DUE.value)
    InProgress.clickViewProgress("_completed")
    ViewProgress.checkAmount("£6.56")
    ViewProgress.checkEstimatedRepeaymentDateNotPresent
    ViewProgress.checkStatusExists(List(ADJUSTMENT_TO_TAX_DUE.value, INITIAL.value))
    ViewProgress.checkStatusNotPresent(List(CLAIM_QUERIED.value, SENT_FOR_RISKING.value, REPAYMENT_APPROVED.value, REPAYMENT_ADJUSTED.value))
    ViewProgress.checkMainMessage("Your repayment is complete")
  }

  "id: 9, REPAYMENT_APPROVED AND Credit Charge Exists" in {
    setup(useBankDetails = true, inPast = false, rdsp = 2, periodKey = PeriodKey("18AC"), ft = ft_credit, status2 = REPAYMENT_APPROVED.value)
    InProgress.clickViewProgress("_completed")
    ViewProgress.checkAmount("£6.56")
    ViewProgress.checkEstimatedRepeaymentDateNotPresent
    ViewProgress.checkStatusExists(List(REPAYMENT_APPROVED.value, INITIAL.value))
    ViewProgress.checkStatusNotPresent(List(CLAIM_QUERIED.value, SENT_FOR_RISKING.value, REPAYMENT_ADJUSTED.value, ADJUSTMENT_TO_TAX_DUE.value))
    ViewProgress.checkMainMessage("Your repayment is complete")
  }

  "check 3 status" in {
    setup(useBankDetails = true, inPast = false, rdsp = 3, periodKey = PeriodKey("18AC"), ft = ft_credit, status3 = REPAYMENT_APPROVED.value)
    InProgress.clickViewProgress("_completed")
    ViewProgress.checkAmount("£6.56")
    ViewProgress.checkEstimatedRepeaymentDateNotPresent
    ViewProgress.checkStatusExists(List(REPAYMENT_APPROVED.value, INITIAL.value, CLAIM_QUERIED.value))
    ViewProgress.checkStatusNotPresent(List(SENT_FOR_RISKING.value, REPAYMENT_ADJUSTED.value, ADJUSTMENT_TO_TAX_DUE.value))
    ViewProgress.checkMainMessage("Your repayment is complete")

  }

  private def setup(useBankDetails: Boolean = true, inPast: Boolean = false,
                    status1: String = INITIAL.value, status2: String = CLAIM_QUERIED.value, status3: String = "", rdsp: Int, periodKey: PeriodKey, ft: Int) = {
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
        DesWireMockResponses.repaymentDetailS1(vrn, date.toString, status1)
        VatRepaymentTrackerBackendWireMockResponses.repaymentDetailS1(vrn, date.toString, status1, periodKey)
      }
      case 2 => {
        DesWireMockResponses.repaymentDetailS2(vrn, date.toString, status1, status2)
        VatRepaymentTrackerBackendWireMockResponses.repaymentDetailS2(vrn, date.toString, status1, status2, periodKey)
      }
      case 3 => {
        DesWireMockResponses.repaymentDetailS3(vrn, date.toString, status1, status2, status3)
        VatRepaymentTrackerBackendWireMockResponses.repaymentDetailS3(vrn, date.toString, status1, status2, status3, periodKey)
      }
    }

    ft match {
      case `ft_404`    => DesWireMockResponses.financialsNotFound(vrn)
      case `ft_credit` => DesWireMockResponses.financialsOkCredit(vrn)
      case `ft_debit`  => DesWireMockResponses.financialsOkDebit(vrn)
    }
    goToViaPath(path)
  }
}
