/*
 * Copyright 2024 HM Revenue & Customs
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

import javax.inject.Singleton
import play.api.Logger
import play.api.i18n.Messages

@Singleton
class PeriodFormatter {

  private val logger = Logger(this.getClass)

  def formatPeriodKey(periodKey: String)(implicit messages: Messages): String = {
    if (periodKey.length != 4) throw new RuntimeException(s"Invalid length periodkey: $periodKey")

    //starts at 0!
    val char3 = periodKey.charAt(2)
    val char4 = periodKey.charAt(3)
    val quarter = periodKey.takeRight(2)

    val year = periodKey.take(2)

    if (char3 == 'Y') {
      formatPeriodKeyYearly(char4, year)
    } else if ((char3 == 'A') && char4.isLetter) {
      formatPeriodKeyMonthly(char4, year)
    } else if (char3.isLetter && char4.isDigit) {
      formatPeriodKeyQuarterly(quarter, year)
    } else ""

  }

  private def formatPeriodKeyMonthly(char4: Char, yearString: String)(implicit messages: Messages) = {

    logger.debug(s"Called formatPeriodKeyMonthly with $char4, $yearString")

    val year = ("20" + yearString).toInt
    val monthString: String = char4 match {
      case 'A' => Messages("month.january")
      case 'B' => if (isLeapYear(year)) Messages("month.february_leap") else Messages("month.february")
      case 'C' => Messages("month.march")
      case 'D' => Messages("month.april")
      case 'E' => Messages("month.may")
      case 'F' => Messages("month.june")
      case 'G' => Messages("month.july")
      case 'H' => Messages("month.august")
      case 'I' => Messages("month.september")
      case 'J' => Messages("month.october")
      case 'K' => Messages("month.november")
      case 'L' => Messages("month.december")
      case _ =>
        logger.warn(s"invalid periodKey for formatPeriodKeyMonthly, could not match month: $char4")
        ""

    }
    val returnStr = s"$monthString $year"
    logger.debug(s"Translated to $returnStr")
    returnStr
  }

  private def formatPeriodKeyQuarterly(quarter: String, yearString: String)(implicit messages: Messages) = {

    logger.debug(s"Called formatPeriodKeyQuarterly with $quarter, $yearString")
    val year = ("20" + yearString).toInt

    val monthString: String = quarter match {
      case "A4" => Messages("quarter.JanuaryQuarter")
      case "B4" => if (isLeapYear(year)) Messages("quarter.FebruaryQuarter_leap") else Messages("quarter.FebruaryQuarter")
      case "C1" => Messages("quarter.MarchQuarter")
      case "A1" => Messages("quarter.AprilQuarter")
      case "B1" => Messages("quarter.MayQuarter")
      case "C2" => Messages("quarter.JuneQuarter")
      case "A2" => Messages("quarter.JulyQuarter")
      case "B2" => Messages("quarter.AugustQuarter")
      case "C3" => Messages("quarter.SeptemberQuarter")
      case "A3" => Messages("quarter.OctoberQuarter")
      case "B3" => Messages("quarter.NovemberQuarter")
      case "C4" => Messages("quarter.DecemberQuarter")
    }
    val returnStr = s"$monthString $year"
    logger.debug(s"Translated to $returnStr")
    returnStr

  }

  private def formatPeriodKeyYearly(char4: Char, year: String)(implicit messages: Messages) = {

    logger.debug(s"Called formatPeriodKeyYearly with $char4, $year")

    val yearString = if (char4 == 'A') s"20$year" else {
      val nextyear = year.toInt + 1
      s"20$nextyear"
    }
    val monthString: String = char4 match {
      case 'A' => Messages("date.JanToDec")
      case 'B' => Messages("date.FebToJan")
      case 'C' => Messages("date.MarToFeb")
      case 'D' => Messages("date.AprToMar")
      case 'E' => Messages("date.MayToApr")
      case 'F' => Messages("date.JunToMay")
      case 'G' => Messages("date.JulToJun")
      case 'H' => Messages("date.AugToJul")
      case 'I' => Messages("date.SepToAug")
      case 'J' => Messages("date.OctToSep")
      case 'K' => Messages("date.NovToOct")
      case 'L' => Messages("date.DecToNov")
      case _ =>
        logger.warn(s"invalid periodKey for formatPeriodKeyYearly, could not match month: $char4")
        ""
    }
    val returnStr = s"$monthString $yearString"
    logger.debug(s"Translated to $returnStr")
    returnStr
  }

  val isLeapYear: Int => Boolean = (year: Int) => ((year % 4) == 0) && !(
    ((year % 100) == 0) &&
    !((year % 400) == 0))

}
