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
import model.des.RiskingStatus
import model.{PeriodKey, Vrn}

object PaymentsOrchestratorStub extends TestHelper {

  def ddOk(vrn: Vrn): StubMapping =
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/dd-data/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(DesData.ddOk.toString())))

  def ddNotFound(vrn: Vrn): StubMapping =
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/dd-data/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(404)
        .withBody(errorResponse(s"""/payments-orchestrator/des/dd-data/vrn/${vrn.value}""").toString())))

  def financialsOkCredit(vrn: Vrn): StubMapping =
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/financial-data/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(DesData.financialDataSingleCredit(vrn: Vrn).toString())))

  def financialsOkCreditNoClearingDate(vrn: Vrn): StubMapping =
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/financial-data/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(DesData.financialDataSingleCreditNoClearingDate(vrn: Vrn).toString())))

  def financialsOkCreditTwoClearingDates(vrn: Vrn): StubMapping =
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/financial-data/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(DesData.financialDataSeveralDates(vrn: Vrn).toString())))

  def financialsOkCreditEmptyItemsArray(vrn: Vrn): StubMapping =
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/financial-data/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(DesData.financialDataEmptyItemsArray(vrn: Vrn).toString())))

  def financialsOkDebit(vrn: Vrn): StubMapping =
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/financial-data/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(DesData.financialDataSingleDebit(vrn: Vrn).toString())))

  def customerDataOkWithBankDetails(vrn: Vrn, partial: Boolean = false): StubMapping =
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/customer-data/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(DesData.customerDataOk(partial).toString())))

  def customerDataOkWithBankDetailsInflight(vrn: Vrn): StubMapping =
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/customer-data/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(DesData.customerDataOkWithBankDetailsInflight.toString())))

  def customerDataOkWithoutBankDetailsInflight(vrn: Vrn): StubMapping =
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/customer-data/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(DesData.customerDataOkWithoutBankDetailsInflight.toString())))

  def customerDataOkWithPartialBankDetails(vrn: Vrn): StubMapping =
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/customer-data/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(DesData.customerDataOkWithPartialBankDetails.toString())))

  def customerDataOkWithoutBankDetails(vrn: Vrn): StubMapping =
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/customer-data/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(DesData.customerDataOkWithoutBankDetails.toString())))

  def customerDataOkDeregistered(vrn: Vrn): StubMapping =
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/customer-data/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(DesData.customerDataOkDeregistered.toString())))

  def financialsNotFound(vrn: Vrn): StubMapping =
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/financial-data/vrn/${vrn.value}"""))
      .willReturn(aResponse().withStatus(404)
        .withBody(errorResponse(s"""/payments-orchestrator/des/financial-data/vrn/${vrn.value}""").toString())))

  def repaymentDetailsNotFound(vrn: Vrn): StubMapping =
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/repayment-details/vrn/${vrn.value}"""))
      .willReturn(aResponse().withStatus(404)
        .withBody(errorResponse(s"""/payments-orchestrator/des/repayment-details/vrn/${vrn.value}""").toString())))

  def repaymentDetailS1(vrn: Vrn, date: String, status1: RiskingStatus, periodKey: PeriodKey, negativeAmt: Boolean = false): StubMapping =
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/repayment-details/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(DesData.repaymentDetails1(date, status1, periodKey, negativeAmt).toString())))

  def repaymentDetailS2(vrn: Vrn, date: String, status1: RiskingStatus, status2: RiskingStatus): StubMapping =
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/repayment-details/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(DesData.repaymentDetails2(date, status1, status2).toString())))

  def repaymentDetailS3(vrn: Vrn, date: String, status1: RiskingStatus, status2: RiskingStatus, status3: RiskingStatus): StubMapping =
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/repayment-details/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(DesData.repaymentDetails3(date, status1, status2, status3).toString())))

  def repaymentDetailS3(vrn: Vrn, date1: String, status1: RiskingStatus, date2: String, status2: RiskingStatus, date3: String, status3: RiskingStatus): StubMapping =
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/repayment-details/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(DesData.repaymentDetails3(date1, status1, date2, status2, date3, status3).toString())))

  def repaymentDetailS4(vrn: Vrn, date: String, status1: RiskingStatus, status2: RiskingStatus, status3: RiskingStatus): StubMapping =
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/repayment-details/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(DesData.repaymentDetails4(date, status1, status2, status3).toString())))

  def repaymentDetailSingleCompleted(vrn: Vrn): StubMapping =
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/repayment-details/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(DesData.repaymentDetailSingleCompleted.toString())))

  def repaymentDetailsMultipleInProgress(vrn: Vrn): StubMapping =
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/repayment-details/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(DesData.repaymentDetailsMultipleInProgress.toString())))

  def repaymentDetailsMultipleCompleted(vrn: Vrn): StubMapping =
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/repayment-details/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(DesData.repaymentDetailsMultipleCompleted().toString())))

  def repaymentDetails3Inprogree1Completed(vrn: Vrn): StubMapping =
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/repayment-details/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(DesData.repaymentDetails3Inprogree1Completed().toString())))

  def repaymentDetails2DifferentPeriods(date: String, date2: String, status1: RiskingStatus, status2: RiskingStatus, vrn: Vrn): StubMapping =
    stubFor(get(urlEqualTo(s"""/payments-orchestrator/des/repayment-details/vrn/${vrn.value}"""))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(DesData.repaymentDetails2DifferentPeriods(date, date2, status1, status2).toString())))
}
