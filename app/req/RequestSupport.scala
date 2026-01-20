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

package req

import javax.inject.Inject
import play.api.i18n.*
import play.api.mvc.{Request, RequestHeader, Result}
import uk.gov.hmrc.http.{HeaderCarrier, SessionKeys}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendHeaderCarrierProvider

import scala.concurrent.Future

/** Repeating the pattern which was brought originally by play-framework and putting some more data which can be derived
  * from a request
  *
  * Use it to provide HeaderCarrier, Lang, or Messages
  */
class RequestSupport @Inject() (override val messagesApi: MessagesApi) extends I18nSupport {

  implicit def hc(using request:      Request[?]): HeaderCarrier = RequestSupport.hc
  implicit def toFutureResult(result: Result): Future[Result]    = Future.successful(result)
  def lang(using messages:            Messages): Lang            = messages.lang
}

object RequestSupport {

  implicit def hc[A](using request: Request[A]): HeaderCarrier = HcProvider.headerCarrier

  def isLoggedIn(using request: RequestHeader): Boolean = request.session.get(SessionKeys.authToken).isDefined

  /** This is because we want to give responsibility of creation of [[HeaderCarrier]] to the platform code. If they
    * refactor how hc is created our code will pick it up automatically.
    */
  private object HcProvider extends FrontendHeaderCarrierProvider {
    def headerCarrier(using request: Request[?]): HeaderCarrier = super.hc(request) // check ***
  }

  object timeoutDialog {
    def message(using request: Request[?], messages: Messages): String =
      if (RequestSupport.isLoggedIn) Messages("timeout_dialog.message.isloggedin")
      else Messages("timeout_dialog.message.timeout")

    def keepAlive(using request: Request[?], messages: Messages): String =
      if (RequestSupport.isLoggedIn) Messages("stay_signed_in")
      else Messages("continue")

    def signOutOrDeleteYourAnswers(using request: Request[?], messages: Messages): String =
      if (RequestSupport.isLoggedIn) Messages("govuk_wrapper.sign_out")
      else Messages("govuk_wrapper.delete_your_answers")
  }

}
