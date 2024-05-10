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

package support

import com.gargoylesoftware.htmlunit.WebClient
import com.github.tomakehurst.wiremock.client.WireMock.{aResponse, get, stubFor, urlPathEqualTo}
import com.github.tomakehurst.wiremock.stubbing.StubMapping
import config.ViewConfig
import org.openqa.selenium.WebDriver
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import play.api.http.HeaderNames
import play.api.mvc.{CookieHeaderEncoding, Session, SessionCookieBaker}
import uk.gov.hmrc.crypto.PlainText
import uk.gov.hmrc.http.SessionKeys
import uk.gov.hmrc.play.bootstrap.frontend.filters.crypto.SessionCookieCrypto

class CustomHtmlUnitDriver extends HtmlUnitDriver {
  override def modifyWebClient(client: WebClient): WebClient = {

    val modifiedClient: WebClient = super.modifyWebClient(client)
    modifiedClient.getOptions.setThrowExceptionOnScriptError(false)
    modifiedClient.getOptions.setRedirectEnabled(true)
    modifiedClient.getOptions.setJavaScriptEnabled(false)
    modifiedClient
  }
}

class BrowserSpec extends ItSpec {
  lazy val viewConfig: ViewConfig = fakeApplication().injector.instanceOf[ViewConfig]

  protected implicit val webDriver: WebDriver = new CustomHtmlUnitDriver

  lazy val webdriverUrl: String = s"http://localhost:$port"

  def goToViaPath(path: String): Unit =
    webDriver.navigate().to(s"$webdriverUrl$path")

  lazy val cookieCrypto: SessionCookieCrypto = injector.instanceOf[SessionCookieCrypto]
  lazy val cookieBaker: SessionCookieBaker = injector.instanceOf[SessionCookieBaker]
  lazy val cookieHeaderEncoding: CookieHeaderEncoding = injector.instanceOf[CookieHeaderEncoding]

  protected def login(): Unit = {
    logInResponse(cookieCrypto, cookieBaker)
    webDriver.navigate().to(s"http://localhost:${WireMockSupport.port}$loginPath")
  }

  def logInResponse(
      cookieCrypto: SessionCookieCrypto,
      baker:        SessionCookieBaker
  ): StubMapping = {
    //Implementation based on CookieCryptoFilter trait and auth-login-stub project
    val session = Session(Map(
      SessionKeys.authToken -> "Token"
    ))

    val rawCookie = baker.encodeAsCookie(session)
    val crypted = cookieCrypto.crypto.encrypt(PlainText(rawCookie.value))
    val cookie = rawCookie.copy(value = crypted.value)

    val headerValue: String = cookieHeaderEncoding.encodeSetCookieHeader(List(cookie))

    stubFor(
      get(urlPathEqualTo(loginPath))
        .willReturn(
          aResponse()
            .withStatus(200)
            .withBody("You have been logged in")
            .withHeader(HeaderNames.SET_COOKIE, headerValue)
        )
    )
  }

  val loginPath = "/login"
}
