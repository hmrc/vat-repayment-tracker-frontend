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

import org.openqa.selenium.WebDriver
import org.scalatest.Assertion

object InProgressCompleted extends CommonDetail {

  def containsBAC(result: Boolean)(implicit wd: WebDriver): Assertion = {
    containsText("Add your bank account for future repayments") shouldBe result
    containsText("The quickest way to receive a repayment is straight into your bank account.") shouldBe result
    containsText("Add your bank account details for any future repayments.") shouldBe result
    containsText("Add bank details") shouldBe result
  }

  def containsBankWarning(result: Boolean)(implicit wd: WebDriver): Assertion = {
    containsText("You have recently updated your bank account details.") shouldBe result
  }

  def containsBankDetails(result: Boolean)(implicit wd: WebDriver): Assertion = {
    containsText("You are currently paid by bank transfer to the following account:") shouldBe result
  }

  def containsNewBankDetailsText(result: Boolean)(implicit wd: WebDriver): Assertion = {
    containsText("You’ll continue to receive repayments by cheque until we verify your bank account details.") shouldBe result
  }

  def checktabs(implicit wd: WebDriver): Assertion = {

    idPresent("completed-exist") shouldBe true
    idPresent("inprogress-exist") shouldBe true
    idPresent("completed-none") shouldBe false
    idPresent("inprogress-none") shouldBe false

  }

  def checktabsInPast(implicit wd: WebDriver): Assertion = {

    idPresent("completed-exist") shouldBe false
    idPresent("inprogress-exist") shouldBe true
    idPresent("completed-none") shouldBe true
    idPresent("inprogress-none") shouldBe false

  }

}
