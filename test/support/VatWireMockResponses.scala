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

import com.github.tomakehurst.wiremock.client.WireMock.{aResponse, get, stubFor, urlEqualTo}
import com.github.tomakehurst.wiremock.stubbing.StubMapping
import model.Vrn

object VatWireMockResponses {

  def calendarOk(vrn: Vrn, isCurrent: Boolean = true): StubMapping = {
    stubFor(get(urlEqualTo(s"/vat/${vrn.value}/calendar"))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(
          if (isCurrent) VatData.calendarCurrentJson.toString() else VatData.calendarJson.toString()
        )))

  }

  def calendar404(vrn: Vrn): StubMapping = {
    stubFor(get(urlEqualTo(s"/vat/${vrn.value}/calendar"))
      .willReturn(aResponse()
        .withStatus(404)
      ))
  }

  def designatoryDetailsOk(vrn: Vrn): StubMapping = {
    stubFor(get(urlEqualTo(s"/vat/${vrn.value}/designatoryDetails"))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(
          VatData.vatDesignatoryDetailsAddressJson.toString())))

  }

  def designatoryDetails404(vrn: Vrn): StubMapping = {
    stubFor(get(urlEqualTo(s"/vat/${vrn.value}/designatoryDetails"))
      .willReturn(aResponse()
        .withStatus(404)
      ))
  }
}

