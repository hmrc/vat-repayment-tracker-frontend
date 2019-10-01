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

package pages

import model.Vrn
import org.openqa.selenium.{By, WebDriver}
import org.scalatest.Assertion
import pages.ManageOrTrack.probing

/**
 * <td id="amount">@onePayment.formatDate(onePayment.returnedReceivedOn)</td>
 * <td id="repay-date">@onePayment.period</td>
 * <td id="received-date">£@onePayment.formatAmount</td>
 * <td id="period">@onePayment.formatDate(onePayment.estimatedRepaymentDate)</td>
 *
 */

object OneDelayedOneInProgress extends CommonPage {

  val path = "/vat-repayment-tracker-frontend/show-results/vrn/"

  def assertPageIsDisplayed(vrn: Vrn)(implicit wd: WebDriver): Assertion = {
    currentPath shouldBe s"""${path}${vrn.value}"""
    readMainMessage() shouldBe "We are processing your VAT repayments"
  }

  def readTitle()(implicit webDriver: WebDriver): String = webDriver.getTitle

  def clickManageAccount()(implicit driver: WebDriver): Unit = probing(_.findElement(By.id("manage-account")).click())

  def clickDelayed()(implicit driver: WebDriver): Unit = probing(_.findElement(By.id("tab_delayed")).click())

  def clickInProgress()(implicit driver: WebDriver): Unit = probing(_.findElement(By.id("tab_inProgress")).click())

  def amount()(implicit webDriver: WebDriver): String = probing(_.findElement(By.id("amount")).getText)

  def repayDate()(implicit webDriver: WebDriver): String = probing(_.findElement(By.id("repay-date")).getText)

  def receivedDate()(implicit webDriver: WebDriver): String = probing(_.findElement(By.id("received-date")).getText)

  def period()(implicit webDriver: WebDriver): String = probing(_.findElement(By.id("period")).getText)

}
