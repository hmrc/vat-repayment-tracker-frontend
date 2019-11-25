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

package controllers.action

import model.TypedVrn.{ClassicVrn, MtdVrn}
import model.{TypedVrn, Vrn}
import play.api.mvc.{Request, WrappedRequest}
import uk.gov.hmrc.auth.core.{Enrolment, EnrolmentIdentifier, Enrolments}

final class AuthenticatedRequest[A](val request:    Request[A],
                                    val enrolments: Enrolments
) extends WrappedRequest[A](request) {

  val enrolmentsVrn: Option[TypedVrn] = {
    import model.EnrolmentKeys._

    enrolments.enrolments.collectFirst {
      case Enrolment(key, identifiers, _, _) if key == mtdVatEnrolmentKey =>
        identifiers.collectFirst { case EnrolmentIdentifier(k, vrn) if Vrn.validVrnKey(k) => MtdVrn(Vrn(vrn)) }

      case Enrolment(key, identifiers, _, _) if Set(vatDecEnrolmentKey, vatVarEnrolmentKey).contains(key) =>
        identifiers.collectFirst { case EnrolmentIdentifier(k, vrn) if Vrn.validVrnKey(k) => ClassicVrn(Vrn(vrn)) }
    }.flatten
  }

  def sessionVrn = request.session.get("vrn") match {
    case Some(vrnString) => Vrn(vrnString)
    case None            => throw new RuntimeException("Could not get VRN from session")
  }

  val typedVrn: TypedVrn = {
    enrolmentsVrn match {
      case Some(x) => x
      case None    => throw new RuntimeException("No vrn available")
    }
  }
}
