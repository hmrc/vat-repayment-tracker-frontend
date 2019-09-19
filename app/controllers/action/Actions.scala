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

import com.google.inject.Inject
import model.TypedVrn.{ClassicVrn, MtdVrn}
import model.{TypedVrn, Vrn}
import play.api.Logger
import play.api.mvc._
import uk.gov.hmrc.auth.core.{AuthorisedFunctions, Enrolment, EnrolmentIdentifier, Enrolments}

import scala.concurrent.{ExecutionContext, Future}

class Actions @Inject() (
    authoriseAction:      AuthenticatedAction,
    af:                   AuthorisedFunctions,
    cc:                   ControllerComponents,
    unhappyPathResponses: UnhappyPathResponses)(implicit ec: ExecutionContext) {

  def securedAction(vrn: Vrn): ActionBuilder[AuthenticatedRequest, AnyContent] = authoriseAction andThen validateVrn(vrn)

  def securedActionFromSession: ActionBuilder[AuthenticatedRequest, AnyContent] = authoriseAction andThen validateVrnFromSession

  private def validateVrnFromSession: ActionRefiner[AuthenticatedRequest, AuthenticatedRequest] =
    new ActionRefiner[AuthenticatedRequest, AuthenticatedRequest] {

      override protected def refine[A](request: AuthenticatedRequest[A]): Future[Either[Result, AuthenticatedRequest[A]]] = {
        val vrn = request.sessionVrn
        vrnCheck(request, vrn)

      }

      override protected def executionContext: ExecutionContext = ec
    }

  private def validateVrn(vrn: Vrn): ActionRefiner[AuthenticatedRequest, AuthenticatedRequest] =
    new ActionRefiner[AuthenticatedRequest, AuthenticatedRequest] {

      override protected def refine[A](request: AuthenticatedRequest[A]): Future[Either[Result, AuthenticatedRequest[A]]] = {

        vrnCheck(request, vrn)
      }
      override protected def executionContext: ExecutionContext = ec
    }

  private def vrnCheck[A](request: AuthenticatedRequest[A], vrn: Vrn): Future[Either[Result, AuthenticatedRequest[A]]] = {
    request.enrolmentsVrn match {
      case Some(typedVrn) => {
        if (typedVrn.vrn.value == vrn.value) {
          Future.successful(Right(request))
        } else {
          Logger.debug(s"""User logged in and passed vrn: ${vrn.value}, has enrolment for ${typedVrn.vrn.value}""")
          implicit val req: AuthenticatedRequest[_] = request
          Future.successful(Left(unhappyPathResponses.unauthorised(vrn)))
        }
      }
      case None => {
        Logger.debug(s"""User logged in and passed vrn: ${vrn.value}, but have not enrolments""")
        implicit val req: AuthenticatedRequest[_] = request
        Future.successful(Left(unhappyPathResponses.unauthorised))
      }
    }
  }

}
