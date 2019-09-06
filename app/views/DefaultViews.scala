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

package views

import javax.inject.Inject
import play.api.i18n.Messages
import play.api.mvc.Request
import play.twirl.api.HtmlFormat
import req.RequestSupport

class DefaultViews @Inject() (
    requestSupport: RequestSupport,
    errorPage:      _root_.views.html.error.auth_error_page,
    errorTemplate:  _root_.views.html.error.error_template,
    error4xx:       _root_.views.html.error.error_4xx,
    error5xx:       _root_.views.html.error.error_5xx) {

  import requestSupport._

  def error5xx(status: Int = 500)(implicit request: Request[_]): HtmlFormat.Appendable =
    error5xx(
      Messages("error.InternalServerError500.title"),
      Messages("error.InternalServerError500.heading"),
      Messages("error.InternalServerError500.message"), status)

  def error4xx(implicit request: Request[_]): HtmlFormat.Appendable =
    error4xx(
      Messages("error.badRequest400.title"),
      Messages("error.badRequest400.heading"),
      Messages("error.badRequest400.message"))

  def notFound404(implicit request: Request[_]): HtmlFormat.Appendable =
    error4xx(
      Messages("error.pageNotFound404.title"),
      Messages("error.pageNotFound404.heading"),
      Messages("error.pageNotFound404.message"))

  def unauthorised()(implicit request: Request[_]): HtmlFormat.Appendable =
    errorPage()

  /**
   * These views don't have welsh translation. They address unusual cases when user did something nasty with browser
   * like changing forms in html, etc
   */
  object ForceBrowsing {

    def error4xx(
        pageTitle: String,
        heading:   String,
        message:   String)(implicit request: Request[_]) =
      errorTemplate(
        pageTitle,
        heading,
        message)

  }

}
