/*
 * Copyright 2020 HM Revenue & Customs
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

package formaters

import javax.inject.{Inject, Singleton}
import langswitch.LangMessages
import play.api.Logger
import play.api.mvc.Request
import req.RequestSupport

@Singleton
class PeriodFormatter @Inject() (requestSupport: RequestSupport) {

  import requestSupport._

  def formatPeriodKey(periodKey: String)(implicit request: Request[_]) = {
    if (periodKey.size != 4) throw new RuntimeException(s"Invalid length periodkey: ${periodKey}")

    //starts at 0!
    val char3 = periodKey.charAt(2)
    val char4 = periodKey.charAt(3)
    val quarter = periodKey.takeRight(2)

    val year = periodKey.take(2)

    if (char3 == 'Y') {
      formatPeriodKeyYearly(char4, year)
    } else if ((char3 == 'A') && (char4.isLetter)) {
      formatPeriodKeyMonthly(char4, year)
    } else if ((char3.isLetter) && (char4.isDigit)) {
      formatPeriodKeyQuarterly(quarter, year)
    } else ""

  }

  private def formatPeriodKeyMonthly(char4: Char, yearString: String)(implicit request: Request[_]) = {

    Logger.debug(s"Called formatPeriodKeyMonthly with ${char4}, ${yearString}")

    val year = ("20" + yearString).toInt
    val monthString: String = char4 match {
      case 'A' => LangMessages.January.show
      case 'B' => if (isLeapYear(year)) LangMessages.February_leap.show else LangMessages.February.show
      case 'C' => LangMessages.March.show
      case 'D' => LangMessages.April.show
      case 'E' => LangMessages.May.show
      case 'F' => LangMessages.June.show
      case 'G' => LangMessages.July.show
      case 'H' => LangMessages.August.show
      case 'I' => LangMessages.September.show
      case 'J' => LangMessages.October.show
      case 'K' => LangMessages.November.show
      case 'L' => LangMessages.December.show
      case _ => {
        Logger.warn(s"invalid periodKey for formatPeriodKeyMonthly, could not match month: ${char4}")
        ""
      }

    }
    val returnStr = s"${monthString} ${year}"
    Logger.debug(s"Translated to ${returnStr}")
    returnStr
  }

  private def formatPeriodKeyQuarterly(quarter: String, yearString: String)(implicit request: Request[_]) = {

    Logger.debug(s"Called formatPeriodKeyQuarterly with ${quarter}, ${yearString}")
    val year = ("20" + yearString).toInt

    val monthString: String = quarter match {
      case "A4" => LangMessages.JanuaryQuarter.show
      case "B4" => if (isLeapYear(year)) LangMessages.FebruaryQuarter_leap.show else LangMessages.FebruaryQuarter.show
      case "C1" => LangMessages.MarchQuarter.show
      case "A1" => LangMessages.AprilQuarter.show
      case "B1" => LangMessages.MayQuarter.show
      case "C2" => LangMessages.JuneQuarter.show
      case "A2" => LangMessages.JulyQuarter.show
      case "B2" => LangMessages.AugustQuarter.show
      case "C3" => LangMessages.SeptemberQuarter.show
      case "A3" => LangMessages.OctoberQuarter.show
      case "B3" => LangMessages.NovemberQuarter.show
      case "C4" => LangMessages.DecemberQuarter.show
    }
    val returnStr = s"${monthString} ${year}"
    Logger.debug(s"Translated to ${returnStr}")
    returnStr

  }

  private def formatPeriodKeyYearly(char4: Char, year: String)(implicit request: Request[_]) = {

    Logger.debug(s"Called formatPeriodKeyYearly with ${char4}, ${year}")

    val yearString = if (char4 == 'A') s"20$year" else {
      val nextyear = year.toInt + 1
      s"20${nextyear}"
    }
    val monthString: String = char4 match {
      case 'A' => LangMessages.JanToDec.show
      case 'B' => LangMessages.FebToJan.show
      case 'C' => LangMessages.MarToFeb.show
      case 'D' => LangMessages.AprToMar.show
      case 'E' => LangMessages.MayToApr.show
      case 'F' => LangMessages.JunToMay.show
      case 'G' => LangMessages.JulToJun.show
      case 'H' => LangMessages.AugToJul.show
      case 'I' => LangMessages.SepToAug.show
      case 'J' => LangMessages.OctToSep.show
      case 'K' => LangMessages.NovToOct.show
      case 'L' => LangMessages.DecToNov.show
      case _ => {
        Logger.warn(s"invalid periodKey for formatPeriodKeyYearly, could not match month: ${char4}")
        ""
      }
    }
    val returnStr = s"${monthString} ${yearString}"
    Logger.debug(s"Translated to ${returnStr}")
    returnStr
  }

  val isLeapYear = (year: Int) => (((year % 4) == 0) && !(
    ((year % 100) == 0) &&
    !((year % 400) == 0))
  )

}
