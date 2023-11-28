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

package pages.classic

import org.openqa.selenium.{By, WebDriver}
import org.scalatest.Assertion
import pages.CommonPage

object VatRepaymentsClassic extends CommonPage {

  val path = "/vat-repayment-tracker/show-vrt"

  def assertPageIsDisplayed()(implicit wd: WebDriver): Assertion = {
    currentPath shouldBe path
    readTitle shouldBe "Your VAT repayments - Business tax account - GOV.UK"
    readMainMessage shouldBe "Your VAT repayments"
  }

  def afterAddress404AssertPageIsDisplayed()(implicit wd: WebDriver): Assertion = {
    currentPath shouldBe path
    readTitle shouldBe "Sorry, there is a problem with the service - Business tax account - GOV.UK"
  }

  def readAddress(implicit webDriver: WebDriver): String = probing(_.findElement(By.id("address")).getText)

  def readReceivedOnDate(implicit webDriver: WebDriver): String = probing(_.findElement(By.id("receivedOn")).getText)

}

