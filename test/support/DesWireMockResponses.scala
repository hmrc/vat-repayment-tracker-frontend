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

package support

import com.github.tomakehurst.wiremock.client.WireMock._
import model.Vrn

object DesWireMockResponses {

  def ddOk(vrn: Vrn) = {
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/dd-data/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(
          DesData.ddOk.toString()
            .stripMargin)))

  }

  def ddNotFound(vrn: Vrn) = {
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/dd-data/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(404)
        .withBody(
          DesData.ddNotFound.toString()
            .stripMargin)))

  }

  def obligationsOk(vrn: Vrn, toDate: String, receivedDate: String) = {
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/obligations-data/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(
          DesData.obligationsDataOk(vrn, toDate, receivedDate).toString()
            .stripMargin)))

  }

  def obligationsDataOkSingleDelayed(vrn: Vrn, receivedDate: String, toDate: String) = {
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/obligations-data/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(
          DesData.obligationsDataOkSingleDelayed(vrn, receivedDate, toDate).toString()
            .stripMargin)))

  }

  def obligationsDataOkMultipleOneOfEach(vrn: Vrn, receivedDate: String, toDate: String, receivedDate2: String, toDate2: String) = {
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/obligations-data/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(
          DesData.obligationsDataOkMultipleOneOfEach(vrn, receivedDate, toDate, receivedDate2, toDate2).toString()
            .stripMargin)))

  }

  def obligationsDataOkMultipleMix(vrn: Vrn, delayedDate: String, currentDate: String, delayedToDate: String, currentToDate: String) = {
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/obligations-data/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(
          DesData.obligationsDataOkMultipleMix(vrn, delayedDate, currentDate, delayedToDate, currentToDate).toString()
            .stripMargin)))

  }

  def financialDataOkTwo(vrn: Vrn) = {
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/financial-data/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(
          DesData.financialDataOkTwo(vrn).toString()
            .stripMargin)))

  }

  def financialsOkMultiple4(vrn: Vrn) = {
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/financial-data/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(
          DesData.financialDataOK4(vrn).toString()
            .stripMargin)))

  }

  def financialsOkSingle(vrn: Vrn) = {
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/financial-data/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(
          DesData.financialDataSingleOk(vrn: Vrn).toString()
            .stripMargin)))

  }

  def customerDataOkWithBankDetails(vrn: Vrn) = {
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/customer-data/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(
          DesData.customerDataOk.toString()
            .stripMargin)))

  }

  def customerDataOkWithoutBankDetails(vrn: Vrn) = {
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/customer-data/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(
          DesData.customerDataOkWithoutBankDetails.toString()
            .stripMargin)))

  }

  def financialsNotFound(vrn: Vrn) = {
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/financial-data/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(404)
        .withBody(
          DesData.financialDataNotFound.toString()
            .stripMargin)))

  }

  def obligationsNotFound(vrn: Vrn) = {
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/obligations-data/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(404)
        .withBody(
          DesData.obligationsDataNotFound.toString()
            .stripMargin)))

  }

  def customerNotFound(vrn: Vrn) = {
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/customer-data/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(404)
        .withBody(
          DesData.customerDataNotFound.toString()
            .stripMargin)))

  }

}
