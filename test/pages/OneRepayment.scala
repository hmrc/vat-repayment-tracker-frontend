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

object OneRepayment extends CommonPage {

  val path = "/vat-repayment-tracker-frontend/show-results/vrn/"

  def clickManageAccount()(implicit driver: WebDriver): Unit = probing(_.findElement(By.id("manage-account")).click())

  def assertPageIsDisplayed(vrn: Vrn, receivedDate: String, repayDate: String)(implicit wd: WebDriver): Assertion = {
    currentPath shouldBe s"""${path}${vrn.value}"""
    readTitle shouldBe "We are processing your VAT repayment"
    readMainMessage shouldBe "We are processing your VAT repayment"
    readAmount shouldBe "£5.56"
    readRepayDate shouldBe repayDate
    readReceivedDate shouldBe receivedDate
    readPeriod shouldBe "March 2018"
    readAccName shouldBe "Name on account: Account holder"
    readAccNumber shouldBe "Account number: ****2222"
    readAccSortCode() shouldBe "Sort code: 667788"

  }

  def readTitle()(implicit webDriver: WebDriver): String = webDriver.getTitle

  def readAmount()(implicit webDriver: WebDriver): String = probing(_.findElement(By.id("amount")).getText)

  def readRepayDate()(implicit webDriver: WebDriver): String = probing(_.findElement(By.id("repay-date")).getText)

  def readReceivedDate()(implicit webDriver: WebDriver): String = probing(_.findElement(By.id("received-date")).getText)

  def readPeriod()(implicit webDriver: WebDriver): String = probing(_.findElement(By.id("period")).getText)
}
