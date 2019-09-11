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

package model

import play.api.libs.functional.syntax._
import play.api.libs.json.Format
import play.api.mvc.PathBindable
import controllers.ValueClassBinder.valueClassBinder

/**
 * Vat Registration Number (Vrn)
 */
final case class Vrn(value: String)

object Vrn {

  implicit val format: Format[Vrn] = implicitly[Format[String]].inmap(Vrn(_), _.value)
  implicit val vrnBinder: PathBindable[Vrn] = valueClassBinder(_.value)

}
