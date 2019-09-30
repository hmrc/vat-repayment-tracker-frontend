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

import org.openqa.selenium.{By, WebDriver}
import org.scalatest.Assertion

object OneRepayment extends CommonDetail {

  def uniqueToPage(implicit wd: WebDriver): Assertion = {
    readTitle shouldBe "We are processing your VAT repayment"
    readMainMessage shouldBe "We are processing your VAT repayment"
  }

  def checkGuidance(implicit wd: WebDriver): Assertion = {
    whenpay shouldBe "When we will repay you"
    whenpay_desc shouldBe "We will ususally repay you before the estimated repayment date, but it may take longer. You do not need to contact us before this date."
  }

  def whenpay(implicit webDriver: WebDriver): String = probing(_.findElement(By.id("whenpay")).getText)
  def whenpay_desc(implicit webDriver: WebDriver): String = probing(_.findElement(By.id("whenpay-desc")).getText)
}
