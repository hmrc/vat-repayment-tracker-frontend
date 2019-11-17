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

object ManageOrTrack extends CommonPage {

  val path = "/vat-repayment-tracker-frontend/manage-or-track/vrn/"

  def assertPageIsDisplayed(
      vrn:             Vrn,
      ddDisplayed:     Boolean = false,
      bankDisplayed:   Boolean = false,
      noddDisplayed:   Boolean = false,
      nobankDisplayed: Boolean = false
  )
    (implicit wd: WebDriver): Assertion =
    {
      currentPath shouldBe s"""${path}${vrn.value}"""
      readTitle shouldBe "Business tax account"
      if (ddDisplayed) dd shouldBe "Manage your Direct Debit"
      if (bankDisplayed) bank shouldBe "Manage your repayment bank account"
      vrt shouldBe "Track your VAT repayments"

      idPresent("dd-label") shouldBe ddDisplayed
      idPresent("bank-label") shouldBe bankDisplayed
      idPresent("nobank-label") shouldBe nobankDisplayed
      idPresent("nodd-label") shouldBe noddDisplayed

      readMainMessage shouldBe "Manage Direct Debit and repayment bank account or track repayments"
    }

  def dd(implicit webDriver: WebDriver): String = probing(_.findElement(By.id("dd-label")).getText)

  def bank(implicit webDriver: WebDriver): String = probing(_.findElement(By.id("bank-label")).getText)

  def vrt(implicit webDriver: WebDriver): String = probing(_.findElement(By.id("vrt-label")).getText)

  def clickVrtLabel()(implicit driver: WebDriver): Unit = probing(_.findElement(By.id("vrt-label")).click())

  def clickBankLabel()(implicit driver: WebDriver): Unit = probing(_.findElement(By.id("bank-label")).click())

}
