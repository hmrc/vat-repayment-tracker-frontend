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

package connectors.des

import java.time.format.DateTimeFormatter

import javax.inject.{Inject, Singleton}
import model.Vrn
import model.des.{CustomerInformation, FinancialData, VatObligations}
import play.api.{Configuration, Logger}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.http.logging.Authorization
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class DesConnector @Inject() (servicesConfig: ServicesConfig, httpClient: HttpClient, configuration: Configuration)(implicit ec: ExecutionContext) {

  private val serviceURL: String = servicesConfig.baseUrl("des")
  private val authorisationToken: String = configuration.get[String]("microservice.services.des.authorization-token")
  private val serviceEnvironment: String = configuration.get[String]("microservice.services.des.environment")
  private val obligationsUrl: String = configuration.get[String]("microservice.services.des.obligations-url")
  private val financialsUrl: String = configuration.get[String]("microservice.services.des.financials-url")
  private val customerUrl: String = configuration.get[String]("microservice.services.des.customer-url")

  private val desHeaderCarrier: HeaderCarrier = HeaderCarrier(authorization = Some(Authorization(s"Bearer $authorisationToken")))
    .withExtraHeaders("Environment" -> serviceEnvironment)

  def getObligations(vrn: Vrn): Future[Option[VatObligations]] = {
    Logger.debug(s"Calling des api 1330 for vrn ${vrn}")
    implicit val hc: HeaderCarrier = desHeaderCarrier
    val fullDate = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val getObligationsURL: String = s"$serviceURL$obligationsUrl/${vrn.value}/VATC?status=O"
    Logger.debug(s"""Calling des api 1330 with url ${getObligationsURL}""")
    httpClient.GET[Option[VatObligations]](getObligationsURL)
  }

  def getFinancialData(vrn: Vrn): Future[Option[FinancialData]] = {
    Logger.debug(s"Calling des api 1166 for vrn ${vrn}")
    implicit val hc: HeaderCarrier = desHeaderCarrier
    val getFinancialURL: String = s"$serviceURL$financialsUrl/${vrn.value}/VATC?onlyOpenItems=true"
    Logger.debug(s"""Calling des api 1166 with url ${getFinancialURL}""")
    httpClient.GET[Option[FinancialData]](getFinancialURL)
  }

  def getCustomerData(vrn: Vrn): Future[Option[CustomerInformation]] = {
    Logger.debug(s"Calling des api 1363 for vrn ${vrn}")
    implicit val hc: HeaderCarrier = desHeaderCarrier
    val getCustomerURL: String = s"$serviceURL$customerUrl/${vrn.value}/information"
    Logger.debug(s"""Calling des api 1363 with url ${getCustomerURL}""")
    httpClient.GET[Option[CustomerInformation]](getCustomerURL)
  }

}
