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

object Completed extends CommonDetail {

  def uniqueToPage(implicit wd: WebDriver): Assertion = {
    readTitle shouldBe "We are processing your VAT repayments"
    readMainMessage shouldBe "We are processing your VAT repayments"
  }

  def noRepayments(implicit webDriver: WebDriver): String = probing(_.findElement(By.id("no-repayments")).getText)

  def viewProgressLink(implicit wd: WebDriver): Assertion = {
    clickInProgress
    noRepayments shouldBe "No in progress repayments content here"
  }

  def checktabs(implicit wd: WebDriver): Assertion = {

    idPresent("completed-exist") shouldBe true
    idPresent("inprogress-exist") shouldBe false
    idPresent("completed-none") shouldBe false
    idPresent("inprogress-none") shouldBe true

  }

}
