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

package controllers.action

import com.google.inject.Inject
import controllers.routes
import model.TypedVrn
import play.api.Logger
import play.api.mvc.Results.Redirect
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

class Actions @Inject() (
    authoriseAction: AuthenticatedAction,
    loggedInAction:  LoggedInAction,
    shutteredAction: ShutteredAction)(implicit ec: ExecutionContext) {

  private val logger = Logger(this.getClass)

  val loggedIn: ActionBuilder[LoggedInRequest, AnyContent] = shutteredAction andThen loggedInAction

  val securedAction: ActionBuilder[AuthenticatedRequest, AnyContent] = shutteredAction andThen authoriseAction

  val securedActionMtdVrnCheck: ActionBuilder[AuthenticatedRequest, AnyContent] = shutteredAction andThen authoriseAction andThen validateMtdVrn

  val securedActionMtdVrnCheckWithoutShutterCheck: ActionBuilder[AuthenticatedRequest, AnyContent] = authoriseAction andThen validateMtdVrn

  private def validateMtdVrn: ActionRefiner[AuthenticatedRequest, AuthenticatedRequest] =
    new ActionRefiner[AuthenticatedRequest, AuthenticatedRequest] {

      override protected def refine[A](request: AuthenticatedRequest[A]): Future[Either[Result, AuthenticatedRequest[A]]] = {

        request.typedVrn match {
          case TypedVrn.MtdVrn(_) => Future.successful(Right(request))
          case _ =>
            logger.debug(s"User logged in with ${request.typedVrn.vrn.value}, this is non-mtd")
            if (request.isPartialMigration) logger.warn("Partially migrated user tried to access MTD authorised VRT")
            Future.successful(Left(Redirect(routes.Controller.nonMtdUser.url)))
        }

      }

      override protected def executionContext: ExecutionContext = ec
    }

}
