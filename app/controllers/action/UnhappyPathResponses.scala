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

import javax.inject.{Inject, Singleton}
import config.ViewConfig
import controllers.ErrorHandler
import langswitch.ErrorMessages
import play.api.mvc.Results.{NotFound, Unauthorized}
import play.api.mvc.Request
import req.RequestSupport

@Singleton
class UnhappyPathResponses @Inject() (
    viewConfig:     ViewConfig,
    errorHandler:   ErrorHandler,
    requestSupport: RequestSupport) {

  import requestSupport._

  def notFound(implicit request: Request[_]) = NotFound(
    errorHandler.standardErrorTemplate(
      ErrorMessages.pageNotFound404Title.show,
      ErrorMessages.pageNotFound404Heading.show,
      ErrorMessages.pageNotFound404Message.show))

  def unauthorised(buttonLink: String)(implicit request: Request[_]) = Unauthorized(
    errorHandler.standardErrorTemplate(
      ErrorMessages.authProblemTitle.show,
      ErrorMessages.authProblemHeading.show,
      ErrorMessages.authProblemMessage.show))

  def unauthorised(implicit request: Request[_]) = Unauthorized(
    errorHandler.standardErrorTemplate(
      ErrorMessages.`You do not have access to this service`.show,
      ErrorMessages.`You do not have access to this service`.show,
      ""))

}
