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

import scala.concurrent.{ExecutionContext, Future}

class ShutteredAction @Inject() (
    viewConfig: ViewConfig,
    cc:         MessagesControllerComponents) extends ActionBuilder[Request, AnyContent] {

  private val logger = Logger(this.getClass)

  def invokeBlock[A](request: Request[A], block: Request[A] => Future[Result]): Future[Result] =
    if (viewConfig.isShuttered) {
      logger.debug("Shuttered")
      Future.successful(Redirect(routes.ShutteredController.shuttered.url))
    } else {
      logger.debug("Not shuttered")
      block(request)
    }

  override def parser: BodyParser[AnyContent] = cc.parsers.defaultBodyParser

  override protected def executionContext: ExecutionContext = cc.executionContext

}
