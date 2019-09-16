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

import java.time.LocalDate

import model.Vrn
import org.openqa.selenium.WebDriver
import org.scalatest.Assertion
import org.openqa.selenium.By
import pages.NoVatRepaymentsFoundPage.readTitle

object OneRepayment extends CommonPage {

  val path = "/vat-repayment-tracker-frontend/show-results/vrn/"

  def readTitle()(implicit webDriver: WebDriver): String = webDriver.getTitle

  def readAmount()(implicit webDriver: WebDriver): String = probing(_.findElement(By.id("amount")).getText)

  def readRepayDate()(implicit webDriver: WebDriver): String = probing(_.findElement(By.id("repay-date")).getText)

  def readReceivedDate()(implicit webDriver: WebDriver): String = probing(_.findElement(By.id("received-date")).getText)

  def readPeriod()(implicit webDriver: WebDriver): String = probing(_.findElement(By.id("period")).getText)

  def clickManageAccount()(implicit driver: WebDriver): Unit = probing(_.findElement(By.id("manage-account")).click())

  def assertPageIsDisplayed(vrn: Vrn)(implicit wd: WebDriver): Assertion = {
    currentPath shouldBe s"""${path}${vrn.value}"""
    readTitle shouldBe "Track your VAT repayments"
    readMainMessage shouldBe "We are processing your VAT repayment"
    readAmount shouldBe "£5.56"
    readRepayDate shouldBe "02 Dec 2027"
    readReceivedDate shouldBe "02 Nov 2027"
    readPeriod shouldBe "March 2018"
    readAccName shouldBe "Name on account: *********"
    readAccNumber shouldBe "Account number: ****2490"
    readAccSortCode() shouldBe "Sort code: 40****"

  }
}
