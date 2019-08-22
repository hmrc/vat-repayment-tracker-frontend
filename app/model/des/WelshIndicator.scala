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

package model.des

import play.api.libs.functional.syntax._
import play.api.libs.json.Format

/**
 * Indicates whether a customer has chosen the Welsh correspondence and language option.
 * Stored in ETMP with their customer details and returned from the DES 1363 API
 */
sealed trait WelshIndicator {
  def isWelsh = this match {
    case Welsh   => true
    case English => false
  }
}

case object Welsh extends WelshIndicator

case object English extends WelshIndicator

object WelshIndicator {
  val welsh = Welsh
  val english = English

  implicit val format: Format[WelshIndicator] = implicitly[Format[Boolean]].inmap(
    welshIndicator => if (welshIndicator) Welsh else English,
    {
      case Welsh   => true
      case English => false
    })
}
