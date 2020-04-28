/*
 * Copyright 2020 HM Revenue & Customs
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

import java.time.LocalDate

import org.openqa.selenium.{By, WebDriver}
import org.scalatest.Assertion

object ViewProgress extends CommonPage {

  def checkAmount(amount: String)(implicit webDriver: WebDriver): Assertion = probing(_.findElement(By.id(s"amount")).getText) shouldBe amount

  def checkEstimatedRepaymentDate(daysAdded: Int)(implicit webDriver: WebDriver): Assertion = probing(_.findElement(By.id(s"repay-date")).getText) shouldBe
    formatDate(LocalDate.now().plusDays(30 + daysAdded))

  def checkEstimatedRepeaymentDateNotPresent(implicit webDriver: WebDriver): Assertion = idPresent("repay-date") shouldBe false

  def checkStatusExists(statusList: List[String])(implicit webDriver: WebDriver): Unit = {

    statusList foreach (e =>
      idPresent(s"${e}_timeline") shouldBe true
    )

  }

  def checkStatusNotPresent(statusList: List[String])(implicit webDriver: WebDriver): Unit = {

    statusList foreach (e =>
      idPresent(s"${e}_timeline") shouldBe false
    )

  }

  def checkMainMessage(mainMessage: String)(implicit webDriver: WebDriver): Assertion = readMainMessage shouldBe mainMessage

  def backExists()(implicit driver: WebDriver): Assertion = idPresent("back") shouldBe true

  def historyUrl(expectedValue: Boolean)(implicit driver: WebDriver): Assertion = idPresent("history-url") shouldBe expectedValue

  def payUrl(expectedValue: Boolean)(implicit driver: WebDriver): Assertion = idPresent("pay-url") shouldBe expectedValue

}
