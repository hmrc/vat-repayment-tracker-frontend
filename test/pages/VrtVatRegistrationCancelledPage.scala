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

object VrtVatRegistrationCancelledPage extends CommonPage {

  val path: String = "/vat-repayment-tracker/vrt-vat-registration-cancelled"

  def assertPageIsDisplayed(implicit wd: WebDriver): Unit = {
    currentPath shouldBe path
    readTitle shouldBe "You cannot use this service - Business tax account - GOV.UK"
    readMainMessage shouldBe "You cannot use this service"
    assertContentMatchesExpectedLines(Expected.mainText)
  }

  object Expected {
    val mainText: List[String] = List(
      "You cannot use this service",
      "You cannot use the track your VAT repayments service because your VAT registration has been cancelled.",
      "Call us on 0300 200 3835 if you cannot track VAT repayments online.",
      "Our opening times are Monday to Friday, 8am to 6pm. We are closed on weekends and bank holidays.",
      "If you need extra support",
      "Find out the different ways to deal with HMRC if you need some help.",
      "You can also use Relay UK if you cannot hear or speak on the phone: dial 18001 then 0345 300 3900.",
      "If you are outside the UK: +44 2890 538 192",
      "Before you call, make sure you have:",
      "your VAT registration number. This is 9 numbers, for example, 123456789",
      "your bank details"
    )
  }
}
