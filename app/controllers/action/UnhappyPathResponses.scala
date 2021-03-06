/*
 * Copyright 2021 HM Revenue & Customs
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

import controllers.ErrorHandler
import javax.inject.{Inject, Singleton}
import model.Vrn
import play.api.i18n.Messages
import play.api.mvc.{Request, Result}
import play.api.mvc.Results.Unauthorized
import req.RequestSupport

@Singleton
class UnhappyPathResponses @Inject() (
    errorHandler:   ErrorHandler,
    requestSupport: RequestSupport) {

  def unauthorised(implicit request: Request[_], message: Messages): Result = Unauthorized(
    errorHandler.standardErrorTemplate(
      Messages("unhappy_path_responses.no_access"),
      Messages("unhappy_path_responses.no_access"),
      ""))

  def unauthorised(vrn: Vrn)(implicit request: Request[_], message: Messages): Result = Unauthorized(
    errorHandler.standardErrorTemplate(
      Messages("unhappy_path_responses.no_access"),
      Messages("unhappy_path_responses.no_access_vrn", vrn),
      ""))

}
