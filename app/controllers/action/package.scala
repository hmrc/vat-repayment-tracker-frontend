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

package controllers

import model.EnrolmentKeys.{mtdVatEnrolmentKey, vatDecEnrolmentKey, vatVarEnrolmentKey}
import model.{TypedVrn, Vrn}
import model.TypedVrn.{ClassicVrn, MtdVrn}
import uk.gov.hmrc.auth.core.{Enrolment, EnrolmentIdentifier, Enrolments}

package object action {

  def extractVrn(enrolments: Enrolments): Option[TypedVrn] = {
    val mtd = enrolments.enrolments.collectFirst {
      case Enrolment(key, identifiers, _, _) if key == mtdVatEnrolmentKey =>
        identifiers.collectFirst { case EnrolmentIdentifier(k, vrn) if Vrn.validVrnKey(k) => MtdVrn(Vrn(vrn)) }
    }.flatten

    val nonMtd = enrolments.enrolments.collectFirst {
      case Enrolment(key, identifiers, _, _) if Set(vatDecEnrolmentKey, vatVarEnrolmentKey).contains(key) =>
        identifiers.collectFirst { case EnrolmentIdentifier(k, vrn) if Vrn.validVrnKey(k) => ClassicVrn(Vrn(vrn)) }
    }.flatten

    (mtd, nonMtd) match {
      case (Some(mdt), None)    => Some(mdt)
      case (None, Some(nonMdt)) => Some(nonMdt)
      case (Some(mdt), Some(_)) => Some(mdt)
      case _                    => None
    }
  }

}
