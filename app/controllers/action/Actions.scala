/*
 * Copyright 2020 HM Revenue & Customs
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
import config.ViewConfig
import model.TypedVrn
import play.api.Logger
import play.api.mvc.Results.Redirect
import play.api.mvc._
import uk.gov.hmrc.auth.core.AuthorisedFunctions

import scala.concurrent.{ExecutionContext, Future}

class Actions @Inject() (
    authoriseAction:      AuthenticatedAction,
    af:                   AuthorisedFunctions,
    cc:                   ControllerComponents,
    viewConfig:           ViewConfig,
    unhappyPathResponses: UnhappyPathResponses,
    loggedInAction:       LoggedInAction)(implicit ec: ExecutionContext) {

  def loggedIn: ActionBuilder[Request, AnyContent] = loggedInAction

  def securedAction: ActionBuilder[AuthenticatedRequest, AnyContent] = authoriseAction

  def securedActionMtdVrnCheck: ActionBuilder[AuthenticatedRequest, AnyContent] = authoriseAction andThen validateMtdVrn

  private def validateMtdVrn: ActionRefiner[AuthenticatedRequest, AuthenticatedRequest] =
    new ActionRefiner[AuthenticatedRequest, AuthenticatedRequest] {

      override protected def refine[A](request: AuthenticatedRequest[A]): Future[Either[Result, AuthenticatedRequest[A]]] = {

        request.typedVrn match {
          case TypedVrn.MtdVrn(vrn) => Future.successful(Right(request))
          case _ =>
            Logger.debug(s"""User logged in with ${request.typedVrn.vrn.value}, this is non-mtd""")
            if (request.isPartialMigration) Logger.warn("Partially migrated user tried to access MTD authorised VRT")
            implicit val req: AuthenticatedRequest[_] = request
            Future.successful(Left(Redirect(viewConfig.nonMtdUser)))
        }

      }

      override protected def executionContext: ExecutionContext = ec
    }

}
