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
import model.payapi.SpjRequestBtaVat

object PayApiWireMockResponses {

  private val expectedStartJourneyUrl = "/pay-api/bta/vat/journey/start"

  val nextUrl = "https://www.tax.service.gov.uk/pay/TestJourneyId-44f9-ad7f-01e1d3d8f151/start"

  def payOk: StubMapping =
    stubFor(post(urlEqualTo(expectedStartJourneyUrl))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(
          s"""
          |{
          |  "journeyId": "TestJourneyId-44f9-ad7f-01e1d3d8f151",
          |  "nextUrl": "$nextUrl"
          |}
    """.stripMargin)))

  def verifyStartJourneyCalled(expectedRequest: SpjRequestBtaVat) =
    verify(
      exactly(1),
      postRequestedFor(urlEqualTo(expectedStartJourneyUrl))
        .withRequestBody(equalToJson(
          s"""{
            |  "amountInPence": ${expectedRequest.amountInPence.toString},
            |  "returnUrl": "${expectedRequest.returnUrl}",
            |  "backUrl": "${expectedRequest.backUrl}",
            |  "vrn": "${expectedRequest.vrn.value}"
            |}
            |""".stripMargin
        ))
    )

}

