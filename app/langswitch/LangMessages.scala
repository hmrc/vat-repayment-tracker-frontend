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
import format.DesFormatter

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

  val `VAT Return period`: Message = Message(
    english = "VAT Return period"
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

  val `Change`: Message = Message(
    english = "Change"
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

  val `Completed` = Message("Completed")

  val `No completed repayments content here` = Message("No completed repayments content here")

  val `No in progress repayments content here` = Message("No in progress repayments content here")

}
