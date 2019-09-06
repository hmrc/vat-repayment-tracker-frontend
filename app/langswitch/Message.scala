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

import javax.inject.{Inject, Singleton}

import scala.language.implicitConversions

case class Message(
    english: String,
    welsh:   Option[String]
) {

}

object Message {

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def apply(english: String, welsh: String = null): Message = Message(english, Option(welsh))

  implicit def show(message: Message)(implicit lang: Language): String = lang match {
    case Languages.English => message.english
    case Languages.Welsh   => message.welsh.getOrElse(message.english)
  }
}

object MessagesA {

  val heading: Message = Message("Vat Repayment Tracker")
  val `We are processing your VAT repayment` = Message("We are processing your VAT repayment")
  val `Amount claimed`: Message = Message("Amount claimed")
  val `Estimated repayment date`: Message = Message("Estimated repayment date")
  val `VAT Return received on`: Message = Message("VAT Return received on")
  val `VAT Return period`: Message = Message("VAT Return period")
  val `When we will repay you`: Message = Message("When we will repay you")
  val `When we will repay you description`: Message = Message("We will ususally repay you before the estimated repayment date, but it may take longer.  You do not need to contact us before this date.")
  val `You do not have access to this service`: Message = Message("You do not have access to this service")
  val `No VAT repayments in progress`: Message = Message("No VAT repayments in progress")
  val `Repayments usually take 24 hours`: Message = Message("Repayments usually take around 24 hours to show on this page after submitting your VAT Return.")
  val `Check this page when you are next expecting a repayment.`: Message = Message("Check this page when you are next expecting a repayment.")
  val `View your VAT account`: Message = Message("View your VAT account")
  val `You are currently paid by bank transfer`: Message = Message("You are currently paid by bank transfer to the following account:")
  val `Name on account`: Message = Message("Name on account:")
  val `Sort code`: Message = Message("Sort code:")
  val `Account number`: Message = Message("Account number:")
  val `Manage your repayment account`: Message = Message("Manage your repayment account")
  val `Your repayment details`: Message = Message("Your repayment details")

}

class OnePayment {

  class View(implicit lang: Language = Languages.English) {
    val heading: String = MessagesA.heading
    val message: String = MessagesA.`We are processing your VAT repayment`
    val amountClaimed: String = MessagesA.`Amount claimed`
    val estRepaymentDate: String = MessagesA.`Estimated repayment date`
    val dateReceived: String = MessagesA.`VAT Return received on`
    val period: String = MessagesA.`VAT Return period`
    val when: String = MessagesA.`When we will repay you`
    val whenDesc: String = MessagesA.`When we will repay you description`
    val manageLink: String = MessagesA.`Manage your repayment account`
    val repaymentDetails: String = MessagesA.`Your repayment details`
    val currentlyPaidBy: String = MessagesA.`You are currently paid by bank transfer`
    val accName: String = MessagesA.`Name on account`
    val accNo: String = MessagesA.`Account number`
    val sortCode: String = MessagesA.`Sort code`

  }

}
