/*
 * Copyright 2023 HM Revenue & Customs
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

import org.openqa.selenium.{By, WebDriver}
import org.scalatest.Assertion

import scala.jdk.CollectionConverters.CollectionHasAsScala

trait CommonDetail extends CommonPage {

  val path = "/vat-repayment-tracker/show-vrt"

  def breadCrumbsExists(implicit driver: WebDriver): Assertion = idPresent("viewVatAccount") shouldBe true

  def clickManageAccount(implicit driver: WebDriver): Unit = probing(_.findElement(By.id("manage-account")).click())

  def clickCallBac(implicit driver: WebDriver): Unit = probing(_.findElement(By.id("call-bac")).click())

  def clickInProgress(implicit driver: WebDriver): Unit = probing(_.findElement(By.id("tab_inProgress")).click())

  def clickCompleted(implicit driver: WebDriver): Unit = probing(_.findElement(By.id("tab_completed")).click())

  def assertPageIsDisplayed(
      checkBank:      Boolean = true,
      checkAddress:   Boolean = false,
      amount:         String,
      partialAccount: Boolean = false,
      period:         String  = "1 July to 31 July 2018")
    (implicit wd: WebDriver): Assertion =
    {
      currentPath shouldBe path
      readAmount() shouldBe amount
      if (checkBank) {
        if (partialAccount)
          readAccName shouldBe "Name on account:"
        else
          readAccName shouldBe "Name on account: Account holder"

        readAccNumber shouldBe "Account number: ****2222"
        readAccSortCode shouldBe "Sort code: 66 77 88"
      }
      if (checkAddress) {
        readAddress shouldBe "VAT PPOB Line1\nVAT PPOB Line2\nVAT PPOB Line3\nVAT PPOB Line4\nTF3 4ER"
      }
      readPeriod() shouldBe period

    }

  def readAddress(implicit webDriver: WebDriver): String = probing(_.findElement(By.id("address")).getText)

  def readAmount()(implicit webDriver: WebDriver): String =
    probing(_.findElement(By.xpath(s"/html/body/div/main/div/article/div[3]/div/div/section/table/tbody/tr[1]/td[3]")).getText)

  def readReceivedDate(appender: String)(implicit webDriver: WebDriver): String = probing(_.findElement(By.id(s"received-date$appender")).getText)

  def readPeriod()(implicit webDriver: WebDriver): String = probing(_.findElement(By.xpath(s"/html/body/div/main/div/article/div[3]/div/div/section[1]/table/tbody/tr[1]/td[2]")).getText)

  def readRowForPeriod(periodKey: String)(implicit webDriver: WebDriver): Seq[String] =
    probing(
      _.findElement(By.cssSelector(s"""tr > td > a.govuk-link[href="/vat-repayment-tracker/view-progress/${periodKey}"]"""))
        .findElement(By.xpath("./../.."))
    ).findElements(By.cssSelector("td")).asScala.toSeq.map(_.getText)

}
