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
import model.{PeriodKey, Vrn}

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

  def financialsOkCredit(vrn: Vrn) = {
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/financial-data/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(
          DesData.financialDataSingleCredit(vrn: Vrn).toString()
            .stripMargin)))

  }

  def financialsOkDebit(vrn: Vrn) = {
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/financial-data/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(
          DesData.financialDataSingleDebit(vrn: Vrn).toString()
            .stripMargin)))

  }

  def customerDataOkWithBankDetails(vrn: Vrn, partial: Boolean = false) = {
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/customer-data/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(
          if (partial) DesData.customerDataOkPartial.toString() else DesData.customerDataOk.toString()
            .stripMargin)))

  }

  def customerDataOkWithoutBankDetailsInflight(vrn: Vrn) = {
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/customer-data/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(
          DesData.customerDataOkWithoutBankDetailsInflight.toString()
            .stripMargin)))

  }

  def customerDataOkWithPartialBankDetails(vrn: Vrn) = {
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/customer-data/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(
          DesData.customerDataOkWithPartialBankDetails.toString()
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

  def customerNotFound(vrn: Vrn) = {
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/customer-data/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(404)
        .withBody(
          DesData.customerDataNotFound.toString()
            .stripMargin)))

  }

  def repaymentDetailsNotFound(vrn: Vrn) = {
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/repayment-details/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(404)
        .withBody(
          DesData.repaymentDetailsNotFound.toString()
            .stripMargin)))

  }

  def repaymentDetailS1(vrn: Vrn, date: String, status1: String, periodKey: PeriodKey, negativeAmt: Boolean = false) = {
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/repayment-details/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(
          DesData.repaymentDetails1(date, status1, periodKey, negativeAmt).toString()
            .stripMargin)))

  }

  def repaymentDetailS2(vrn: Vrn, date: String, status1: String, status2: String) = {
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/repayment-details/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(
          DesData.repaymentDetails2(date, status1, status2).toString()
            .stripMargin)))

  }

  def repaymentDetailS3(vrn: Vrn, date: String, status1: String, status2: String, status3: String) = {
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/repayment-details/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(
          DesData.repaymentDetails3(date, status1, status2, status3).toString()
            .stripMargin)))

  }

  def repaymentDetailSingleCompleted(vrn: Vrn) = {
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/repayment-details/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(
          DesData.repaymentDetailSingleCompleted.toString()
            .stripMargin)))

  }

  def repaymentDetailsMultipleInProgress(vrn: Vrn) = {
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/repayment-details/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(
          DesData.repaymentDetailsMultipleInProgress.toString()
            .stripMargin)))

  }

  def repaymentDetailsMultipleCompleted(vrn: Vrn) = {
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/repayment-details/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(
          DesData.repaymentDetailsMultipleCompleted.toString()
            .stripMargin)))

  }

  def repaymentDetails3Inprogree1Completed(vrn: Vrn) = {
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/repayment-details/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(
          DesData.repaymentDetails3Inprogree1Completed.toString()
            .stripMargin)))

  }

  def repaymentDetails2DifferentPeriods(date: String, date2: String, status1: String, status2: String, vrn: Vrn) = {
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/repayment-details/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(
          DesData.repaymentDetails2DifferentPeriods(date, date2, status1, status2).toString()
            .stripMargin)))

  }

}
