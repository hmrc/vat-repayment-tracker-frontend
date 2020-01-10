/*
 * Copyright 2020 HM Revenue & Customs
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
import play.api.libs.json.JsObject

object PayApiWireMockResponses {

  def payOk: StubMapping = {
    stubFor(post(urlEqualTo("/pay-api/bta/vat/journey/start"))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(
          """
      {
        "journeyId": "TestJourneyId-44f9-ad7f-01e1d3d8f151",
        "nextUrl": "https://www.tax.service.gov.uk/pay/TestJourneyId-44f9-ad7f-01e1d3d8f151/start"
      }
    """.stripMargin)))
  }

}

