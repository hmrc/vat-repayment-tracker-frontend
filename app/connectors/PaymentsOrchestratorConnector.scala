/*
 * Copyright 2024 HM Revenue & Customs
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
import model.des.{CustomerInformation, DirectDebitData, FinancialData, RepaymentDetailData}
import play.api.mvc.Request
import play.api.{Configuration, Logger}
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig
import uk.gov.hmrc.http.HttpReads.Implicits._
import uk.gov.hmrc.http.StringContextOps
import uk.gov.hmrc.http.client.HttpClientV2

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PaymentsOrchestratorConnector @Inject() (
    servicesConfig: ServicesConfig,
    httpClient:     HttpClientV2,
    configuration:  Configuration)
  (implicit ec: ExecutionContext) {

  private val serviceURL: String = servicesConfig.baseUrl("payments-orchestrator")
  private val financialsUrl: String = configuration.get[String]("microservice.services.payments-orchestrator.financials-url")
  private val customerUrl: String = configuration.get[String]("microservice.services.payments-orchestrator.customer-url")
  private val ddUrl: String = configuration.get[String]("microservice.services.payments-orchestrator.dd-url")
  private val repaymentDetailsUrl: String = configuration.get[String]("microservice.services.payments-orchestrator.repayment-details-url")

  import req.RequestSupport._

  private val logger = Logger(this.getClass)

  def getFinancialData(vrn: Vrn)(implicit request: Request[_]): Future[Option[FinancialData]] = {
    logger.debug(s"Calling payments orchestrator for des api 1166 for vrn $vrn")
    val getFinancialURL: String = s"$serviceURL$financialsUrl/${vrn.value}"
    logger.debug(s"""Calling payments orchestrator for des api 1166 with url $getFinancialURL""")
    httpClient.get(url"$getFinancialURL").execute[Option[FinancialData]]
  }

  def getCustomerData(vrn: Vrn)(implicit request: Request[_]): Future[Option[CustomerInformation]] = {
    logger.debug(s"Calling payments orchestrator for des api 1363 for vrn $vrn")
    val getCustomerURL: String = s"$serviceURL$customerUrl/${vrn.value}"
    logger.debug(s"""Calling payments orchestrator for des api 1363 with url $getCustomerURL""")
    httpClient.get(url"$getCustomerURL").execute[Option[CustomerInformation]]
  }

  def getDDData(vrn: Vrn)(implicit request: Request[_]): Future[Option[DirectDebitData]] = {
    logger.debug(s"Calling payments orchestrator for des api 1396 for vrn $vrn")
    val getDDURL: String = s"$serviceURL$ddUrl/${vrn.value}"
    logger.debug(s"""Calling payments orchestrator for des api 1396 with url $getDDURL""")
    httpClient.get(url"$getDDURL").execute[Option[DirectDebitData]]
  }

  def getRepaymentsDetails(vrn: Vrn)(implicit request: Request[_]): Future[Option[Seq[RepaymentDetailData]]] = {
    logger.debug(s"Calling payments orchestrator for des api 1533 for vrn $vrn")
    val getRDURL: String = s"$serviceURL$repaymentDetailsUrl/${vrn.value}"
    logger.debug(s"""Calling payments orchestrator for des api 1533 with url $getRDURL""")
    httpClient.get(url"$getRDURL").execute[Option[Seq[RepaymentDetailData]]]
  }

}
