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

import model.Language
import model.Language._
import org.openqa.selenium.WebDriver
import org.scalatest.Assertion

object VrtVatRegistrationCancelledPage extends CommonPage {

  val path: String = "/vat-repayment-tracker/vrt-vat-registration-cancelled"
  val backButtonUrl: String = ""

  def expectedTitle(language: Language): String = language match {
    case English => "Call us to track your VAT repayments - Business tax account - GOV.UK"
    case Welsh   => "Ffoniwch ni i ddilyn hynt eich ad-daliadau TAW - Cyfrif treth busnes - GOV.UK"
  }

  def expectedMainMessage(language: Language): String = language match {
    case English => "Call us to track your VAT repayments"
    case Welsh   => "Ffoniwch ni i ddilyn hynt eich ad-daliadau TAW"
  }

  def assertPageIsDisplayed(language: Language = English)(implicit wd: WebDriver): Unit = {
    currentPath shouldBe path
    readTitle shouldBe expectedTitle(language)
    readMainMessage shouldBe expectedMainMessage(language)
    assertContentMatchesExpectedLines(Expected.MainText()(language))
  }

  def assertHyperLinkedTextDisplayed(language: Language = English)(implicit wd: WebDriver): Assertion = {
    hasTextHyperLinkedTo(
      language match {
        case English => "deal with HMRC if you need some help"
        case Welsh   => "ddelio â CThEF os oes angen help arnoch chi"
      },
      "https://www.gov.uk/get-help-hmrc-extra-support"
    )
    hasTextHyperLinkedTo(
      "Relay UK",
      "https://www.relayuk.bt.com/"
    )
  }

  object Expected {

    object MainText {
      def apply()(implicit language: Language): List[String] = language match {
        case English => mainTextEnglish
        case Welsh   => mainTextWelsh
      }

      val mainTextEnglish: List[String] = List(
        "Call us to track your VAT repayments",
        "You cannot track your VAT repayments online because your VAT registration has been cancelled.",
        "Call us on 0300 200 3700 if you need to track a VAT repayment.",
        "Before you call, make sure you have your VAT registration number which is 9 digits long, like 123456789.",
        "Our opening times are Monday to Friday, 8am to 6pm. We are closed on weekends and bank holidays.",
        "If you need extra support",
        "Find out the different ways to deal with HMRC if you need some help.",
        "You can also use Relay UK if you cannot hear or speak on the phone: dial 18001 then 0300 200 3700.",
        "If you’re calling from outside the UK",
        "Call us on +44 2920 501 261.",
        "Our opening times are Monday to Friday, 8am to 6pm (UK time). We are closed on weekends and bank holidays."
      )

      val mainTextWelsh: List[String] = List(
        "Ffoniwch ni i ddilyn hynt eich ad-daliadau TAW",
        "Ni allwch ddilyn hynt eich ad-daliadau TAW ar-lein oherwydd bod eich cofrestriad TAW wedi’i ganslo.",
        "Os oes angen i chi ddilyn hynt eich ad-daliadau TAW, ffoniwch ni ar 0300 200 1900.",
        "Cyn i chi ffonio, gwnewch yn siŵr bod gennych eich rhif cofrestru TAW, sy’n 9 digid o hyd, er enghraifft 123456789.",
        "Ein horiau agor yw Dydd Llun i Ddydd Gwener, 08:30 i 17:00. Rydym ar gau ar benwythnosau a gwyliau banc.",
        "Os oes angen cymorth ychwanegol arnoch chi",
        "Dysgwch am y ffyrdd gwahanol o ddelio â CThEF os oes angen help arnoch chi.",
        "Gallwch hefyd defnyddio Relay UK os na allwch glywed na siarad dros y ffôn: deialwch 18001 ac yna 0300 200 3700. Sylwer – dim ond galwadau ffôn Saesneg eu hiaith y mae Relay UK yn gallu ymdrin â nhw."
      )
    }
  }
}
