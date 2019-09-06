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

package langswitch

import scala.language.implicitConversions

case class Message(
    english: String,
    welsh:   Option[String]
) {

}

object Message {

  def apply(english: String, welsh: String = null): Message = Message(english, Option(welsh))

  implicit def show(message: Message)(implicit lang: Language): String = lang match {
    case Languages.English => message.english
    case Languages.Welsh   => message.welsh.getOrElse(message.english)
  }
}

object MessagesA {

  val `Some Message`: Message = Message("someMessage")
  val `Pay your tax`: Message = Message("Pay your tax", "Talwch eich treth")

}

object MessagesB {
  val `Pay your tax2`: Message = Message("Pay your tax2", "Talwch eich treth2")
}
