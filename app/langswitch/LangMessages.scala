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

package langswitch

import model.des.BankDetails

object LangMessages {

  val exapmleMessage: Message = Message(
    english = "Example message",
    welsh   = "Neges enghreifftiol"
  )

  val `We are processing your VAT repayments` = Message(
    english = "We are processing your VAT repayments"
  )

  val `Amount claimed`: Message = Message(
    english = "Amount claimed"
  )

  val `Received on`: Message = Message(
    english = "Received on"
  )

  val `Accounting period`: Message = Message(
    english = "Accounting period"
  )

  val `When we will repay you`: Message = Message(
    english = "When we will repay you"
  )

  val `When we will repay you description`: Message = Message(
    english = "We will usually repay you within 30 days of HMRC receiving your VAT Return. If you need to speak to someone about your repayment, only "
  )
  val `after 30 days have passed.`: Message = Message(
    english = " after 30 days have passed."
  )

  val `No VAT repayments in progress`: Message = Message(
    english = "No VAT repayments in progress"
  )

  val `Repayments usually take 24 hours`: Message = Message(
    english = "Repayments usually take around 24 hours to show on this page after submitting your VAT Return."
  )

  val `Check this page when you are next expecting a repayment.`: Message = Message(
    english = "Check this page when you are next expecting a repayment."
  )

  val `View your VAT account`: Message = Message(
    english = "View your VAT account"
  )

  val `You are currently paid by bank transfer`: Message = Message(
    english = "You are currently paid by bank transfer to the following account:"
  )

  val `Name on account`: Message = Message(
    english = "Name on account:"
  )

  val `Sort code`: Message = Message(
    english = "Sort code:"
  )

  val `Account number`: Message = Message(
    english = "Account number:"
  )

  val `Manage your repayment bank account`: Message = Message(
    english = "Manage your repayment bank account"
  )

  val `Change account details`: Message = Message(
    english = "Change account details"
  )

  val `Your repayment details`: Message = Message(
    english = "Your repayment details"
  )

  val `Track your VAT repayments`: Message = Message(
    english = "Track your VAT repayments"
  )

  val `Sent to this account`: Message = Message(
    english = "Your VAT repayments will be sent to this account"
  )

  val `View your business details to update your repayment bank account`: Message = Message(
    english = "View your business details to update your repayment bank account"
  )

  val `Contact HMRC`: Message = Message(
    english = "contact HMRC"
  )

  val `In progress`: Message = Message(
    english = "In progress"
  )

  val `Currently sent to`: Message = Message(
    english = "We'll send your cheque to:"
  )

  val `For faster payment next time`: Message = Message(
    english = "For faster payment next time"
  )

  val `add a repayment bank account`: Message = Message(
    english = "add a repayment bank account"
  )

  val `Update your business address`: Message = Message(
    english = "Update your business address"
  )

  val `Manage or track`: Message = Message(
    english = "Manage bank accounts or track repayments"
  )

  val heading_bta: Message = Message(
    english = "Business tax account"
  )

  val Continue: Message = Message(
    english = "Continue"
  )
  val `View what HMRC owe you`: Message = Message(
    english = "View what HMRC owe you"
  )
  val `There is a problem` = Message(
    "There is a problem"
  )

  def accountAndSortCode(bankDetails: BankDetails) = Message(
    s"""Account: ${bankDetails.obscureBankAccountNumber}, Sort code: ${bankDetails.formatSortCode}"""
  )

  val `Manage your Direct Debit` = Message(
    "Manage your Direct Debit"
  )

  val `Set up a Direct Debit` = Message(
    "Set up a Direct Debit"
  )

  val `HMRC will automatically` = Message(
    "HMRC will automatically collect your VAT Return payments when due"
  )

  val `Set up a repayment bank account` = Message("Set up a repayment bank account")

  val `Tell HMRC where to pay your money` = Message("Tell HMRC where to pay your money")

  val `View progress` = Message("View progress")

  val `View history` = Message("View history")

  val `Completed` = Message("Completed")

  val `No completed repayments content here` = Message("No completed repayments content here")

  val `No in progress repayments content here` = Message("No in progress repayments content here")

  val `Your repayment is being processed` = Message("Your repayment is being processed")

  val `Your repayment is delayed` = Message("Your repayment is delayed")

  val `VAT Return received on`: Message = Message(
    english = "VAT Return received on"
  )

  val `Estimated repayment date`: Message = Message(
    english = "Estimated repayment date"
  )

  val `What's happened so far` = Message("What's happened so far")

  val `You must now pay some VAT` = Message("You most now pay some VAT")

  val `Your repayment is complete` = Message("Your repayment is complete")

  val `Checking amount` = Message("Checking amount")

  val `We received your return` = Message("We received your return and are now checking the repayment amount we owe you.")

  val `Repayment amount changed` = Message("Repayment amount changed")

  def `You claimed a VAT repayment of`(originalPostingAmount: String, vatToPay_Box5: String, url: String) = Message(s"You claimed £${originalPostingAmount}. We calculated this amount was incorrect so we will repay you £${vatToPay_Box5}.  This will reach your repayment bank account in 3 working days.  We sent you a letter explaining why we changed your amount.  <br><br>If you do not receive a letter in the next few days, check your <a href=url>VAT payments history.</a> We may have used the amount to pay off a VAT charge on your account.")

  val `Sending for further checks` = Message("Sending for further checks")

  val `Repayment approved` = Message("Repayment approved")

  val `VAT payment due` = Message("VAT payment due")

  def `We calculated that the original amount you claimed of`(originalPostingAmount: String, vatToPay_Box5: String) = Message(s"""We calculated that the original amount you claimed of £${originalPostingAmount} was incorrect. You now owe HMRC £${vatToPay_Box5}. If you want to find out why your repayment amount changed <a href="https://www.gov.uk/government/organisations/hm-revenue-customs/contact/vat-enquiries">contact HMRC.</a>""")

  val `Estimated repayment date has passed` = Message("Estimated repayment date has passed")

  val `You do not need to do anything right now` = Message("""You do not need to do anything right now. We are working on paying you as soon as possible. </br>If you need to speak to someone about your repayment, you can <a href="https://www.gov.uk/government/organisations/hm-revenue-customs/contact/vat-enquiries">contact HMRC.</a>""")

  val `Repayment complete` = Message("Repayment complete")

  val `Your repayment has been approved` = Message("Your repayment has been approved")

  val `We are making sure we pay you the right amount` = Message("We are making sure we pay you the right amount. You do not need to do anything, but we may contact you if we need any further information.")

  val period_1 = Message("1 January to 31 March")
  val period_2 = Message("1 April to 30 June")
  val period_3 = Message("1 July to 30 September")
  val period_4 = Message("1 October to 31 December")

  val January = Message("1 January to 31 January")
  val February = Message("1 February to 28 February")
  val February_leap = Message("1 February to 29 February")
  val March = Message("1 March to 31 March")
  val April = Message("1 April to 30 April")
  val May = Message("1 May to 31 May")
  val June = Message("1 June to 30 June")
  val July = Message("1 July to 31 July")
  val August = Message("1 August to 31 August")
  val September = Message("1 September to 30 September")
  val October = Message("1 October to 31 October")
  val November = Message("1 November to 30 November")
  val December = Message("1 December to 31 December")

  val JanToDec = Message("January to December")
  val FebToJan = Message("February to January")
  val MarToFeb = Message("March to February")
  val AprToMar = Message("April to March")
  val MayToApr = Message("May to April")
  val JunToMay = Message("June to May")
  val JulToJun = Message("July to June")
  val AugToJul = Message("August to July")
  val SepToAug = Message("September to August")
  val OctToSep = Message("October to September")
  val NovToOct = Message("November to October")
  val DecToNov = Message("December to November")

  val JanuaryQuarter = Message("1 November to 31 January")
  val FebruaryQuarter = Message("1 December to 28 February")
  val FebruaryQuarter_leap = Message("1 December to 29 February")
  val MarchQuarter = Message("1 January to 31 March")
  val AprilQuarter = Message("1 February to 30 April")
  val MayQuarter = Message("1 March to 31 May")
  val JuneQuarter = Message("1 April to 30 June")
  val JulyQuarter = Message("1 May to 31 July")
  val AugustQuarter = Message("1 June to 31 August")
  val SeptemberQuarter = Message("1 July to 30 September")
  val OctoberQuarter = Message("1 August to 31 October")
  val NovemberQuarter = Message("1 September to 30 November")
  val DecemberQuarter = Message("1 October to 31 December")

  val `We will send this to your repayment bank account` = Message("This will reach your repayment bank account in 3 workings days.")

  def `We will send a cheque to your business address`(url: String) = Message("We will send a cheque to your business address. This will reach you in 5 to 6 working days. We sent you a letter explaining why we changed your amount.  <br><br>If you do not receive a letter in the next few days, check your <a href=url>VAT payments history.</a> We may have used the amount to pay off a VAT charge on your account.")

  def `repayment-complete-bank-details`(name: String, number: String, sortCode: String, amount: String) =
    Message(s"""We sent a payment of £${amount} to your repayment bank account:</br>Name: ${name}</br>Account number: ${number}</br>Sort code: ${sortCode}.""")

  def `repayment-complete-address`(address: String, amount: String) = Message(s"""We sent a payment of £${amount} as a cheque to </br> ${address}.""")

  def `repayment-complete-bank-details-adjusted`(name: String, number: String, sortCode: String, amount: String) =
    Message(s"""We sent an adjusted payment of £${amount} to your repayment bank account:</br>Name: ${name}</br>Account number: ${number}</br>Sort code: ${sortCode}.""")

  def `repayment-complete-address-adjusted`(address: String, amount: String) = Message(s"""We sent you an adjusted payment of £${amount} as a cheque to </br> ${address}.""")

  val addressNotAvailable = Message("Address not available")

  val `You now owe HMRC` = Message("You now owe HMRC")

  val `We received your VAT payment` = Message(s"""We received your VAT payment.""")

  val `Amount you claimed` = Message("Amount you claimed")

  val `Amount we'll pay you` = Message("Amount we'll pay you")

  val `Amount to pay` = Message("Amount to pay")

  val `Amount we paid you` = Message("Amount we paid you")

  val `Amount you paid` = Message("Amount you paid")

  val `You need to make a VAT payment` = Message("You need to make a VAT payment")

  val `You have no completed repayments` = Message("You have no completed repayments")

  val `No repayments in progress` = Message("No repayments in progress")

  val `no-in-progress-desc` = Message("""If you are expecting a repayment and it is not showing here, check this page again in 24 hours.</br></br>If you submitted your return early check this page after your accounting period has ended.""")

  def `no-completed-desc`(url: String) = Message(s"""Completed repayments will only show here for 60 days after your repayment date. View your <a href="${url}">VAT payments history.</a>""")

  val `Your VAT account` = Message("Your VAT account")

  val Back = Message("Back")

  val `View payments history` = Message("View payments history")

  val `Pay now` = Message("Pay now")

}
