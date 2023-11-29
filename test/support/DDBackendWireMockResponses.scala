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
import model.dd.CreateVATJourneyRequest

object DDBackendWireMockResponses {

  private val startJourneyExpectedUrl = "/direct-debit-backend/vc/vat/journey/start"

  val nextUrl =
    "https://www.qa.tax.service.gov.uk/direct-debit/enter-bank-details/11f4e440-1db1-4138-9a66-cb73db12174e"

  def ddOk: StubMapping =
    stubFor(post(urlEqualTo(startJourneyExpectedUrl))
      .willReturn(aResponse()
        .withStatus(201)
        .withBody(s"""{"nextUrl":"$nextUrl"}""")))

  def verifyStartJourneyCalled(expectedRequest: CreateVATJourneyRequest) =
    verify(
      exactly(1),
      postRequestedFor(urlEqualTo(startJourneyExpectedUrl))
        .withRequestBody(equalToJson(
          s"""{
             |  "userId": "${expectedRequest.userId}",
             |  "userIdType": "${expectedRequest.userIdType}",
             |  "returnUrl": "${expectedRequest.returnUrl}",
             |  "backUrl": "${expectedRequest.backUrl}"
             |}
             |""".stripMargin
        ))
    )

}
