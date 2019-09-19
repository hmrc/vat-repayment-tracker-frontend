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

package connectors

import javax.inject.{Inject, Singleton}
import model.Vrn
import model.des.{CustomerInformation, FinancialData, VatObligations}
import play.api.mvc.Request
import play.api.{Configuration, Logger}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.HeaderCarrierConverter
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PaymentsOrchestratorConnector @Inject() (
    servicesConfig: ServicesConfig,
    httpClient:     HttpClient,
    configuration:  Configuration)
  (implicit ec: ExecutionContext) {

  private val serviceURL: String = servicesConfig.baseUrl("payments-orchestrator")
  private val obligationsUrl: String = configuration.get[String]("microservice.services.payments-orchestrator.obligations-url")
  private val financialsUrl: String = configuration.get[String]("microservice.services.payments-orchestrator.financials-url")
  private val customerUrl: String = configuration.get[String]("microservice.services.payments-orchestrator.customer-url")

  def getObligations(vrn: Vrn)(implicit request: Request[_]): Future[Option[VatObligations]] = {

    implicit val hc: HeaderCarrier = HeaderCarrierConverter.fromHeadersAndSession(request.headers, Some(request.session))

    Logger.debug(s"Calling payments orchestrator for des api 1330 for vrn ${vrn}")
    val getObligationsURL: String = s"$serviceURL$obligationsUrl/${vrn.value}"
    Logger.debug(s"""Calling payments orchestrator for des api 1330 with url ${getObligationsURL}""")
    httpClient.GET[Option[VatObligations]](getObligationsURL)
  }

  def getFinancialData(vrn: Vrn)(implicit request: Request[_]): Future[Option[FinancialData]] = {

    implicit val hc: HeaderCarrier = HeaderCarrierConverter.fromHeadersAndSession(request.headers, Some(request.session))

    Logger.debug(s"Calling payments orchestrator for des api 1166 for vrn ${vrn}")
    val getFinancialURL: String = s"$serviceURL$financialsUrl/${vrn.value}"
    Logger.debug(s"""Calling payments orchestrator for des api 1166 with url ${getFinancialURL}""")
    httpClient.GET[Option[FinancialData]](getFinancialURL)
  }

  def getCustomerData(vrn: Vrn)(implicit request: Request[_]): Future[Option[CustomerInformation]] = {

    implicit val hc: HeaderCarrier = HeaderCarrierConverter.fromHeadersAndSession(request.headers, Some(request.session))

    Logger.debug(s"Calling payments orchestrator for des api 1363 for vrn ${vrn}")
    val getCustomerURL: String = s"$serviceURL$customerUrl/${vrn.value}"
    Logger.debug(s"""Calling payments orchestrator for des api 1363 with url ${getCustomerURL}""")
    httpClient.GET[Option[CustomerInformation]](getCustomerURL)
  }

}