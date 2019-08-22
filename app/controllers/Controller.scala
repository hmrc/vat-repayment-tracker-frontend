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

package controllers

import config.ViewConfig
import javax.inject.{Inject, Singleton}
import play.api.Logger
import play.api.i18n.Messages
import play.api.mvc._
import uk.gov.hmrc.auth.core.{AuthorisationException, AuthorisedFunctions}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class Controller @Inject()(mcc: MessagesControllerComponents, implicit val viewConfig: ViewConfig, page1: views.html.page1,
                           val authConnector: FrontendAuthConnector, errorHandler: ErrorHandler)(implicit ec: ExecutionContext)

  extends FrontendController(mcc) with AuthorisedFunctions {

  def showResults: Action[AnyContent] = Action.async { implicit request =>
    authorised() {
      Future.successful(Ok(page1()))
    }.recoverWith {
      case e: AuthorisationException =>
        Logger.debug(s"Unauthorised because of ${e.reason}, $e")
        Future.successful(Unauthorized(
          errorHandler.standardErrorTemplate(
            Messages("unauthorised.title"),
            Messages("unauthorised.heading"),
            "")))

    }
  }

}
