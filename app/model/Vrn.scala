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

package model

import controllers.ValueClassBinder.valueClassBinder
import model.TypedVrn.MtdVrn
import play.api.libs.functional.syntax.*
import play.api.libs.json.Format
import play.api.mvc.PathBindable

/** Vat Registration Number (Vrn)
  */
final case class Vrn(value: String)

object Vrn:
  given format: Format[Vrn]          = implicitly[Format[String]].inmap(Vrn(_), _.value)
  given vrnBinder: PathBindable[Vrn] = valueClassBinder(_.value)

  private val validVrnKeys: List[String] = List("VRN", "VATRegNo")

  def validVrnKey(vrnKey: String): Boolean = validVrnKeys.contains(vrnKey)

  def isMtdEnrolled(typedVrn: TypedVrn): Boolean = typedVrn match
    case _: MtdVrn => true
    case _         => false

enum TypedVrn(val vrn: Vrn):
  case ClassicVrn(override val vrn: Vrn) extends TypedVrn(vrn)
  case MtdVrn(override val vrn: Vrn)     extends TypedVrn(vrn)
