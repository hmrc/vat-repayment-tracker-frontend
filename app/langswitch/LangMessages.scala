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

import model.des.BankDetails
import play.api.mvc.Request
import req.RequestSupport

object LangMessages {

  val exapmleMessage: Message = Message(
    english = "Example message",
    welsh   = "Neges enghreifftiol"
  )

  val `We are processing your VAT repayments`: Message = Message(
    english = "We are processing your VAT repayments",
    welsh   = "Rydym yn prosesu’ch ad-daliadau TAW"

  )

  val `Amount claimed`: Message = Message(
    english = "Amount claimed",
    welsh   = "Swm a hawliwyd"
  )

  val `Received on`: Message = Message(
    english = "Received on",
    welsh   = "Cafwyd ar"
  )

  val `Accounting period`: Message = Message(
    english = "Accounting period",
    welsh   = "Cyfnod cyfrifyddu"
  )

  val `Accounting period progress hidden`: Message = Message(
    english = "View progress for accounting period",
    welsh   = "Bwrw golwg dros gynnydd ar gyfer y cyfnod cyfrifyddu"
  )

  val `Accounting period history hidden`: Message = Message(
    english = "View history for accounting period",
    welsh   = "Bwrw golwg dros hanes ar gyfer y cyfnod cyfrifyddu"
  )

  val `When we will repay you`: Message = Message(
    english = "When we will repay you",
    welsh   = "Pryd y byddwn yn eich ad-dalu"
  )

  val `When we will repay you description`: Message = Message(
    english = "We will usually repay you within 30 days of HMRC receiving your VAT Return. If you need to speak to someone about your repayment, you can only do this after 30 days have passed.",
    welsh   = "Fel arfer, caiff ad-daliadau eu gwneud cyn pen 30 diwrnod ar ôl i CThEM gael eich Ffurflen TAW. Os bydd angen i chi siarad â rhywun am eich ad-daliad, gallwch ond wneud hynny ar ôl 30 diwrnod."
  )
  val `after 30 days have passed.`: Message = Message(
    english = " after 30 days have passed."
  )

  val `No VAT repayments in progress`: Message = Message(
    english = "No VAT repayments in progress",
    welsh   = "Nid oes ad-daliadau TAW ar y gweill"
  )

  val `Repayments usually take 24 hours`: Message = Message(
    english = "Repayments usually take around 24 hours to show on this page after submitting your VAT Return.",
    welsh   = "Fel arfer, mae’n cymryd tua 24 awr i ddangos ad-daliadau ar y dudalen hon ar ôl cyflwyno’ch Ffurflen TAW."
  )

  val `Check this page when you are next expecting a repayment.`: Message = Message(
    english = "Check this page when you are next expecting a repayment.",
    welsh   = "Gwiriwch y dudalen hon y tro nesa yr ydych yn disgwyl ad-daliad"
  )

  val `View your VAT account`: Message = Message(
    english = "View your VAT account",
    welsh   = "Bwrw golwg dros eich cyfrif TAW"
  )

  val `You are currently paid by bank transfer`: Message = Message(
    english = "You are currently paid by bank transfer to the following account:",
    welsh   = "Rydych yn cael eich talu drwy drosglwyddiad banc ar hyn o bryd, a hynny i’r cyfrif canlynol:"
  )

  val `Name on account`: Message = Message(
    english = "Name on account:",
    welsh   = "Yr enw sydd ar y cyfrif:"
  )

  val `Sort code`: Message = Message(
    english = "Sort code:",
    welsh   = "Cod didoli:"
  )

  val `Account number`: Message = Message(
    english = "Account number:",
    welsh   = "Rhif y cyfrif:"
  )

  val `Manage your repayment bank account`: Message = Message(
    english = "Manage your repayment bank account",
    welsh   = "Rheoli’ch cyfrif banc ar gyfer ad-daliadau"
  )

  val `Change account details`: Message = Message(
    english = "Change account details",
    welsh   = "Newid manylion cyfrif"
  )

  val `Your repayment bank details`: Message = Message(
    english = "Your repayment bank details",
    welsh   = "Eich manylion banc ar gyfer ad-daliadau"
  )

  val `Your repayment details`: Message = Message(
    english = "Your repayment details",
    welsh   = "Eich manylion ar gyfer ad-daliadau"
  )

  val `Track your VAT repayments`: Message = Message(
    english = "Track your VAT repayments",
    welsh   = "Dilyn hynt eich ad-daliadau TAW"
  )

  val `Sent to this account`: Message = Message(
    english = "Your VAT repayments will be sent to this account",
    welsh   = "Caiff eich ad-daliadau TAW eu hanfon i’r cyfrif hwn"
  )

  val `View your business details to update your repayment bank account`: Message = Message(
    english = "View your business details to update your repayment bank account",
    welsh   = "Bwrw golwg dros fanylion eich busnes i ddiweddaru’ch cyfrif banc ar gyfer ad-daliadau"
  )

  val `Contact HMRC`: Message = Message(
    english = "contact HMRC",
    welsh   = "cysylltu â CThEM"
  )

  val `In progress`: Message = Message(
    english = "In progress",
    welsh   = "Ar y gweill"
  )

  val `In progress repayment caption`: Message = Message(
    english = "VAT repayments in progress",
    welsh   = "Ad-daliadau TAW ar y gweill"
  )

  val `Repayment suspended` = Message(
    english = "Repayment suspended",
    welsh   = "Ad-daliad wedi’i ohirio"
  )

  val `Your repayment is suspended` = Message(
    english = "Your repayment is suspended.",
    welsh   = "Mae’ch ad-daliad wedi’i ohirio."
  )

  val `Action required` = Message(
    english = "Action required",
    welsh   = "Angen cymryd camau"
  )

  val `You must submit your latest VAT return` = Message(
    english = "You must submit your latest VAT return",
    welsh   = "Mae’n rhaid i chi gyflwyno’ch Ffurflen TAW ddiweddaraf"
  )

  val `submit your latest VAT return` = Message(
    english = "You must <a href=\"https://www.gov.uk/vat-returns/send-your-return\">submit your latest VAT return</a>",
    welsh   = "Mae’n rhaid i chi <a href=\"https://www.gov.uk/ffurflenni-taw/sut-i-gyflwynoch-ffurflen-taw\">gyflwyno’ch Ffurflen TAW ddiweddaraf</a>"
  )

  val `Submit VAT return` = Message(
    english = "Submit VAT return",
    welsh   = "Cyflwyno Ffurflen TAW"
  )

  val `Submit your return` = Message(
    english = "<a href=\"https://www.gov.uk/vat-returns/send-your-return\">Submit your return</a>",
    welsh   = "<a href=\"https://www.gov.uk/ffurflenni-taw/sut-i-gyflwynoch-ffurflen-taw\">Cyflwyno’ch Ffurflen TAW</a>"
  )

  val `We cannot process your repayment` = Message(
    english = "We cannot process your repayment until you submit your VAT return for the last accounting period.<br /><br /><a href=\"https://www.gov.uk/vat-returns/send-your-return\">Submit your return</a>",
    welsh   = "Ni allwn brosesu’ch ad-daliad hyd nes eich bod yn cyflwyno’ch Ffurflen TAW ar gyfer y cyfnod cyfrifyddu diweddaf.<br /><br /><a href=\"https://www.gov.uk/ffurflenni-taw/sut-i-gyflwynoch-ffurflen-taw\">Cyflwyno’ch Ffurflen TAW</a>"
  )

  val `Currently sent to`: Message = Message(
    english = "We'll send your cheque to:",
    welsh   = "Byddwn yn anfon eich siec i:"
  )

  val `For faster payment next time`: Message = Message(
    english = "For faster payment next time",
    welsh   = "I gael eich talu’n fwy cyflym y tro nesaf"
  )

  val `add a repayment bank account`: Message = Message(
    english = "add a repayment bank account",
    welsh   = "ychwanegu cyfrif banc ar gyfer ad-daliadau"
  )

  val `Update your business address`: Message = Message(
    english = "Update your business address",
    welsh   = "Diweddaru cyfeiriad eich busnes"
  )

  val `Manage or track`: Message = Message(
    english = "Manage Direct Debit and repayment bank account or track repayments",
    welsh   = "Rheoli Debyd Uniongyrchol a’r cyfrif banc ar gyfer ad-daliadau neu ddilyn hynt ad-daliadau"
  )

  val `Manage or track shuttered`: Message = Message(
    english = "Manage Direct Debit and repayment bank account",
    welsh   = "Rheoli Debyd Uniongyrchol a’r cyfrif banc ar gyfer ad-daliadau"
  )

  val heading_bta: Message = Message(
    english = "Business tax account",
    welsh   = "Cyfrif treth busnes"
  )

  val generic_prefixError: Message = Message(
    english = "Error",
    welsh   = "Gwall"
  )

  val Continue: Message = Message(
    english = "Continue",
    welsh   = "Yn eich blaen"
  )
  val `View what HMRC owe you`: Message = Message(
    english = "View what HMRC owe you",
    welsh   = "Bwrw golwg dros yr hyn sydd ar CThEM i chi"
  )
  val `There is a problem`: Message = Message(
    english = "There is a problem",
    welsh   = "Mae problem wedi codi"
  )

  def accountAndSortCode(bankDetails: BankDetails): Message = Message(
    english = s"""Account: ${bankDetails.obscureBankAccountNumber}, Sort code: ${bankDetails.formatSortCode}""",
    welsh   = s"Cyfrif: ${bankDetails.obscureBankAccountNumber}, Cod didoli: ${bankDetails.formatSortCode}"
  )

  val `Manage your Direct Debit`: Message = Message(
    english = "Manage your Direct Debit",
    welsh   = "Rheoli’ch Debyd Uniongyrchol"
  )

  val `Set up a Direct Debit`: Message = Message(
    english = "Set up a Direct Debit",
    welsh   = "Sefydlu Debyd Uniongyrchol"
  )

  val `HMRC will automatically`: Message = Message(
    english = "HMRC will automatically collect your VAT Return payments when due",
    welsh   = "Bydd CThEM yn casglu’ch taliadau Ffurflen TAW yn awtomatig pan fyddant yn ddyledus"
  )

  val `Set up a repayment bank account`: Message = Message(
    english = "Set up a repayment bank account",
    welsh   = "Sefydlu cyfrif banc ar gyfer ad-daliadau")

  val `Tell HMRC where to pay your money`: Message = Message(
    english = "Tell HMRC where to pay your money",
    welsh   = "Rhoi gwybod i CThEM ble i dalu’ch arian")

  val `View progress`: Message = Message(
    "View progress",
    "Bwrw golwg dros y cynnydd"
  )

  val `View history`: Message = Message(
    english = "View history",
    welsh   = "Bwrw golwg dros eich hanes"
  )

  val `Completed`: Message = Message(
    "Completed",
    "Wedi’i gwblhau"
  )

  val `Completed repayment caption`: Message = Message(
    english = "Completed VAT repayments",
    welsh   = "Ad-daliadau TAW wedi’u cwblhau"
  )

  val `No completed repayments content here`: Message = Message(
    english = "No completed repayments content here",
    welsh   = "Nid oes cynnwys ar gyfer ad-daliadau wedi’u gwneud i’w gael yma"
  )

  val `No in progress repayments content here`: Message = Message(
    english = "No in progress repayments content here",
    welsh   = "Nid oes cynnwys ar gyfer ad-daliadau ar y gweill i’w gael yma"
  )

  val `Your repayment is being processed`: Message = Message(
    english = "Your repayment is being processed",
    welsh   = "Mae’ch ad-daliad yn cael ei brosesu")

  val `Your repayment is delayed`: Message = Message(
    english = "Your repayment is delayed",
    welsh   = "Mae oedi i’ch ad-daliad")

  val `VAT Return received on`: Message = Message(
    english = "VAT Return received on",
    welsh   = "Daeth Ffurflenni TAW i law ar"
  )

  val `Estimated repayment date`: Message = Message(
    english = "Estimated repayment date",
    welsh   = "Amcangyfrif o’r dyddiad ad-dalu"
  )

  val `Your repayment progress`: Message = Message(
    english = "Your repayment progress",
    welsh   = "Cynnydd eich ad-daliadau")

  val `Your repayment history`: Message = Message(
    english = "Your repayment history",
    welsh   = "Hanes eich ad-daliadau")

  val `You must now pay some VAT`: Message = Message(
    english = "You most now pay some VAT",
    welsh   = "Mae’n rhaid i chi dalu rhywfaint o TAW nawr"
  )

  val `Your repayment is complete`: Message = Message(
    english = "Your repayment is complete",
    welsh   = "Mae’ch ad-daliad wedi’i wneud")

  val `Checking amount`: Message = Message(
    english = "Checking amount",
    welsh   = "Gwirio’r swm")

  val `We received your return`: Message = Message(
    english = "We received your return and are now checking the repayment amount we owe you.",
    welsh   = "Mae’ch Ffurflen TAW wedi dod i law ac rydym yn awr yn gwirio swm yr ad-daliad sydd arnom i chi."
  )

  val `Repayment amount changed`: Message = Message(
    english = "Repayment amount changed",
    welsh   = "Rydym wedi newid y swm i’w ad-dalu")

  def `You claimed a VAT repayment of`(originalPostingAmount: String, vatToPay_Box5: String, url: String): Message =
    Message(
      s"You claimed £$originalPostingAmount. We calculated this amount was incorrect so we will repay you £$vatToPay_Box5.  This will reach your repayment bank account in 3 working days.  We sent you a letter explaining why we changed your amount. </p><p>If you do not receive a letter in the next 7 days, check your <a href=$url>VAT payments history</a>.",
      s"Gwnaethoch hawlio £$originalPostingAmount. Rydym wedi cyfrifo’r swm hwn i fod yn anghywir, felly byddwn yn ad-dalu £$vatToPay_Box5 i chi. Bydd hwn yn cyrraedd eich cyfrif banc ar gyfer ad-daliadau cyn pen 3 diwrnod gwaith. Gwnaethom anfon llythyr atoch yn esbonio pam y gwnaethom newid eich swm. </p><p>Os na chewch lythyr o fewn y 7 diwrnod nesaf, gwiriwch <a href=$url>hanes eich taliadau TAW</a>."
    )

  val `Sending for further checks`: Message = Message(
    english = "Sending for further checks",
    welsh   = "Anfon am wiriadau pellach")

  val `Repayment approved`: Message = Message(
    english = "Repayment approved",
    welsh   = "Ad-daliad wedi’i gymeradwyo")

  val `VAT payment due`: Message = Message(
    english = "VAT payment due",
    welsh   = "Taliad TAW sy’n ddyledus")

  def `We calculated that the original amount you claimed of`(originalPostingAmount: String, vatToPay_Box5: String): Message =
    Message(
      english = s"""We calculated that the original amount of £$originalPostingAmount you claimed was incorrect. You now owe HMRC £$vatToPay_Box5. We sent you a letter with the reason for this change.""",
      welsh   = s"Gwnaethom gyfrifo bod y swm gwreiddiol o £$originalPostingAmount y gwnaethoch ei hawlio’n anghywir. Erbyn hyn, mae arnoch £$vatToPay_Box5 i CThEM. Gwnaethom anfon llythyr atoch gyda’r rheswm dros y newid hwn."
    )

  val `Estimated repayment date has passed`: Message = Message(
    english = "Estimated repayment date has passed",
    welsh   = "Mae’r dyddiad ad-dalu amcangyfrifiedig wedi mynd heibio")

  val `You do not need to do anything right now`: Message =
    Message(
      english = """You do not need to do anything right now. We are working on repaying you as soon as possible. </p><p>We have sent you a letter to explain that your repayment is delayed.""",
      welsh   = """Does dim angen i chi wneud dim byd ar hyn o bryd. Rydym yn gweithio ar eich ad-dalu cyn gynted â phosibl. </p><p>Rydym wedi anfon llythyr atoch er mwyn esbonio bod oedi i’ch ad-daliad."""
    )

  val `Repayment complete`: Message = Message(
    english = "Repayment complete",
    welsh   = "Ad-daliad wedi’i wneud")

  val `Your repayment has been approved`: Message = Message(
    english = "Your repayment has been approved",
    welsh   = "Mae’ch ad-daliad wedi cael ei gymeradwyo")

  val `We are making sure we pay you the right amount`: Message = Message(
    english = "We are making sure we pay you the right amount. You do not need to do anything, but we may contact you if we need any further information.",
    welsh   = "Rydym yn gwneud yn siŵr ein bod yn talu’r swm cywir i chi. Nid oes angen i chi wneud dim byd, ond mae’n bosibl y byddwn yn cysylltu â chi os bydd angen gwybodaeth bellach arnom."
  )

  val period_1: Message = Message(
    english = "1 January to 31 March",
    welsh   = "1 Ionawr i 31 Mawrth"
  )
  val period_2: Message = Message(
    english = "1 April to 30 June",
    welsh   = "1 Ebrill i 30 Mehefin"
  )
  val period_3: Message = Message(
    english = "1 July to 30 September",
    welsh   = "1 Gorffennaf i 30 Medi"
  )
  val period_4: Message = Message(
    english = "1 October to 31 December",
    welsh   = "1 Hydref i 31 Rhagfyr")

  val January: Message = Message(
    "1 January to 31 January",
    "1 Ionawr i 31 Ionawr"
  )
  val February: Message = Message(
    "1 February to 28 February",
    "1 Chwefror i 28 Chwefror"
  )
  val February_leap: Message = Message(
    "1 February to 29 February",
    "1 Chwefror i 29 Chwefror"
  )
  val March: Message = Message(
    "1 March to 31 March",
    "1 Mawrth i 31 Mawrth"
  )
  val April: Message = Message(
    "1 April to 30 April",
    "1 Ebrill i 30 Ebrill"
  )
  val May: Message = Message(
    "1 May to 31 May",
    "1 Mai i 31 Mai"
  )
  val June: Message = Message(
    "1 June to 30 June",
    "1 Mehefin i 30 Mehefin"
  )
  val July: Message = Message(
    "1 July to 31 July",
    "1 Gorffennaf i 31 Gorffennaf"
  )
  val August: Message = Message(
    "1 August to 31 August",
    "1 Awst i 31 Awst"
  )
  val September: Message = Message(
    "1 September to 30 September",
    "1 Medi i 30 Medi"
  )
  val October: Message = Message(
    "1 October to 31 October",
    "1 Hydref i 31 Hydref"
  )
  val November: Message = Message(
    "1 November to 30 November",
    "1 Tachwedd i 30 Tachwedd"
  )
  val December: Message = Message(
    "1 December to 31 December",
    "1 Rhagfyr i 31 Rhagfyr"
  )

  val JanToDec: Message = Message(
    "January to December",
    "Ionawr – Rhagfyr"
  )
  val FebToJan: Message = Message(
    english = "February to January",
    welsh   = "Chwefror – Ionawr")
  val MarToFeb: Message = Message(
    english = "March to February",
    welsh   = "Mawrth – Chwefror")
  val AprToMar: Message = Message(
    english = "April to March",
    welsh   = "Ebrill – Mawrth")
  val MayToApr: Message = Message(
    english = "May to April",
    welsh   = "Mai – Ebrill")
  val JunToMay: Message = Message(
    english = "June to May",
    welsh   = "Mehefin – Mai")
  val JulToJun: Message = Message(
    english = "July to June",
    welsh   = "Gorffennaf – Mehefin")
  val AugToJul: Message = Message(
    english = "August to July",
    welsh   = "Awst – Gorffennaf")
  val SepToAug: Message = Message(
    english = "September to August",
    welsh   = "Medi – Awst")
  val OctToSep: Message = Message(
    english = "October to September",
    welsh   = "Hydref – Medi")
  val NovToOct: Message = Message(
    english = "November to October",
    welsh   = "Tachwedd – Hydref")
  val DecToNov: Message = Message(
    english = "December to November",
    welsh   = "Rhagfyr – Tachwedd")
  val JanuaryQuarter: Message = Message(
    english = "1 November to 31 January",
    welsh   = "1 Tachwedd i 31 Ionawr")
  val FebruaryQuarter: Message = Message(
    english = "1 December to 28 February",
    welsh   = "1 Rhagfyr i 28 Chwefror")
  val FebruaryQuarter_leap: Message = Message(
    english = "1 December to 29 February",
    welsh   = "1 Rhagfyr i 29 Chwefror")
  val MarchQuarter: Message = Message(
    english = "1 January to 31 March",
    welsh   = "1 Ionawr i 31 Mawrth")
  val AprilQuarter: Message = Message(
    english = "1 February to 30 April",
    welsh   = "1 Chwefror i 30 Ebrill")
  val MayQuarter: Message = Message(
    english = "1 March to 31 May",
    welsh   = "1 Mawrth i 31 Mai")
  val JuneQuarter: Message = Message(
    english = "1 April to 30 June",
    welsh   = "1 Ebrill i 30 Mehefin")
  val JulyQuarter: Message = Message(
    english = "1 May to 31 July",
    welsh   = "1 Mai i 31 Gorffennaf")
  val AugustQuarter: Message = Message(
    english = "1 June to 31 August",
    welsh   = "1 Mehefin i 31 Awst")
  val SeptemberQuarter: Message = Message(
    english = "1 July to 30 September",
    welsh   = "1 Gorffennaf i 30 Medi")
  val OctoberQuarter: Message = Message(
    english = "1 August to 31 October",
    welsh   = "1 Awst i 31 Hydref")
  val NovemberQuarter: Message = Message(
    english = "1 September to 30 November",
    welsh   = "1 Medi i 30 Tachwedd")
  val DecemberQuarter: Message = Message(
    english = "1 October to 31 December",
    welsh   = "1 Hydref i 31 Rhagfyr")

  val `We will send this to your repayment bank account`: Message = Message(
    english = "This will reach your repayment bank account in 3 workings days.",
    welsh   = "Bydd hwn yn cyrraedd eich cyfrif banc ar gyfer ad-daliadau cyn pen 3 diwrnod gwaith.")

  def `We will send a cheque to your business address`(url: String): Message = Message(
    english = s"We will send a cheque to your business address. This will reach you in 5 to 6 working days. We sent you a letter explaining why we changed your amount.  </p><p>If you do not receive a letter in the next few days, check your <a href=$url>VAT payments history</a>.",
    welsh   = s"Byddwn yn anfon siec i gyfeiriad eich busnes. Bydd hon yn eich cyrraedd cyn pen 5 i 6 diwrnod gwaith. Gwnaethom anfon llythyr atoch yn esbonio pam y gwnaethom newid eich swm. </p><p>Os na chewch lythyr yn ystod y dyddiau nesaf, gwiriwch <a href=$url>hanes eich taliadau TAW</a>."
  )

  def `You claimed a VAT repayment of post`(originalPostingAmount: String, vatToPay_Box5: String, url: String): Message =
    Message(
      english = s"You claimed £$originalPostingAmount. We calculated this amount was incorrect so we will repay you £$vatToPay_Box5. We will send a cheque to your business address. This will reach you in 5 to 6 working days. We sent you a letter explaining why we changed your amount. </p><p>If you do not receive a letter in the next 7 days, check your <a href=$url>VAT payments history</a>.",
      welsh   = s"Gwnaethoch hawlio £$originalPostingAmount. Rydym wedi cyfrifo’r swm hwn i fod yn anghywir, felly byddwn yn ad-dalu £$vatToPay_Box5 i chi. Byddwn yn anfon siec i gyfeiriad eich busnes. Bydd hon yn eich cyrraedd cyn pen 5 i 6 diwrnod gwaith. Gwnaethom anfon llythyr atoch yn esbonio pam y gwnaethom newid eich swm. </p><p>Os na chewch lythyr o fewn y 7 diwrnod nesaf, gwiriwch <a href=$url>hanes eich taliadau TAW</a>."
    )

  def `repayment-complete-bank-details`(name: String, number: String, sortCode: String, amount: String): Message =
    Message(
      english = s"""We sent a payment of £$amount to your repayment bank account:</p><p>Name: $name</p><p>Account number: $number</p><p>Sort code: $sortCode.""",
      welsh   = s"Gwnaethom anfon taliad o £$amount i’ch cyfrif banc ar gyfer ad-daliadau:</p><p>Enw: $name</p><p>Rhif y cyfrif: $number</p><p>Cod didoli: $sortCode."
    )

  def `repayment-complete-address`(address: String, amount: String): Message = Message(
    english = s"""We sent a payment of £$amount as a cheque to </p><p> $address.""",
    welsh   = s"Gwnaethom anfon taliad o £$amount ar ffurf siec i </p><p> $address."
  )

  def `repayment-complete-bank-details-adjusted`(name: String, number: String, sortCode: String, amount: String): Message =
    Message(
      english = s"""We sent an adjusted payment of £$amount to your repayment bank account:</p><p>Name: $name</p><p>Account number: $number</p><p>Sort code: $sortCode.""",
      welsh   = s"""Gwnaethom anfon taliad wedi’i addasu o £$amount i’ch cyfrif banc ar gyfer ad-daliadau:</p><p>Enw: $name</p><p>Rhif y cyfrif: $number</p><p>Cod didoli: $sortCode."""
    )

  def `repayment-complete-address-adjusted`(address: String, amount: String): Message = Message(
    english = s"""We sent you an adjusted payment of £$amount as a cheque to </p><p> $address.""",
    welsh   = s"""Gwnaethom anfon taliad wedi’i addasu o £$amount ar ffurf siec i </p><p> $address."""
  )

  val addressNotAvailable: Message = Message(
    english = "Address not available",
    welsh   = "Nid yw’r cyfeiriad ar gael")

  val `You now owe HMRC`: Message = Message(
    english = "You now owe HMRC",
    welsh   = "Erbyn hyn, mae arnoch y swm hwn i CThEM")

  val `We received your VAT payment`: Message = Message(
    english = s"""We received your VAT payment.""",
    welsh   = "Mae’ch taliad TAW wedi dod i law."
  )

  val `Amount you claimed`: Message = Message(
    english = "Amount you claimed",
    welsh   = "Y swm a hawliwyd gennych"
  )

  val `Amount we'll pay you`: Message = Message(
    english = "Amount we'll pay you",
    welsh   = "Y swm y byddwch yn ei dalu i chi"
  )

  val `Amount to pay`: Message = Message(
    english = "Amount to pay",
    welsh   = "Y swm i’w dalu"
  )

  val `Amount we paid you`: Message = Message(
    english = "Amount we paid you",
    welsh   = "Y swm y gwnaethom ei dalu i chi")

  val `Amount you paid`: Message = Message(
    english = "Amount you paid",
    welsh   = "Y swm a dalwyd gennych")

  val `You need to make a VAT payment`: Message = Message(
    english = "You need to make a VAT payment",
    welsh   = "Mae’n rhaid i chi wneud taliad TAW"
  )

  val `You have no completed repayments`: Message = Message(
    english = "You have no completed repayments",
    welsh   = "Nid oes gennych ad-daliadau wedi’u gwneud"
  )

  val `No repayments in progress`: Message = Message(
    english = "No repayments in progress",
    welsh   = "Nid oes ad-daliadau ar y gweill")

  val `no-in-progress-desc`: Message = Message(
    """If you are expecting a repayment and it is not showing here, check this page again in 24 hours.</p><p>If you submitted your return early check this page after your accounting period has ended.""",
    """Os ydych yn disgwyl ad-daliad ac nad yw’n ymddangos yma, gwiriwch y dudalen hon eto mewn 24 awr.</p><p>Os gwnaethoch gyflwyno’ch Ffurflen TAW yn gynnar, gwiriwch y dudalen hon ar ôl i’ch cyfnod cyfrifyddu wedi dod i ben."""
  )

  def `no-completed-desc`(url: String): Message = Message(
    s"""Completed repayments will only show here for 60 days after your repayment date. View your <a href="$url">VAT payments history.</a>""",
    s"""Bydd ad-daliadau sydd wedi’i gwneud ond yn dangos yma am 60 diwrnod ar ôl eich dyddiad ad-dalu. Bwrw golwg dros <a href="$url">hanes eich taliadau TAW.</a>""")

  val `Your VAT account`: Message = Message(
    english = "Your VAT account",
    welsh   = "Eich cyfrif TAW")

  val Back: Message = Message(
    english = "Back",
    welsh   = "Yn ôl")

  val `View payments history`: Message = Message(
    english = "View payments history",
    welsh   = "Bwrw golwg dros hanes y taliadau")

  val `Pay now`: Message = Message(
    english = "Pay now",
    welsh   = "Talu nawr")

  val `Payment and repayments`: Message = Message(
    english = "Payment and repayments",
    welsh   = "Taliadau ac ad-daliadau"
  )

  val `You cannot access this service`: Message = Message(
    english = "You cannot access this service",
    welsh   = "Ni allwch gael mynediad at y gwasanaeth hwn"
  )

  val `non-mtd-line1`: Message = Message(
    english = "You have not signed up to Making Tax Digital for VAT. You need to this before you can track your VAT repayments.",
    welsh   = "Nid ydych wedi’ch cofrestru ar gyfer Troi Treth yn Ddigidol ar gyfer TAW. Mae angen i chi wneud hyn cyn y gallwch ddilyn hynt eich ad-daliadau TAW.")

  def `non-mtd-line2`(url: String): Message = Message(
    english = s"""Find out how to <a href="$url" target="_blank">sign up for Making Tax Digital for VAT (opens in a new window or tab).</a>""",
    welsh   = s"""Ewch ati i ddysgu sut i <a href="$url" target="_blank">gofrestru am y cynllun Troi Treth yn Ddigidol ar gyfer TAW (yn agor ffenestr neu dab newydd).</a>""")

  def `non-mtd-line3`(url: String): Message = Message(
    s"""View your <a href="$url">Business Tax Account.""",
    s"""Bwrw golwg dros eich <a href="$url">Cyfrif Treth Busnes."""
  )

  val shuttered: Message = Message(
    english = "This service is shuttered",
    welsh   = "Nid yw’r gwasnaeth hwn ar gael ar hyn o bryd"
  )

  object timeoutDialog {
    def message(implicit request: Request[_]) = if (RequestSupport.isLoggedIn)
      Message(
        english = "For your security, we will sign you out in",
        welsh   = "Er eich diogelwch, byddwn yn eich allgofnodi ymhen")
    else
      Message(
        english = "For your security, we will time you out in",
        welsh   = "Er eich diogelwch, byddwn yn dod â’ch sesiwn i ben ymhen")

    def keepAlive(implicit request: Request[_]) = if (RequestSupport.isLoggedIn)
      Message(
        english = "Stay signed in",
        welsh   = "Parhau i fod wedi’ch mewngofnodi")
    else
      Message(
        english = "Continue",
        welsh   = "Yn eich blaen")

    def signOut(implicit request: Request[_]) = if (RequestSupport.isLoggedIn)
      Message(
        english = "Sign out",
        welsh   = "Allgofnodi")
    else
      Message(english = "Delete your answers",
              welsh   = "Dileu’ch atebion")
  }

  val `For your security, we signed you out` = Message(
    english = "For your security, we signed you out",
    welsh   = "Er eich diogelwch, gwnaethom eich allgofnodi"
  )
  val `We did not save your answers` = Message(
    english = "We did not save your answers.",
    welsh   = "Ni wnaethom gadw’ch atebion.")
}

