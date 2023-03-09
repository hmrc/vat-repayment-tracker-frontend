/*
 * Copyright 2023 HM Revenue & Customs
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

package langswitch

import play.api.i18n.I18nSupport
import javax.inject.Inject
import play.api.mvc._
import play.mvc.Http.HeaderNames

import scala.concurrent.ExecutionContext

class LanguageSwitchController @Inject() (
    errorTemplate: views.html.error.error_template,
    cc:            ControllerComponents)(
    implicit
    ec: ExecutionContext)
  extends AbstractController(cc) with I18nSupport {

  def switchToLanguage(language: Language): Action[AnyContent] = cc.actionBuilder { implicit request =>
    val maybeReferrer: Option[String] =
      request
        .headers
        .get(HeaderNames.REFERER)

    val result: Result = maybeReferrer.fold(noReferrerContent(language))(Redirect(_))
    result.withLang(language.toPlayLang)
  }

  def noReferrerContent(l: Language)(implicit request: Request[_]): Result = Ok(
    errorTemplate(
      pageTitle = missingRefererHeader(l),
      heading   = missingRefererHeader(l),
      message   = "This url has to have parent page."
    )
  )

  private def missingRefererHeader(language: Language): String =
    s"Missing referer header - language changed to ${language.label}"

}

