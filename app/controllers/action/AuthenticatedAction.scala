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
import connectors.PaymentsOrchestratorConnector
import controllers.routes
import model.TypedVrn.ClassicVrn
import model.des.CustomerInformation
import model.{TypedVrn, Vrn}
import play.api.Logger
import play.api.mvc.Results._
import play.api.mvc._
import uk.gov.hmrc.auth.core._
import uk.gov.hmrc.auth.core.retrieve.v2.Retrievals
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.http.HeaderCarrierConverter

import scala.concurrent.{ExecutionContext, Future}

class AuthenticatedAction @Inject() (
    af:           AuthorisedFunctions,
    viewConfig:   ViewConfig,
    cc:           MessagesControllerComponents,
    orchestrator: PaymentsOrchestratorConnector)(implicit ec: ExecutionContext) extends ActionBuilder[AuthenticatedRequest, AnyContent] {

  private val logger = Logger(this.getClass)

  private def isPartial(customer: Option[CustomerInformation]): Boolean = {
    if (viewConfig.isShuttered) false
    else {
      customer match {
        case Some(x) => x.isPartiallyMigrated
        case None    => false
      }
    }
  }

  override def invokeBlock[A](request: Request[A], block: AuthenticatedRequest[A] => Future[Result]): Future[Result] = {
    implicit val hc: HeaderCarrier = HeaderCarrierConverter.fromRequestAndSession(request, request.session)
    implicit val r: Request[A] = request

    af.authorised().retrieve(Retrievals.allEnrolments) { enrolments =>
      val typedVrn: TypedVrn = extractVrn(enrolments).getOrElse(throw InsufficientEnrolments())

      orchestrator.getCustomerData(typedVrn.vrn).flatMap { customer =>
        if (customer.exists(_.isDeregistered)) {
          logger.debug(s"Unauthorised because VAT registration cancelled")
          Future(Redirect(routes.Controller.deregistered.url))
        } else if (Vrn.isMtdEnroled(typedVrn) && isPartial(customer)) {
          block(new AuthenticatedRequest(request, enrolments, ClassicVrn(typedVrn.vrn), true))
        } else block(new AuthenticatedRequest(request, enrolments, typedVrn, false))
      }

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
