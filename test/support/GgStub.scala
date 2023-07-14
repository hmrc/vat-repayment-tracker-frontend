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

import com.github.tomakehurst.wiremock.client.WireMock._
import com.github.tomakehurst.wiremock.stubbing.StubMapping
import model.Vrn
import org.scalatest.matchers.should.Matchers

object GgStub extends Matchers {

  def signInPage(port: Int, vrn: Vrn): StubMapping = {
    stubFor(
      get(urlPathEqualTo(signInPath))
        .withQueryParam("continue", equalTo(
          s"""http://localhost:$port/vat-repayment-tracker-frontend/show-results/vrn/${vrn.value}"""
        ))
        .withQueryParam("origin", equalTo(s"""pay-online"""))
        .willReturn(
          aResponse()
            .withStatus(200)
            .withBody(
              singInPageSource)))
  }

  val singInPageSource = s"""
<html>
  <head/>
  <body>
    Sign in using Government Gateway
  </body>
</html>
    """
  val signInPath = "/auth-login-stub/gg-sign-in"

}

