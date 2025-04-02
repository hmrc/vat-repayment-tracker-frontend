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
import pages.ViewProgress.ProgressTimelineItem
import pages.{InProgress, ViewProgress}
import support._

import java.time.format.DateTimeFormatter

class ViewProgressSpec extends BrowserSpec {

  val vrn: Vrn = Vrn("234567890")
  val path = "/vat-repayment-tracker/show-vrt"
  val ft_404: Int = 1
  val ft_credit: Int = 2
  val ft_debit: Int = 3
  val `ft_noClearingDate`: Int = 4
  val `ft_twoClearingDates`: Int = 5
  val `ft_emptyItemsArray`: Int = 6

  val today = LocalDate.now()

  def formatDayShortMonthYear(d: LocalDate) = d.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))

  val formattedTodayString = formatDayShortMonthYear(today)

  "id: 1 click view progress basic" in {
    setup(rdsp      = 1, periodKey = PeriodKey("18AG"), ft = ft_404)
    InProgress.clickViewProgress()
    ViewProgress.checkAmount("£0.00")
    ViewProgress.checkEstimatedRepaymentDate(6)
    ViewProgress.checkStatusExists(List(INITIAL))
    ViewProgress.checkStatusNotPresent(List(SENT_FOR_RISKING, CLAIM_QUERIED, REPAYMENT_ADJUSTED, ADJUSMENT_TO_TAX_DUE, REPAYMENT_APPROVED))
    ViewProgress.checkMainMessage("Your repayment is being processed")
    ViewProgress.backExists()
    ViewProgress.assertWebchatLinkPresent()

    ViewProgress.getProgressTimelineItems shouldBe List(
      ProgressTimelineItem("Checking amount", formattedTodayString, List("We received your return and are now checking the repayment amount we owe you."))
    )
  }

  "id: 2 , add in INITIAL status (CLAIM QUERIED)" in {
    setup(rdsp      = 1, periodKey = PeriodKey("18AG"), ft = ft_404, status1 = CLAIM_QUERIED)
    InProgress.clickViewProgress()
    ViewProgress.checkAmount("£0.00")
    ViewProgress.checkEstimatedRepaymentDate(6)
    ViewProgress.checkStatusExists(List(CLAIM_QUERIED, INITIAL))
    ViewProgress.checkStatusNotPresent(List(SENT_FOR_RISKING, REPAYMENT_ADJUSTED, ADJUSMENT_TO_TAX_DUE, REPAYMENT_APPROVED))
    ViewProgress.checkMainMessage("Your repayment is being processed")
    ViewProgress.payUrl(expectedValue = false)
    ViewProgress.historyUrl(expectedValue = false)
    ViewProgress.assertWebchatLinkPresent()

    ViewProgress.getProgressTimelineItems shouldBe List(
      ProgressTimelineItem(
        "Sending for further checks",
        formattedTodayString,
        List("We are making sure we pay you the right amount. You do not need to do anything, but we may contact you if we need any further information.")
      ),
      ProgressTimelineItem(
        "Checking amount",
        formattedTodayString,
        List("We received your return and are now checking the repayment amount we owe you.")
      )
    )
  }

  "id: 2 , add in INITIAL status (SENT_FOR_RISKING)" in {
    setup(rdsp      = 1, periodKey = PeriodKey("18AG"), ft = ft_404, status1 = SENT_FOR_RISKING)
    InProgress.clickViewProgress()
    ViewProgress.checkAmount("£0.00")
    ViewProgress.checkEstimatedRepaymentDate(6)
    ViewProgress.checkStatusExists(List(SENT_FOR_RISKING, INITIAL))
    ViewProgress.checkStatusNotPresent(List(CLAIM_QUERIED, REPAYMENT_ADJUSTED, ADJUSMENT_TO_TAX_DUE, REPAYMENT_APPROVED))
    ViewProgress.checkMainMessage("Your repayment is being processed")
    ViewProgress.payUrl(expectedValue = false)
    ViewProgress.historyUrl(expectedValue = false)
    ViewProgress.assertWebchatLinkPresent()

    ViewProgress.getProgressTimelineItems shouldBe List(
      ProgressTimelineItem(
        "Sending for further checks",
        formattedTodayString,
        List("We are making sure we pay you the right amount. You do not need to do anything, but we may contact you if we need any further information.")
      ),
      ProgressTimelineItem(
        "Checking amount",
        formattedTodayString,
        List("We received your return and are now checking the repayment amount we owe you.")
      )
    )
  }

  "id: 4 , add in INITIAL status (CLAIM QUERIED) in past" in {
    val _50DaysAgoFormattedString = formatDayShortMonthYear(LocalDate.now().minusDays(50))

    setup(inPast    = true, rdsp = 1, periodKey = PeriodKey("18AG"), ft = ft_404, status1 = CLAIM_QUERIED)
    InProgress.clickViewProgress()
    ViewProgress.checkAmount("£0.00")
    ViewProgress.checkEstimatedRepaymentDate(-44) //50 -6
    ViewProgress.checkStatusExists(List(CLAIM_QUERIED, INITIAL))
    ViewProgress.checkStatusNotPresent(List(SENT_FOR_RISKING, REPAYMENT_ADJUSTED, ADJUSMENT_TO_TAX_DUE, REPAYMENT_APPROVED))
    ViewProgress.checkMainMessage("Your repayment is delayed")
    ViewProgress.payUrl(expectedValue = false)
    ViewProgress.historyUrl(expectedValue = false)
    ViewProgress.assertWebchatLinkPresent()

    ViewProgress.getProgressTimelineItems shouldBe List(
      ProgressTimelineItem(
        "Estimated repayment date has passed",
        _50DaysAgoFormattedString,
        List(
          "You do not need to do anything right now. We are working on repaying you as soon as possible. We'll contact you with any further information about the delay."
        )
      ),
      ProgressTimelineItem(
        "Checking amount",
        _50DaysAgoFormattedString,
        List("We received your return and are now checking the repayment amount we owe you.")
      )
    )
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
    ViewProgress.assertWebchatLinkPresent()

    ViewProgress.getProgressTimelineItems shouldBe List(
      ProgressTimelineItem(
        "Repayment amount changed",
        formattedTodayString,
        List(
          "You claimed £5.56. We calculated this amount was incorrect so we will repay you £6.56. This will reach your " +
            "repayment bank account in 3 working days. We sent you a letter explaining why we changed your amount.",
          "If you do not receive a letter in the next 7 days, check your VAT payments history."
        )
      ),
      ProgressTimelineItem(
        "Checking amount",
        formattedTodayString,
        List("We received your return and are now checking the repayment amount we owe you.")
      )
    )
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
    ViewProgress.assertWebchatLinkPresent()

    ViewProgress.getProgressTimelineItems shouldBe List(
      ProgressTimelineItem(
        "You now owe HMRC",
        formattedTodayString,
        List(
          "We calculated that the original amount of £5.56 you claimed was incorrect. You now owe HMRC £6.56. We sent " +
            "you a letter with the reason for this change."
        )
      ),
      ProgressTimelineItem(
        "Checking amount",
        formattedTodayString,
        List("We received your return and are now checking the repayment amount we owe you.")
      )
    )
  }

  "id: 6, REPAYMENT_APPROVED" in {
    setup(rdsp      = 2, periodKey = PeriodKey("18AG"), ft = ft_404, status2 = REPAYMENT_APPROVED)
    InProgress.clickViewProgress()
    ViewProgress.checkAmount("£5.56")
    ViewProgress.checkEstimatedRepeaymentDateNotPresent
    ViewProgress.checkStatusExists(List(REPAYMENT_APPROVED, INITIAL))
    ViewProgress.checkStatusNotPresent(List(CLAIM_QUERIED, SENT_FOR_RISKING, REPAYMENT_ADJUSTED, ADJUSMENT_TO_TAX_DUE))
    ViewProgress.checkStatusNotPresent(List(REPAYMENT_APPROVED), completed = true)
    ViewProgress.checkMainMessage("Your repayment has been approved")
    ViewProgress.payUrl(expectedValue = false)
    ViewProgress.historyUrl(expectedValue = false)
    ViewProgress.assertWebchatLinkPresent()

    ViewProgress.getProgressTimelineItems shouldBe List(
      ProgressTimelineItem(
        "Repayment approved",
        formattedTodayString,
        List("This will reach your repayment bank account in 3 workings days.")
      ),
      ProgressTimelineItem(
        "Checking amount",
        formattedTodayString,
        List("We received your return and are now checking the repayment amount we owe you.")
      )
    )
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
    ViewProgress.assertWebchatLinkPresent()

    ViewProgress.getProgressTimelineItems shouldBe List(
      ProgressTimelineItem(
        "Repayment complete",
        "01 Mar 2018",
        List(
          "We sent an adjusted payment of £6.56 to your repayment bank account:",
          "Name: Account holder",
          "Account number: ****2222",
          "Sort code: 66 77 88."
        )
      ),
      ProgressTimelineItem(
        "Repayment amount changed",
        formattedTodayString,
        List(
          "You claimed £5.56. We calculated this amount was incorrect so we will repay you £6.56. This will reach your " +
            "repayment bank account in 3 working days. We sent you a letter explaining why we changed your amount.",
          "If you do not receive a letter in the next 7 days, check your VAT payments history."
        )
      ),
      ProgressTimelineItem(
        "Checking amount",
        formattedTodayString,
        List("We received your return and are now checking the repayment amount we owe you.")
      )
    )
  }

  "id: 7, REPAYMENT_ADJUSTED AND Credit Charge Exists with several dates" in {
    setup(rdsp      = 2, periodKey = PeriodKey("18AG"), ft = `ft_twoClearingDates`, status2 = REPAYMENT_ADJUSTED)
    InProgress.clickViewProgress()
    ViewProgress.checkAmount("£6.56")
    ViewProgress.checkEstimatedRepeaymentDateNotPresent
    ViewProgress.checkStatusExists(List(REPAYMENT_ADJUSTED, INITIAL))
    ViewProgress.checkStatusExists(List(REPAYMENT_ADJUSTED), completed = true)
    ViewProgress.checkStatusNotPresent(List(CLAIM_QUERIED, SENT_FOR_RISKING, REPAYMENT_APPROVED, ADJUSMENT_TO_TAX_DUE))
    ViewProgress.checkMainMessage("Your repayment is complete")
    ViewProgress.payUrl(expectedValue = false)
    ViewProgress.historyUrl(expectedValue = true)
    ViewProgress.assertWebchatLinkPresent()

    ViewProgress.getProgressTimelineItems shouldBe List(
      ProgressTimelineItem(
        "Repayment complete",
        "03 Mar 2018",
        List(
          "We sent an adjusted payment of £6.56 to your repayment bank account:",
          "Name: Account holder",
          "Account number: ****2222",
          "Sort code: 66 77 88."
        )
      ),
      ProgressTimelineItem(
        "Repayment amount changed",
        formattedTodayString,
        List(
          "You claimed £5.56. We calculated this amount was incorrect so we will repay you £6.56. This will reach your " +
            "repayment bank account in 3 working days. We sent you a letter explaining why we changed your amount.",
          "If you do not receive a letter in the next 7 days, check your VAT payments history."
        )
      ),
      ProgressTimelineItem(
        "Checking amount",
        formattedTodayString,
        List("We received your return and are now checking the repayment amount we owe you.")
      )
    )
  }

  "id: 7, REPAYMENT_ADJUSTED, Credit Charge Exists, Clearing Date Does Not Exist" in {
    setup(rdsp      = 2, periodKey = PeriodKey("18AG"), ft = ft_noClearingDate, status2 = REPAYMENT_ADJUSTED)
    InProgress.clickViewProgress()
    ViewProgress.checkAmount("£6.56")
    ViewProgress.checkEstimatedRepeaymentDateNotPresent
    ViewProgress.checkStatusExists(List(REPAYMENT_ADJUSTED, INITIAL))
    ViewProgress.checkStatusExists(List(REPAYMENT_ADJUSTED), completed = true)
    ViewProgress.checkStatusNotPresent(List(CLAIM_QUERIED, SENT_FOR_RISKING, REPAYMENT_APPROVED, ADJUSMENT_TO_TAX_DUE))
    ViewProgress.checkMainMessage("Your repayment is complete")
    ViewProgress.payUrl(expectedValue = false)
    ViewProgress.historyUrl(expectedValue = true)
    ViewProgress.assertWebchatLinkPresent()

    ViewProgress.getProgressTimelineItems shouldBe List(
      ProgressTimelineItem(
        "Repayment complete",
        formattedTodayString,
        List(
          "We sent an adjusted payment of £6.56 to your repayment bank account:",
          "Name: Account holder",
          "Account number: ****2222",
          "Sort code: 66 77 88."
        )
      ),
      ProgressTimelineItem(
        "Repayment amount changed",
        formattedTodayString,
        List(
          "You claimed £5.56. We calculated this amount was incorrect so we will repay you £6.56. This will reach your " +
            "repayment bank account in 3 working days. We sent you a letter explaining why we changed your amount.",
          "If you do not receive a letter in the next 7 days, check your VAT payments history."
        )
      ),
      ProgressTimelineItem(
        "Checking amount",
        formattedTodayString,
        List("We received your return and are now checking the repayment amount we owe you.")
      )
    )
  }

  "id: 7, REPAYMENT_ADJUSTED AND Credit Charge Exists AND no bank details" in {
    setup(rdsp           = 2, periodKey = PeriodKey("18AG"), ft = ft_credit, status2 = REPAYMENT_ADJUSTED, useBankDetails = false)
    InProgress.clickViewProgress()
    ViewProgress.checkAmount("£6.56")
    ViewProgress.checkEstimatedRepeaymentDateNotPresent
    ViewProgress.checkStatusExists(List(REPAYMENT_ADJUSTED, INITIAL))
    ViewProgress.checkStatusExists(List(REPAYMENT_ADJUSTED), completed = true)
    ViewProgress.checkStatusNotPresent(List(CLAIM_QUERIED, SENT_FOR_RISKING, REPAYMENT_APPROVED, ADJUSMENT_TO_TAX_DUE))
    ViewProgress.checkMainMessage("Your repayment is complete")
    ViewProgress.payUrl(expectedValue = false)
    ViewProgress.historyUrl(expectedValue = true)
    ViewProgress.assertWebchatLinkPresent()

    ViewProgress.getProgressTimelineItems shouldBe List(
      ProgressTimelineItem(
        "Repayment complete",
        "01 Mar 2018",
        List(
          "We sent you an adjusted payment of £6.56 as a cheque to",
          "VAT PPOB Line1\nVAT PPOB Line2\nVAT PPOB Line3\nVAT PPOB Line4\nTF3 4ER\n."
        )
      ),
      ProgressTimelineItem(
        "Repayment amount changed",
        formattedTodayString,
        List(
          "You claimed £5.56. We calculated this amount was incorrect so we will repay you £6.56. We will send a " +
            "cheque to your business address. This will reach you in 5 to 6 working days. We sent you a letter " +
            "explaining why we changed your amount.",
          "If you do not receive a letter in the next 7 days, check your VAT payments history."
        )
      ),
      ProgressTimelineItem(
        "Checking amount",
        formattedTodayString,
        List("We received your return and are now checking the repayment amount we owe you.")
      )
    )
  }

  "id: 7, REPAYMENT_ADJUSTED AND Credit Charge Exists With Empty Items Array" in {
    setup(rdsp      = 2, periodKey = PeriodKey("18AG"), ft = `ft_emptyItemsArray`, status2 = REPAYMENT_ADJUSTED)
    InProgress.clickViewProgress()
    ViewProgress.checkAmount("£6.56")
    ViewProgress.checkEstimatedRepeaymentDateNotPresent
    ViewProgress.checkStatusExists(List(REPAYMENT_ADJUSTED, INITIAL))
    ViewProgress.checkStatusExists(List(REPAYMENT_ADJUSTED), completed = true)
    ViewProgress.checkStatusNotPresent(List(CLAIM_QUERIED, SENT_FOR_RISKING, REPAYMENT_APPROVED, ADJUSMENT_TO_TAX_DUE))
    ViewProgress.checkMainMessage("Your repayment is complete")
    ViewProgress.payUrl(expectedValue = false)
    ViewProgress.historyUrl(expectedValue = true)
    ViewProgress.assertWebchatLinkPresent()

    ViewProgress.getProgressTimelineItems shouldBe List(
      ProgressTimelineItem(
        "Repayment complete",
        formattedTodayString,
        List(
          "We sent an adjusted payment of £6.56 to your repayment bank account:",
          "Name: Account holder",
          "Account number: ****2222",
          "Sort code: 66 77 88."
        )
      ),
      ProgressTimelineItem(
        "Repayment amount changed",
        formattedTodayString,
        List(
          "You claimed £5.56. We calculated this amount was incorrect so we will repay you £6.56. This will reach your " +
            "repayment bank account in 3 working days. We sent you a letter explaining why we changed your amount.",
          "If you do not receive a letter in the next 7 days, check your VAT payments history."
        )
      ),
      ProgressTimelineItem(
        "Checking amount",
        formattedTodayString,
        List("We received your return and are now checking the repayment amount we owe you.")
      )
    )
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
    ViewProgress.assertWebchatLinkPresent()

    ViewProgress.getProgressTimelineItems shouldBe List(
      ProgressTimelineItem(
        "Repayment complete",
        "01 Mar 2018",
        List("We received your VAT payment.")
      ),
      ProgressTimelineItem(
        "You now owe HMRC",
        formattedTodayString,
        List(
          "We calculated that the original amount of £5.56 you claimed was incorrect. You now owe HMRC £6.56. We sent " +
            "you a letter with the reason for this change."
        )
      ),
      ProgressTimelineItem(
        "Checking amount",
        formattedTodayString,
        List("We received your return and are now checking the repayment amount we owe you.")
      )
    )
  }

  "id: 9, REPAYMENT_APPROVED AND Credit Charge Exists" in {
    setup(rdsp      = 2, periodKey = PeriodKey("18AG"), ft = ft_credit, status2 = REPAYMENT_APPROVED)
    InProgress.clickViewProgress()
    ViewProgress.checkAmount("£5.56")
    ViewProgress.checkEstimatedRepeaymentDateNotPresent
    ViewProgress.checkStatusExists(List(REPAYMENT_APPROVED, INITIAL))
    ViewProgress.checkStatusExists(List(REPAYMENT_APPROVED), completed = true)
    ViewProgress.checkStatusNotPresent(List(CLAIM_QUERIED, SENT_FOR_RISKING, REPAYMENT_ADJUSTED, ADJUSMENT_TO_TAX_DUE))
    ViewProgress.checkMainMessage("Your repayment is complete")
    ViewProgress.payUrl(expectedValue = false)
    ViewProgress.historyUrl(expectedValue = true)
    ViewProgress.assertWebchatLinkPresent()

    ViewProgress.getProgressTimelineItems shouldBe List(
      ProgressTimelineItem(
        "Repayment complete",
        "01 Mar 2018",
        List(
          "We sent a payment of £5.56 to your repayment bank account:",
          "Name: Account holder",
          "Account number: ****2222",
          "Sort code: 66 77 88."
        )
      ),
      ProgressTimelineItem(
        "Repayment approved",
        formattedTodayString,
        List("This will reach your repayment bank account in 3 workings days.")
      ),
      ProgressTimelineItem(
        "Checking amount",
        formattedTodayString,
        List("We received your return and are now checking the repayment amount we owe you.")
      )
    )
  }

  "id: 9, REPAYMENT_APPROVED AND Credit Charge Exists AND no bank details" in {
    setup(rdsp           = 2, periodKey = PeriodKey("18AG"), ft = ft_credit, status2 = REPAYMENT_APPROVED, useBankDetails = false)
    InProgress.clickViewProgress()
    ViewProgress.checkAmount("£5.56")
    ViewProgress.checkEstimatedRepeaymentDateNotPresent
    ViewProgress.checkStatusExists(List(REPAYMENT_APPROVED, INITIAL))
    ViewProgress.checkStatusExists(List(REPAYMENT_APPROVED), completed = true)
    ViewProgress.checkStatusNotPresent(List(CLAIM_QUERIED, SENT_FOR_RISKING, REPAYMENT_ADJUSTED, ADJUSMENT_TO_TAX_DUE))
    ViewProgress.checkMainMessage("Your repayment is complete")
    ViewProgress.payUrl(expectedValue = false)
    ViewProgress.historyUrl(expectedValue = true)
    ViewProgress.assertWebchatLinkPresent()

    ViewProgress.getProgressTimelineItems shouldBe List(
      ProgressTimelineItem(
        "Repayment complete",
        "01 Mar 2018",
        List(
          "We sent a payment of £5.56 as a cheque to",
          "VAT PPOB Line1\nVAT PPOB Line2\nVAT PPOB Line3\nVAT PPOB Line4\nTF3 4ER\n."
        )
      ),
      ProgressTimelineItem(
        "Repayment approved",
        formattedTodayString,
        List(
          "We will send a cheque to your business address. This will reach you in 5 to 6 working days. We sent you a " +
            "letter explaining why we changed your amount.",
          "If you do not receive a letter in the next few days, check your VAT payments history."
        )
      ),
      ProgressTimelineItem(
        "Checking amount",
        formattedTodayString,
        List("We received your return and are now checking the repayment amount we owe you.")
      )
    )
  }

  "check 3 status" in {
    setup(rdsp      = 3, periodKey = PeriodKey("18AG"), ft = ft_credit, status3 = REPAYMENT_APPROVED)
    InProgress.clickViewProgress()
    ViewProgress.checkAmount("£5.56")
    ViewProgress.checkEstimatedRepeaymentDateNotPresent
    ViewProgress.checkStatusExists(List(REPAYMENT_APPROVED, INITIAL, CLAIM_QUERIED))
    ViewProgress.checkStatusExists(List(REPAYMENT_APPROVED), completed = true)
    ViewProgress.checkStatusNotPresent(List(SENT_FOR_RISKING, REPAYMENT_ADJUSTED, ADJUSMENT_TO_TAX_DUE))
    ViewProgress.checkMainMessage("Your repayment is complete")
    ViewProgress.payUrl(expectedValue = false)
    ViewProgress.historyUrl(expectedValue = true)
    ViewProgress.assertWebchatLinkPresent()

    ViewProgress.getProgressTimelineItems shouldBe List(
      ProgressTimelineItem(
        "Repayment complete",
        "01 Mar 2018",
        List(
          "We sent a payment of £5.56 to your repayment bank account:",
          "Name: Account holder",
          "Account number: ****2222",
          "Sort code: 66 77 88."
        )
      ),
      ProgressTimelineItem(
        "Repayment approved",
        "24 Oct 2019",
        List("This will reach your repayment bank account in 3 workings days.")
      ),
      ProgressTimelineItem(
        "Sending for further checks",
        formattedTodayString,
        List(
          "We are making sure we pay you the right amount. You do not need to do anything, but we may contact you if " +
            "we need any further information."
        )
      ),
      ProgressTimelineItem(
        "Checking amount",
        formattedTodayString,
        List("We received your return and are now checking the repayment amount we owe you.")
      )
    )
  }

  "id: 2 , add in INITIAL status (SENT_FOR_RISKING) , #001 status" in {
    setup(rdsp             = 1, periodKey = PeriodKey("#001"), ft = ft_404, status1 = SENT_FOR_RISKING, periodKeyBackend = PeriodKey("%23001"))
    InProgress.clickViewProgress()
    ViewProgress.checkAmount("£0.00")
    ViewProgress.checkEstimatedRepaymentDate(6)
    ViewProgress.checkStatusExists(List(SENT_FOR_RISKING, INITIAL))
    ViewProgress.checkStatusNotPresent(List(CLAIM_QUERIED, REPAYMENT_ADJUSTED, ADJUSMENT_TO_TAX_DUE, REPAYMENT_APPROVED))
    ViewProgress.checkMainMessage("Your repayment is being processed")
    ViewProgress.payUrl(expectedValue = false)
    ViewProgress.historyUrl(expectedValue = false)
    ViewProgress.assertWebchatLinkPresent()

    ViewProgress.getProgressTimelineItems shouldBe List(
      ProgressTimelineItem(
        "Sending for further checks",
        formattedTodayString,
        List(
          "We are making sure we pay you the right amount. You do not need to do anything, but we may contact you if " +
            "we need any further information."
        )
      ),
      ProgressTimelineItem(
        "Checking amount",
        formattedTodayString,
        List("We received your return and are now checking the repayment amount we owe you.")
      )
    )
  }

  "display action required messaging if payment is suspended" in {
    setup(rdsp             = 1, periodKey = PeriodKey("#001"), ft = ft_404, status1 = REPAYMENT_SUSPENDED, periodKeyBackend = PeriodKey("%23001"))
    InProgress.clickViewProgress()

    ViewProgress.checkActionRequired(result = true)

    ViewProgress.amount shouldBe None
    ViewProgress.estimatedDate shouldBe None
    ViewProgress.assertWebchatLinkPresent()

    ViewProgress.getProgressTimelineItems shouldBe List(
      ProgressTimelineItem(
        "You must submit your latest VAT return",
        formattedTodayString,
        List(
          "We cannot process your repayment until you submit your VAT return for the last accounting period.\n\n" +
            "Submit your return"
        )
      ),
      ProgressTimelineItem(
        "Checking amount",
        formattedTodayString,
        List("We received your return and are now checking the repayment amount we owe you.")
      )
    )

  }

  "don't display action required messaging if payment is not suspended" in {
    setup(rdsp             = 1, periodKey = PeriodKey("#001"), ft = ft_404, status1 = CLAIM_QUERIED, periodKeyBackend = PeriodKey("%23001"))
    InProgress.clickViewProgress()

    ViewProgress.checkActionRequired(result = false)

    ViewProgress.amount.isDefined shouldBe true
    ViewProgress.estimatedDate.isDefined shouldBe true
    ViewProgress.assertWebchatLinkPresent()

    ViewProgress.getProgressTimelineItems shouldBe List(
      ProgressTimelineItem(
        "Sending for further checks",
        formattedTodayString,
        List(
          "We are making sure we pay you the right amount. You do not need to do anything, but we may contact you if" +
            " we need any further information."
        )
      ),
      ProgressTimelineItem(
        "Checking amount",
        formattedTodayString,
        List("We received your return and are now checking the repayment amount we owe you.")
      )
    )
  }

  // not a good test :-(  You have to look in logfile for 'KNOZ' msgs, remove once issue has been fixed ( OPS-8163 )
  "check zero amount logger" in {
    setup(rdsp      = 4, periodKey = PeriodKey("18AG"), ft = ft_credit, status3 = REPAYMENT_APPROVED)
    InProgress.clickViewProgress()
    ViewProgress.checkAmount("£5.56")
    ViewProgress.checkEstimatedRepeaymentDateNotPresent
    ViewProgress.checkStatusExists(List(REPAYMENT_APPROVED, INITIAL, CLAIM_QUERIED))
    ViewProgress.checkStatusExists(List(REPAYMENT_APPROVED), completed = false)
    ViewProgress.checkStatusNotPresent(List(SENT_FOR_RISKING, REPAYMENT_ADJUSTED, ADJUSMENT_TO_TAX_DUE))
    ViewProgress.checkMainMessage("Your repayment is complete")
    ViewProgress.payUrl(expectedValue = false)
    ViewProgress.historyUrl(expectedValue = true)
    ViewProgress.assertWebchatLinkPresent()

    ViewProgress.getProgressTimelineItems shouldBe List(
      ProgressTimelineItem(
        "Repayment complete",
        "01 Mar 2018",
        List(
          "We sent a payment of £5.56 to your repayment bank account:",
          "Name: Account holder",
          "Account number: ****2222",
          "Sort code: 66 77 88."
        )
      ),
      ProgressTimelineItem(
        "Repayment approved",
        "24 Oct 2019",
        List("This will reach your repayment bank account in 3 workings days.")
      ),
      ProgressTimelineItem(
        "Sending for further checks",
        formattedTodayString,
        List(
          "We are making sure we pay you the right amount. You do not need to do anything, but we may contact you if " +
            "we need any further information."
        )
      ),
      ProgressTimelineItem(
        "Checking amount",
        formattedTodayString,
        List("We received your return and are now checking the repayment amount we owe you.")
      )
    )
  }

  "the lastUpdateReceivedDate should determine the main message and the amount shown" in {
    VatRepaymentTrackerBackendWireMockResponses.storeOk()
    AuditWireMockResponses.auditIsAvailable
    AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
    PaymentsOrchestratorStub.customerDataOkWithBankDetails(vrn)

    val today = LocalDate.now().toString
    val oneDayAgo = LocalDate.now().minusDays(1).toString
    val twoDaysAgo = LocalDate.now().minusDays(2).toString

    PaymentsOrchestratorStub.repaymentDetailS3(vrn, twoDaysAgo, INITIAL, today, REPAYMENT_APPROVED, oneDayAgo, SENT_FOR_RISKING)
    VatRepaymentTrackerBackendWireMockResponses.repaymentDetailS3(vrn, twoDaysAgo, INITIAL, today, REPAYMENT_APPROVED, oneDayAgo, SENT_FOR_RISKING, PeriodKey("18AG"))

    PaymentsOrchestratorStub.financialsNotFound(vrn)

    login()
    goToViaPath(path)

    InProgress.clickViewProgress()

    ViewProgress.checkMainMessage("Your repayment has been approved")
    ViewProgress.checkAmount("£4.56")
    ViewProgress.assertWebchatLinkPresent()

    ViewProgress.getProgressTimelineItems shouldBe List(
      ProgressTimelineItem(
        "Repayment approved",
        formattedTodayString,
        List("This will reach your repayment bank account in 3 workings days.")
      ),
      ProgressTimelineItem(
        "Sending for further checks",
        formatDayShortMonthYear(LocalDate.now().minusDays(1)),
        List(
          "We are making sure we pay you the right amount. You do not need to do anything, but we may contact you " +
            "if we need any further information."
        )
      ),
      ProgressTimelineItem(
        "Checking amount",
        formatDayShortMonthYear(LocalDate.now().minusDays(2)),
        List("We received your return and are now checking the repayment amount we owe you.")
      )
    )
  }

  private def setup(
      useBankDetails:   Boolean       = true,
      inPast:           Boolean       = false,
      status1:          RiskingStatus = INITIAL,
      status2:          RiskingStatus = CLAIM_QUERIED,
      status3:          RiskingStatus = REPAYMENT_ADJUSTED,
      rdsp:             Int,
      periodKey:        PeriodKey,
      ft:               Int,
      periodKeyBackend: PeriodKey     = PeriodKey("18AG")
  ): Unit = {
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
        PaymentsOrchestratorStub.repaymentDetailS1(vrn, date, status1, periodKey)
        VatRepaymentTrackerBackendWireMockResponses.repaymentDetailS1(vrn, date, status1, periodKeyBackend)
      case 2 =>
        PaymentsOrchestratorStub.repaymentDetailS2(vrn, date, status1, status2)
        VatRepaymentTrackerBackendWireMockResponses.repaymentDetailS2(vrn, date, status1, status2, periodKey)
      case 3 =>
        PaymentsOrchestratorStub.repaymentDetailS3(vrn, date, status1, status2, status3)
        VatRepaymentTrackerBackendWireMockResponses.repaymentDetailS3(vrn, date, status1, status2, status3, periodKey)
      case 4 =>
        PaymentsOrchestratorStub.repaymentDetailS3(vrn, date, status1, status2, status3)
        VatRepaymentTrackerBackendWireMockResponses.repaymentDetailS4(vrn, date, status1, status2, status3, periodKey)
    }

    ft match {
      case `ft_404`              => PaymentsOrchestratorStub.financialsNotFound(vrn)
      case `ft_credit`           => PaymentsOrchestratorStub.financialsOkCredit(vrn)
      case `ft_noClearingDate`   => PaymentsOrchestratorStub.financialsOkCreditNoClearingDate(vrn)
      case `ft_twoClearingDates` => PaymentsOrchestratorStub.financialsOkCreditTwoClearingDates(vrn)
      case `ft_emptyItemsArray`  => PaymentsOrchestratorStub.financialsOkCreditEmptyItemsArray(vrn)
      case `ft_debit`            => PaymentsOrchestratorStub.financialsOkDebit(vrn)
      case other                 => throw new IllegalArgumentException(s"no ft match for $other")
    }

    login()
    goToViaPath(path)
  }
}
