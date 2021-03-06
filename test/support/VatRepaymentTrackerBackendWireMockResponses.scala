/*
 * Copyright 2021 HM Revenue & Customs
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
import model.des.RiskingStatus
import model.{PeriodKey, Vrn}
import play.api.http.Status

object VatRepaymentTrackerBackendWireMockResponses {

  def storeOk(): StubMapping = {
    stubFor(
      post(urlPathEqualTo("/vat-repayment-tracker-backend/store"))
        .willReturn(
          aResponse()
            .withBody("")
            .withStatus(Status.CREATED)))
  }

  def repaymentDetailS1(vrn: Vrn, date: String, status1: RiskingStatus, periodKey: PeriodKey): StubMapping = {
    stubFor(get(urlEqualTo(s"""/vat-repayment-tracker-backend/find/vrn/${vrn.value}/${periodKey.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(
          DesData.storedRepaymentDetails1(date, status1, periodKey).toString()
            .stripMargin)))

  }

  def repaymentDetailS2(vrn: Vrn, date: String, status1: RiskingStatus, status2: RiskingStatus, periodKey: PeriodKey): StubMapping = {
    stubFor(get(urlEqualTo(s"""/vat-repayment-tracker-backend/find/vrn/${vrn.value}/${periodKey.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(
          DesData.storedRepaymentDetails2(date, status1, status2).toString()
            .stripMargin)))

  }

  def repaymentDetailS3(vrn: Vrn, date: String, status1: RiskingStatus, status2: RiskingStatus, status3: RiskingStatus, periodKey: PeriodKey): StubMapping = {
    stubFor(get(urlEqualTo(s"""/vat-repayment-tracker-backend/find/vrn/${vrn.value}/${periodKey.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(
          DesData.storedRepaymentDetails3(date, status1, status2, status3).toString()
            .stripMargin)))

  }

}
