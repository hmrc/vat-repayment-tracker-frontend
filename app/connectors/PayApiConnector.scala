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

import model.Vrn
import model.payapi.{SpjRequestBtaVat, SpjResponse}
import play.api.libs.json.Json
import play.api.mvc.Request
import play.api.{Configuration, Logger}
import uk.gov.hmrc.http.HttpReads.Implicits._
import uk.gov.hmrc.http.StringContextOps
import uk.gov.hmrc.http.client.HttpClientV2
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PayApiConnector @Inject() (
    servicesConfig: ServicesConfig,
    httpClient:     HttpClientV2,
    configuration:  Configuration)
  (implicit ec: ExecutionContext) {

  import req.RequestSupport._

  private val logger = Logger(this.getClass)

  private val serviceUrl: String = servicesConfig.baseUrl("pay-api")
  private val viewUrl: String = configuration.get[String]("microservice.services.pay-api.sj-url")
  private val payBackUrl: String = configuration.get[String]("urls.pay-back-url")

  def startJourney(amountInPence: Long, vrn: Vrn)(implicit request: Request[_]): Future[SpjResponse] = {

    val journeyRequest: SpjRequestBtaVat = SpjRequestBtaVat(amountInPence, payBackUrl, payBackUrl, vrn)
    logger.debug(s"Using back url : $payBackUrl")
    logger.debug(s"Calling pay-api start journey for vrn $vrn")
    val startJourneyURL: String = s"$serviceUrl$viewUrl"
    logger.debug(s"Calling pay-api start journey for vrn with url $startJourneyURL)")
    httpClient.post(url"$startJourneyURL").withBody(Json.toJson(journeyRequest)).execute[SpjResponse]
  }
}
