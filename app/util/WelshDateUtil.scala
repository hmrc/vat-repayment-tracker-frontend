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

package util

import cats.syntax.all._

import scala.util.Try

object WelshDateUtil {
  private val months: Map[String, String] = Map(
    "January" -> "Ionawr",
    "February" -> "Chwefror",
    "March" -> "Mawrth",
    "April" -> "Ebrill",
    "May" -> "Mai",
    "June" -> "Mehefin",
    "July" -> "Gorffennaf",
    "August" -> "Awst",
    "September" -> "Medi",
    "October" -> "Hydref",
    "November" -> "Tachwedd",
    "December" -> "Rhagfyr"
  )

  implicit class StringOps(date: String) {
    def welshMonth: String = {
        def updateString(d: String, m: Map[String, String]) = {
          val monthToChange: Map[String, String] = m.filter(_._1 === d.replace("am", "").replace("pm", "").filter(_.isLetter))
          d.replace(monthToChange.headOption.fold(d)(_._1), monthToChange(d.replace("am", "").replace("pm", "").filter(_.isLetter)))
        }
      Try(updateString(date, months)).getOrElse(date)
    }
  }
}
