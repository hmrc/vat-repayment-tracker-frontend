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

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import javax.inject.{Inject, Singleton}
import model.{FinancialData, VatObligations, CustomerInformation}
import play.api.{Configuration, Logger}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.http.logging.Authorization
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class DesConnector @Inject() (servicesConfig: ServicesConfig, httpClient: HttpClient, configuration: Configuration)(implicit ec: ExecutionContext) {

  val serviceURL: String = servicesConfig.baseUrl("des")
  val authorisationToken: String = configuration.get[String]("microservice.services.des.authorization-token")
  val serviceEnvironment: String = configuration.get[String]("microservice.services.des.environment")
  val obligationsUrl: String = configuration.get[String]("microservice.services.des.obligations-url")
  val financialsUrl: String = configuration.get[String]("microservice.services.des.financials-url")
  val customerUrl: String = configuration.get[String]("microservice.services.des.customer-url")

  val desHeaderCarrier: HeaderCarrier = HeaderCarrier(authorization = Some(Authorization(s"Bearer $authorisationToken")))
    .withExtraHeaders("Environment" -> serviceEnvironment)

  //may not need this one
  def getObligations(vrn: String, fromDate: LocalDate, toDate: LocalDate): Future[VatObligations] = {
    Logger.debug(s"Calling des api 1330 for vrn ${vrn}, fromDate ${fromDate.toString}, toDate ${toDate.toString}")
    implicit val hc: HeaderCarrier = desHeaderCarrier
    val fullDate = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val getObligationsURL: String = s"$serviceURL$obligationsUrl/${vrn}/VATC?from=${fromDate.format(fullDate)}to=${toDate.format(fullDate)}&status=F"
    Logger.debug(s"""Calling des api 1330 with url ${getObligationsURL}""")
    httpClient.GET[VatObligations](getObligationsURL)
  }

  def getFinancialData(vrn: String): Future[FinancialData] = {
    Logger.debug(s"Calling des api 1166 for vrn ${vrn}")
    implicit val hc: HeaderCarrier = desHeaderCarrier
    val getFinancialURL: String = s"$serviceURL$financialsUrl/${vrn}/VATC?onlyOpenItems=true"
    Logger.debug(s"""Calling des api 1166 with url ${getFinancialURL}""")
    httpClient.GET[FinancialData](getFinancialURL)
  }

  //this may only be needed for welsh indicator if needed
  def getCustomerData(vrn: String): Future[CustomerInformation] = {
    Logger.debug(s"Calling des api 1363 for vrn ${vrn}")
    implicit val hc: HeaderCarrier = desHeaderCarrier
    val getCustomerURL: String = s"$serviceURL$customerUrl/${vrn}/information:"
    Logger.debug(s"""Calling des api 1363 with url ${getCustomerURL}""")
    httpClient.GET[CustomerInformation](getCustomerURL)
  }

}
