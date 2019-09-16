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

import model.des.{AccountHolderName, BankAccountNumber, SortCode}
import org.openqa.selenium.WebDriver
import org.scalatest.Assertion
import pages.OneRepayment.readTitle

object ViewRepaymentAccount extends CommonPage {

  val path = "/vat-repayment-tracker-frontend/view-repayment-account/"

  def readTitle()(implicit webDriver: WebDriver): String = webDriver.getTitle

  def assertPageIsDisplayed(accountHolderName: AccountHolderName, bankAccountNumber: BankAccountNumber, sortCode: SortCode)(implicit wd: WebDriver): Assertion = {
    currentPath shouldBe s"""${path}${accountHolderName.value}/${bankAccountNumber.value}/${sortCode.value}"""
    readTitle shouldBe "Track your VAT repayments"
    readAccName shouldBe "Name on account: *********"
    readAccNumber shouldBe "Account number: ****2490"
    readAccSortCode() shouldBe "Sort code: 40****"
    readMainMessage() shouldBe "Your VAT repayments will be sent to this account"
  }

}
