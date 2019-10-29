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
import play.api.Logger

trait CommonDetail extends CommonPage {

  val path = "/vat-repayment-tracker-frontend/show-results/vrn/"

  def clickManageAccount()(implicit driver: WebDriver): Unit = probing(_.findElement(By.id("manage-account")).click())

  def clickInProgress()(implicit driver: WebDriver): Unit = probing(_.findElement(By.id("tab_inProgress")).click())

  def clickCompleted()(implicit driver: WebDriver): Unit = probing(_.findElement(By.id("tab_completed")).click())

  def assertPageIsDisplayed(
      vrn:            Vrn,
      checkBank:      Boolean = true,
      checkAddress:   Boolean = false,
      amount:         String,
      partialAccount: Boolean = false,
      appender:       String)
    (implicit wd: WebDriver): Assertion =
    {
      currentPath shouldBe s"""${path}${vrn.value}"""
      readAmount(appender) shouldBe amount
      if (checkBank) {
        if (partialAccount)
          readAccName shouldBe "Name on account:"
        else
          readAccName shouldBe "Name on account: Account holder"

        readAccNumber shouldBe "Account number: ****2222"
        readAccSortCode shouldBe "Sort code: 66 77 88"
      }
      if (checkAddress) {
        Logger.error(readAddress())
        readAddress shouldBe "VAT PPOB Line1\nVAT PPOB Line2\nVAT PPOB Line3\nVAT PPOB Line4\nTF3 4ER"
      }
      readPeriod(appender) shouldBe "1 July to 30 September 2018"

    }

  def readAddress()(implicit webDriver: WebDriver): String = probing(_.findElement(By.id("address")).getText)

  def readAmount(appender: String)(implicit webDriver: WebDriver): String = probing(_.findElement(By.id(s"amount${appender}")).getText)

  def readReceivedDate(appender: String)(implicit webDriver: WebDriver): String = probing(_.findElement(By.id(s"received-date${appender}")).getText)

  def readPeriod(appender: String)(implicit webDriver: WebDriver): String = probing(_.findElement(By.id(s"period${appender}")).getText)
}
