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

package connectors

import config.ViewConfig
import javax.inject.{Inject, Singleton}
import model.Vrn
import model.payapi.{SpjRequestBtaVat, SpjResponse}
import play.api.{Configuration, Logger}
import play.api.mvc.Request
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig
import uk.gov.hmrc.http.HttpClient
import uk.gov.hmrc.http.HttpReads.Implicits._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PayApiConnector @Inject() (
    servicesConfig: ServicesConfig,
    httpClient:     HttpClient,
    configuration:  Configuration,
    viewConfig:     ViewConfig)
  (implicit ec: ExecutionContext) {

  import req.RequestSupport._

  private val logger = Logger(this.getClass)

  private val serviceUrl: String = servicesConfig.baseUrl("pay-api")
  private val viewUrl: String = configuration.get[String]("microservice.services.pay-api.sj-url")
  private val payBackUrl: String = configuration.get[String]("urls.pay-back-url")

  def startJourney(amountInPence: Long, vrn: Vrn)(implicit request: Request[_]): Future[SpjResponse] = {

    val bUrl: String = s"${viewConfig.frontendBaseUrl}$payBackUrl${vrn.value}"
    val journeyRequest: SpjRequestBtaVat = SpjRequestBtaVat(amountInPence, bUrl, bUrl, vrn)
    logger.debug(s"Using back url : $bUrl")
    logger.debug(s"Calling pay-api start journey for vrn $vrn")
    val startJourneyURL: String = s"$serviceUrl$viewUrl"
    logger.debug(s"Calling pay-api start journey for vrn with url $startJourneyURL)")
    httpClient.POST[SpjRequestBtaVat, SpjResponse](startJourneyURL, journeyRequest)

  }
}
