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
import org.openqa.selenium.WebDriver
import org.scalatest.Assertion

object ViewRepaymentAccount extends CommonPage {

  val path = "/vat-repayment-tracker-frontend/view-repayment-account"

  def assertPageIsDisplayed(vrn: Vrn)(implicit wd: WebDriver): Assertion = {
    currentPath shouldBe s"""${path}"""
    readTitle shouldBe "Business tax account"
    //readAccName() shouldBe s"""Account number: ${accountHolderName}"""
    readAccNumber should include ("****2222")
    readAccSortCode() should include ("66 77 88")
    readMainMessage() shouldBe "Your VAT repayments will be sent to this account"
  }

}
