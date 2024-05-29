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
import config.ViewConfig
import controllers.routes
import play.api.Logger
import play.api.mvc.Results._
import play.api.mvc._
import uk.gov.hmrc.auth.core.retrieve.v2.Retrievals
import uk.gov.hmrc.auth.core.{AuthorisationException, AuthorisedFunctions, NoActiveSession}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.http.HeaderCarrierConverter

import scala.concurrent.{ExecutionContext, Future}

class LoggedInAction @Inject() (
    af:         AuthorisedFunctions,
    viewConfig: ViewConfig,
    cc:         MessagesControllerComponents)(implicit ec: ExecutionContext) extends ActionBuilder[LoggedInRequest, AnyContent] {

  private val logger = Logger(this.getClass)

  override def invokeBlock[A](request: Request[A], block: LoggedInRequest[A] => Future[Result]): Future[Result] = {
    implicit val hc: HeaderCarrier = HeaderCarrierConverter.fromRequestAndSession(request, request.session)

    af.authorised().retrieve(Retrievals.allEnrolments) { enrolments =>
      block(new LoggedInRequest(request, extractVrn(enrolments)))
    }.recover {
      case _: NoActiveSession =>
        Redirect(viewConfig.loginUrl, Map("continue" -> Seq(viewConfig.frontendBaseUrl + request.uri), "origin" -> Seq("pay-online")))
      case e: AuthorisationException =>
        logger.debug(s"Unauthorised because of ${e.reason}, $e")
        Redirect(routes.Controller.nonMtdUser.url)
    }
  }

  override def parser: BodyParser[AnyContent] = cc.parsers.defaultBodyParser

  override protected def executionContext: ExecutionContext = cc.executionContext

}
