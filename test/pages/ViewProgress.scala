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

package pages

import model.des.RiskingStatus

import java.time.LocalDate
import org.openqa.selenium.{By, WebDriver}
import org.scalatest.Assertion
import pages.InProgress.probing

import scala.util.Try

object ViewProgress extends CommonPage {

  def amount(implicit webDriver: WebDriver): Option[String] = Try {
    probing(_.findElement(By.id(s"amount")).getText)
  }.toOption

  def estimatedDate(implicit webDriver: WebDriver): Option[String] = Try {
    probing(_.findElement(By.id(s"repay-date")).getText)
  }.toOption

  def actionRequired()(implicit webDriver: WebDriver): String = Try {
    probing(_.findElement(By.id(s"repayment-suspended-action-required")).getText)
  }.getOrElse("")

  def checkActionRequired(result: Boolean)(implicit webDriver: WebDriver): Unit = {
    val div = actionRequired()

    div.contains {
      "Action required"
    } shouldBe result

    div.contains {
      "Submit VAT return"
    } shouldBe result

    div.contains {
      "Submit your return"
    } shouldBe result

    ()
  }

  def checkAmount(amount: String)(implicit webDriver: WebDriver): Assertion = probing(_.findElement(By.id(s"amount")).getText) shouldBe amount

  def checkEstimatedRepaymentDate(daysAdded: Int)(implicit webDriver: WebDriver): Assertion = probing(_.findElement(By.id(s"repay-date")).getText) shouldBe
    formatDate(LocalDate.now().plusDays(30 + daysAdded))

  def checkEstimatedRepeaymentDateNotPresent(implicit webDriver: WebDriver): Assertion = idPresent("repay-date") shouldBe false

  def checkStatusExists(statusList: List[RiskingStatus], completed: Boolean = false)(implicit webDriver: WebDriver): Unit = {
    val completedFrag = if (completed) "_Y" else ""

    statusList foreach (e =>
      idPresent(s"${e}${completedFrag}_timeline") shouldBe true
    )

  }

  def checkStatusNotPresent(statusList: List[RiskingStatus], completed: Boolean = false)(implicit webDriver: WebDriver): Unit = {
    val completedFrag = if (completed) "_Y" else ""

    statusList foreach (e =>
      idPresent(s"${e}${completedFrag}_timeline") shouldBe false
    )

  }

  def checkMainMessage(mainMessage: String)(implicit webDriver: WebDriver): Assertion = readMainMessage shouldBe mainMessage

  def backExists()(implicit driver: WebDriver): Assertion = idPresent("back") shouldBe true

  def historyUrl(expectedValue: Boolean)(implicit driver: WebDriver): Assertion = idPresent("history-url") shouldBe expectedValue

  def payUrl(expectedValue: Boolean)(implicit driver: WebDriver): Assertion = idPresent("pay-url") shouldBe expectedValue

}
