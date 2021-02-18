/*
 * Copyright 2021 HM Revenue & Customs
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

package langswitch

object AccessibilityStatementMessages {

  val label: Message = Message(english = "Accessibility statement")

  val header: Message = Message(
    english = "Accessibility statement for the VAT repayments service",
    welsh   = "Datganiad hygyrchedd ar gyfer y gwasanaeth ad-dalu TAW")
  val intro_p1: Message = Message(
    english =
      "This accessibility statement explains how accessible this service is, what to do if you have difficulty using it, and how to report accessibility problems with the service.",
    welsh   =
      "Mae’r datganiad hygyrchedd hwn yn esbonio pa mor hygyrch yw’r gwasanaeth hwn, beth i’w wneud os ydych yn cael anhawster wrth ei ddefnyddio, a sut i roi gwybod am broblemau hygyrchedd gyda’r gwasanaeth."
  )
  val intro_p2: Message = Message(
    english = "This service is part of the wider GOV.UK website. There is a separate <a href=\"https://www.gov.uk/help/accessibility\">accessibility statement</a> for the main GOV.UK website.",
    welsh   = "Mae’r gwasanaeth hwn yn rhan o wefan ehangach GOV.UK. Mae <a href=\"https://www.gov.uk/help/accessibility\">datganiad hygyrchedd</a> ar wahân ar gyfer prif wefan GOV.UK."
  )
  val intro_p3: Message = Message(
    english = "This page only contains information about the VAT repayment service, available at www.tax.service.gov.uk/vat-repayment-tracker/show-vrt",
    welsh   = "Mae’r dudalen hon yn cynnwys gwybodaeth am y gwasanaeth ad-dalu TAW yn unig, sydd ar gael yn www.tax.service.gov.uk/vat-repayment-tracker/show-vrt "
  )

  val using_this_service_header: Message = Message(
    english = "Using this service",
    welsh   = "Defnyddio’r gwasanaeth hwn"
  )
  val using_this_service_p1: Message = Message(
    english = "This service allows you to view the details and estimated payment date of your VAT repayments.",
    welsh   = "Mae’r gwasanaeth hwn yn caniatáu i chi weld manylion a dyddiad talu amcangyfrifedig eich ad-daliadau TAW."
  )
  val using_this_service_p2: Message = Message(
    english = "This service is run by HM Revenue and Customs (HMRC). We want as many people as possible to be able to use this service. This means you should be able to:",
    welsh   = "Mae’r gwasanaeth hwn yn cael ei redeg gan Gyllid a Thollau EM (CThEM). Rydym am i gymaint o bobl â phosibl allu defnyddio’r gwasanaeth hwn. Mae hyn yn golygu y dylech allu gwneud y canlynol:"
  )
  val using_this_service_li1: Message = Message(
    english = "change colours, contrast levels and fonts",
    welsh   = "newid lliwiau, lefelau cyferbyniad a ffontiau"
  )
  val using_this_service_li2: Message = Message(
    english = "zoom in up to 300% without the text spilling off the screen",
    welsh   = "chwyddo’r sgrin hyd at 300% heb i’r testun ddisgyn oddi ar y sgrin"
  )
  val using_this_service_li3: Message = Message(
    english = "get from the start of the service to the end using just a keyboard",
    welsh   = "mynd o ddechrau’r gwasanaeth i’r diwedd gan ddefnyddio bysellfwrdd yn unig"
  )
  val using_this_service_li4: Message = Message(
    english = "get from the start of the service to the end using speech recognition software",
    welsh   = "mynd o ddechrau’r gwasanaeth i’r diwedd gan ddefnyddio meddalwedd adnabod lleferydd"
  )
  val using_this_service_li5: Message = Message(
    english = "listen to the service using a screen reader (including the most recent versions of JAWS, NVDA and VoiceOver)",
    welsh   = "gwrando ar y gwasanaeth gan ddefnyddio darllenydd sgrin (gan gynnwys y fersiynau diweddaraf o JAWS, NVDA a VoiceOver)"
  )
  val using_this_service_p3: Message = Message(
    english = "We have also made the text in the service as simple as possible to understand.",
    welsh   = "Rydym hefyd wedi sicrhau bod y testun a ddefnyddir yn y gwasanaeth mor syml â phosibl i’w ddeall."
  )
  val using_this_service_p4: Message = Message(
    english = "<a class=\"govuk-link\" href=\"https://mcmw.abilitynet.org.uk/\">AbilityNet</a> has advice on making your device easier to use if you have a disability.",
    welsh   = "Mae gan <a class=\"govuk-link\" href=\"https://mcmw.abilitynet.org.uk/\">AbilityNet</a> gyngor ar wneud eich dyfais yn haws i’w defnyddio os oes gennych anabledd.")

  val how_accessible_header: Message = Message(
    english = "How accessible this service is",
    welsh   = "Pa mor hygyrch yw’r gwasanaeth hwn"
  )
  val how_accessible_p1: Message = Message(
    english = "This service is partially compliant with the <a class=\"govuk-link\" href=\"https://www.w3.org/TR/WCAG21/\">Web Content Accessibility Guidelines version 2.1 AA standard</a>.",
    welsh   = "Mae’r gwasanaeth hwn yn cydymffurfio’n rhannol â <a class=\"govuk-link\" href=\"https://www.w3.org/TR/WCAG21/\">safon AA Canllawiau Hygyrchedd Cynnwys y We, fersiwn 2.1</a>."
  )
  val how_accessible_p2: Message = Message(
    english = "Some people may find parts of this service difficult to use:",
    welsh   = "Mae’n bosibl y bydd rhai pobl yn ei chael hi’n anodd defnyddio rhannau o’r gwasanaeth hwn:   "
  )
  val how_accessible_li1: Message = Message(
    english = "some of the pages in the service which use deprecated HTML such as tab-index attributes and the same ID in some instances, this prevents assistive technologies using the service easily",
    welsh   = "rhai o’r tudalennau yn y gwasanaeth sy’n defnyddio HTML anghymeradwy fel priodoleddau mynegai tab a’r un ID mewn rhai achosion, mae hyn yn atal technolegau cynorthwyol rhag defnyddio’r gwasanaeth yn hawdd"
  )

  val difficulty_usiing_service_header: Message = Message(
    english = "What to do if you have difficulty using this service",
    welsh   = "Beth i’w wneud os ydych yn cael anhawster wrth ddefnyddio’r gwasanaeth hwn"
  )
  val difficulty_usiing_service_p1: Message = Message(
    english = "If you have difficulty using this service, use the 'Get help with this page' link on the page in the online service.",
    welsh   = "Os ydych yn cael anhawster wrth ddefnyddio’r gwasanaeth hwn, defnyddiwch y cysylltiad ‘Help gyda’r dudalen hon’ ar y dudalen yn y gwasanaeth ar-lein."
  )

  val reporting_header: Message = Message(
    english = "Reporting accessibility problems with this service",
    welsh   = "Rhoi gwybod am broblemau hygyrchedd gyda’r gwasanaeth hwn"
  )
  def reporting_p1(url: String): Message = Message(
    english = s"""We are always looking to improve the accessibility of this service. If you find any problems that are not listed on this page or think we are not meeting accessibility requirements, report the <a href=\"$url\" target=\"_blank\">accessibility problem (opens in new tab)</a>.""",
    welsh   = s"""Rydym bob amser yn ceisio gwella hygyrchedd y gwasanaeth hwn. Os byddwch yn dod o hyd i unrhyw broblemau nad ydynt wedi’u rhestru ar y dudalen hon, neu os ydych o’r farn nad ydym yn bodloni gofynion hygyrchedd, rhowch wybod am y <a href=\"$url\" target=\"_blank\">broblem hygyrchedd (yn agor ffenestr neu dab newydd)</a>."""
  )

  val not_happy_with_response_header: Message = Message(
    english = "What to do if you are not happy with how we respond to your complaint",
    welsh   = "Beth i’w wneud os nad ydych yn hapus â sut rydym yn ateb eich cwyn")
  val not_happy_with_response_p1: Message = Message(
    english = "The Equality and Human Rights Commission (EHRC) is responsible for enforcing the Public Sector Bodies (Websites and Mobile Applications) (No. 2) Accessibility Regulations 2018 (the ‘accessibility regulations’). If you are not happy with how we respond to your complaint, <a href=\"https://www.equalityadvisoryservice.com/\">contact the Equality Advisory and Support Service</a> (EASS), or the <a href=\"https://www.equalityni.org/Home\">Equality Commission for Northern Ireland</a> (ECNI) if you live in Northern Ireland.",
    welsh   = "Mae’r Comisiwn Cydraddoldeb a Hawliau Dynol (EHRC) yn gyfrifol am orfodi Rheoliadau Hygyrchedd Cyrff Sector Cyhoeddus (Gwefannau a Chymwysiadau Symudol) (Rhif 2) 2018 (y ‘rheoliadau hygyrchedd’). Os nad ydych yn hapus â sut rydym yn ateb eich cwyn,  <a href=\"https://www.equalityadvisoryservice.com/\">cysylltwch â’r Gwasanaeth Cynghori a Chymorth Cydraddoldeb</a> (EASS), neu’r <a href=\"https://www.equalityni.org/Home\">Equality Commission for Northern Ireland</a> (ECNI) os ydych yn byw yng Ngogledd Iwerddon."
  )

  val contacting_us_header: Message = Message(
    english = "Contacting us by phone or getting a visit from us in person",
    welsh   = "Cysylltu â ni dros y ffôn neu gael ymweliad personol gennym"
  )
  val contacting_us_p1: Message = Message(
    english = "We provide a text relay service if you are deaf, hearing impaired or have a speech impediment.",
    welsh   = "Rydym yn cynnig gwasanaeth text relay os ydych yn fyddar, â nam ar eich clyw neu os oes gennych nam ar eich lleferydd."
  )
  val contacting_us_p2: Message = Message(
    english = "We can provide a British Sign Language (BSL) interpreter, or you can arrange a visit from an HMRC advisor to help you complete the service.",
    welsh   = "Gallwn ddarparu dehonglydd Iaith Arwyddion Prydain (BSL), neu gallwch drefnu ymweliad gan ymgynghorydd CThEM i’ch helpu i gwblhau’r gwasanaeth."
  )
  val contacting_us_p3: Message = Message(
    english = "Find out how to <a href=\"https://www.gov.uk/dealing-hmrc-additional-needs\">contact us</a>.",
    welsh   = "Gwybodaeth am sut i <a href=\"https://www.gov.uk/dealing-hmrc-additional-needs\">gysylltu â ni</a>."
  )

  val technical_information_header: Message = Message(
    english = "Technical information about this service’s accessibility",
    welsh   = "Gwybodaeth dechnegol am hygyrchedd y gwasanaeth hwn"
  )
  val technical_information_p1: Message = Message(
    english = "HMRC is committed to making this service accessible, in accordance with the Public Sector Bodies (Websites and Mobile Applications) (No. 2) Accessibility Regulations 2018.",
    welsh   = "Mae CThEM wedi ymrwymo i wneud y gwasanaeth hwn yn hygyrch, yn unol â Rheoliadau Hygyrchedd Cyrff Sector Cyhoeddus (Gwefannau a Chymwysiadau Symudol) (Rhif 2) 2018."
  )
  val technical_information_p2: Message = Message(
    english = "This service is partially compliant with the <a href=\"https://www.w3.org/TR/WCAG21/\">Web Content Accessibility Guidelines version 2.1 AA standard</a>, due to the compliances listed below.",
    welsh   = "Mae’r gwasanaeth hwn yn cydymffurfio’n rhannol â <a href=\"https://www.w3.org/TR/WCAG21/\">safon AA Canllawiau Hygyrchedd Cynnwys y We, fersiwn 2.1</a>."
  )
  val technical_information_header2: Message = Message(
    english = "Non-accessible content",
    welsh   = "Cynnwys nad yw’n hygyrch"
  )
  val technical_information_p3: Message = Message(
    english = "The content listed below is non-accessible for the following reasons.",
    welsh   = "Nid yw’r cynnwys a restrir isod yn hygyrch oherwydd y rhesymau canlynol."
  )
  val technical_information_header3: Message = Message(
    english = "Non-compliance with the accessibility regulations",
    welsh   = "Diffyg cydymffurfio â’r rheoliadau hygyrchedd"
  )
  val technical_information_p4: Message = Message(
    english = "The service uses some deprecated attributes and IDs that are not unique which means assistive technologies cannot use the service reliably. This does not meet WCAG success criterion 4.1.1 (Parsing).",
    welsh   = "Mae’r gwasanaeth yn defnyddio rhai priodoleddau ac ID anghymeradwy sydd ddim yn unigryw sy’n golygu nad yw’r gwasanaeth yn ddibynadwy i dechnolegau cynorthwyol. Nid yw hyn yn bodloni maen prawf llwyddiant 4.1.1 (Parsing) Canllawiau Hygyrchedd Cynnwys y We."
  )
  val technical_information_p5: Message = Message(
    english = "A DAC assessment has been requested and we plan to correct the issues in the service by 15 December 2021.",
    welsh   = "Gofynnwyd am asesiad DAC ac rydym yn bwriadu cywiro’r materion yn y gwasanaeth erbyn mis 15 Rhagfyr 2021."
  )

  val how_we_tested_header: Message = Message(
    english = "How we tested this service",
    welsh   = "Sut gwnaethom brofi’r gwasanaeth hwn\n"
  )
  val how_we_tested_p1: Message = Message(
    english = "The service was last tested on 13 August 2020 and was checked for compliance with WCAG 2.1 AA. The service was built using parts that were tested by the Digital Accessibility Centre. ",
    welsh   = "Cafodd y gwasanaeth ei brofi diwethaf ar 13 Awst 2020 a gwiriwyd ei fod yn cydymffurfio â safon ‘AA’ Canllawiau Hygyrchedd Cynnwys y We, fersiwn 2.1. Adeiladwyd y gwasanaeth gan ddefnyddio rhannau a brofwyd gan y Ganolfan Hygyrchedd Digidol (Digital Accessibility Centre). "
  )
  val how_we_tested_p2: Message = Message(
    english = "The full service was tested by HMRC and included disabled users.",
    welsh   = "Profwyd y gwasanaeth llawn gan CThEM, ac roedd y gwaith o brofi’r gwasanaeth yn cynnwys defnyddwyr anabl."
  )
  val how_we_tested_p3: Message = Message(
    english = "This page was prepared on 24 August 2020. It was last updated on 12 February 2021.",
    welsh   = "Paratowyd y dudalen hon ar 24 Awst 2020. Cafodd ei diweddaru diwethaf ar 12 Chwefror 2021."
  )

}
