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

object ViewRepaymentAccount extends CommonPage {

  def assertPageIsDisplayed(heading: String)(implicit wd: WebDriver): Assertion = {
    currentPath shouldBe s"""/vat-repayment-tracker/view-repayment-account"""

    if (heading === "Mae’r manylion cyfrif banc ar gyfer eich ad-daliadau TAW yn cael eu diweddaru") {
      readTitle shouldBe s"""$heading - Cyfrif treth busnes - GOV.UK"""
    } else {
      readTitle shouldBe s"""$heading - Business tax account - GOV.UK"""
    }

    readMainMessage shouldBe s"""$heading"""

    if (heading === "The bank account details for your VAT repayments are being updated") {
      readWarning should include("The updated bank account details will appear in your tax account by 13 April 2019, but may appear sooner.")
    } else if (heading === "Mae’r manylion cyfrif banc ar gyfer eich ad-daliadau TAW yn cael eu diweddaru") {
      readWarning should include("Bydd y manylion banc sydd wedi’u diweddaru yn ymddangos yn eich cyfrif treth erbyn 13 Ebrill 2019, ond mae’n bosibl y byddant yn ymddangos yn gynt.")
    }

    readAccName should include("Account holder")
    readAccNumber should include ("****2222")
    readAccSortCode should include ("66 77 88")
  }

}
