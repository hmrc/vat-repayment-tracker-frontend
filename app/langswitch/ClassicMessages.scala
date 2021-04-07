/*
 * Copyright 2021 HM Revenue & Customs
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

object ClassicMessages {
  val no_returns_line1: Message = Message(english = "If you are expecting a repayment and it is not showing here, this may be because:")
  val no_returns_line2: Message = Message(english = "repayments can take up to 24 hours to show on this page after you submit your return")
  val no_returns_line3: Message = Message(english = "you submitted your return before the end of the VAT accounting period")
  val no_returns_line4: Message = Message(english = "if we recently paid you, it can take up to a week for you to receive it")
  val your_vat_repayments: Message = Message(english = "Your VAT repayments")
  val returns_line1: Message = Message(english = "Return received on:")
  val returns_line2: Message = Message(english = "We will usually repay you within 30 days of receiving your return.")
  val returns_line3: Message = Message(english = " You do not need to contact us before this.")
  val returns_line4: Message = Message(english = "Your repayment will not show on this page once it has been completed and approved.  It can take up to a week for you to receive the repayment after it has been approved.")

  val bank_details: Message = Message(english = "If you have added your bank details we will send repayments to your bank account.")
  def bank_account_link(url: String): Message = Message(s"""<a href="$url">Add or change your bank account details</a>""")
  val address_details: Message = Message(english = "If you have not added your bank details, we will send you a cheque to:")
  def address_link(url: String): Message = Message(s"""<a href="$url">Change your business address</a>""")
}
