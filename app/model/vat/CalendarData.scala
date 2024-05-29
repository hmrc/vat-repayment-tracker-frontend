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

package model.vat

import java.time.LocalDate
import java.time.chrono.ChronoLocalDate

import formaters.CommonFormatter
import play.api.libs.json.{Json, OFormat}

case class CalendarData(
    currentPeriod:   Option[CalendarPeriod],
    previousPeriods: Seq[CalendarPeriod]
) {
  def countReturns: Int = currentPeriod.count(_.returnReceivedDate.isDefined) + previousPeriods.count(_.returnReceivedDate.isDefined)

  def latestReceivedOnFormatted: String = {
    latestReceivedOn match {
      case Some(x) => CommonFormatter.formatDate(x)
      case None    => ""
    }
  }

  private def latestReceivedOn: Option[LocalDate] = {

    currentPeriod match {
      case Some(found) => found.returnReceivedDate match {
        case Some(rd) => Some(rd)
        case None     => mostRecentPreviousReceivedData
      }
      case None => mostRecentPreviousReceivedData
    }

  }

  private def mostRecentPreviousReceivedData: Option[LocalDate] = {
    implicit val localDateOrdering: Ordering[LocalDate] = Ordering.by(identity[ChronoLocalDate])
    previousPeriods.flatMap(_.returnReceivedDate).sorted.reverse.headOption
  }

}

object CalendarData {
  implicit val formats: OFormat[CalendarData] = Json.format[CalendarData]
}

