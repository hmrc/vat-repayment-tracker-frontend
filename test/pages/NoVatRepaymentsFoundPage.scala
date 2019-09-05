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

import org.openqa.selenium.WebDriver
import org.scalatest.Assertion
import org.openqa.selenium.By
import pages.OnePayment.{probing, readAccName, readAccNumber, readAccSortCode}

object NoVatRepaymentsFoundPage extends CommonPage {

  val path = "/vat-repayment-tracker-frontend/show-results/vrn/"

  def readTitle()(implicit webDriver: WebDriver): String = webDriver.getTitle

  def readMainMessage()(implicit webDriver: WebDriver): String = probing(_.findElement(By.id("main-message")).getText)

  def readAccName()(implicit webDriver: WebDriver): String = probing(_.findElement(By.id("acc-name")).getText)

  def readAccSortCode()(implicit webDriver: WebDriver): String = probing(_.findElement(By.id("acc-sort-code")).getText)

  def readAccNumber()(implicit webDriver: WebDriver): String = probing(_.findElement(By.id("acc-number")).getText)

  def assertPageIsDisplayed(vrn: String)(implicit wd: WebDriver): Assertion = {
    currentPath shouldBe s"""${path}${vrn}"""
    readTitle shouldBe "Vat Repayment Tracker"
    readMainMessage shouldBe "No VAT repayments in progress"
    readAccName shouldBe "Name on account: *********"
    readAccSortCode shouldBe "Sort code: ****2490"
    readAccNumber shouldBe "Account number: 40****"
  }
}
