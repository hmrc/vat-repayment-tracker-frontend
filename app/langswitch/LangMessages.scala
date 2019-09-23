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

  val heading: Message = Message(
    english = "Track your VAT repayments"
  )

  val `We are processing your VAT repayment` = Message(
    english = "We are processing your VAT repayment"
  )

  val `We are processing your VAT repayments` = Message(
    english = "We are processing your VAT repayments"
  )

  val `Amount claimed`: Message = Message(
    english = "Amount claimed"
  )

  val `Estimated repayment date`: Message = Message(
    english = "Estimated repayment date"
  )

  val `VAT Return received on`: Message = Message(
    english = "VAT Return received on"
  )

  val `VAT Return period`: Message = Message(
    english = "VAT Return period"
  )

  val `When we will repay you`: Message = Message(
    english = "When we will repay you"
  )

  val `When we will repay you description`: Message = Message(
    english = "We will ususally repay you before the estimated repayment date, but it may take longer.  You do not need to contact us before this date."
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

  val `Manage your repayment account`: Message = Message(
    english = "Manage your repayment account"
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

  val `Change account details`: Message = Message(
    english = "Change account details"
  )

  val `Your repayment is delayed`: Message = Message(
    english = "Your repayment is delayed"
  )

  val `Your repayments are delayed`: Message = Message(
    english = "Your repayments are delayed"
  )

  val `What happens next`: Message = Message(
    english = "What happens next"
  )

  val `What happens next description`: Message = Message(
    english = "You do not need to do anything right now.  We are working on paying you as soon as possible.  If you need to speak to someone about your repayment, you can "
  )

  val `Contact HMRC`: Message = Message(
    english = "Contact HMRC."
  )

  val `Delayed`: Message = Message(
    english = "Delayed"
  )

  val `In progress`: Message = Message(
    english = "In progress"
  )

  val `Currently sent to`: Message = Message(
    english = "Your cheque is currently send to:"
  )

  val `For faster payment next time`: Message = Message(
    english = "For faster payment next time"
  )

  val `add a repayment account`: Message = Message(
    english = "add a repayment account"
  )

  val `Update your correspondence address`: Message = Message(
    english = "Update your correspondence address"
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
  val `Manage your repayment bank account` = Message(
    "Manage your repayment bank account"
  )

  def accountAndSortCode(bankDetails: BankDetails) = Message(
    s"""Account: ${bankDetails.bankAccountNumber.bankAccounNumberObscured}, Sort code: ${bankDetails.sortCode.value}"""
  )

  val `Manage your Direct Debit` = Message(
    "Manage your Direct Debit"
  )

}
