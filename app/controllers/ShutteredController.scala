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

package controllers

import javax.inject.{Inject, Singleton}
import play.api.Logger
import play.api.mvc.*
import req.RequestSupport

import scala.concurrent.Future

@Singleton
class ShutteredController @Inject() (
  cc:             ControllerComponents,
  view:           views.html.shuttered,
  requestSupport: RequestSupport
) extends FrontendBaseController(cc):
  private val logger = Logger(this.getClass)

  import requestSupport.*

  def shuttered: Action[AnyContent] = Action.async: (request: Request[?]) =>
    given Request[?] = request

    logger.debug("shutter")
    Future.successful(Ok(view()))
