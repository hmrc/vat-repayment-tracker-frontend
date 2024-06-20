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
import model.dd.CreateVATJourneyRequest
import model.{NextUrl, Vrn}
import play.api.libs.json.Json
import play.api.mvc.Request
import play.api.{Configuration, Logger}
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig
import uk.gov.hmrc.http.HttpReads.Implicits._
import uk.gov.hmrc.http.StringContextOps
import uk.gov.hmrc.http.client.HttpClientV2

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class DirectDebitBackendConnector @Inject() (
    servicesConfig: ServicesConfig,
    httpClient:     HttpClientV2,
    configuration:  Configuration)
  (implicit ec: ExecutionContext) {

  import req.RequestSupport._

  private val logger = Logger(this.getClass)

  private val serviceUrl: String = servicesConfig.baseUrl("direct-debit-backend")
  private val sjUrl: String = configuration.get[String]("microservice.services.direct-debit-backend.sj-url")
  private val rUrl: String = configuration.get[String]("microservice.services.direct-debit-backend.return-url")
  private val bUrl: String = configuration.get[String]("urls.dd-back-url")

  def startJourney(vrn: Vrn)(implicit request: Request[_]): Future[NextUrl] = {
    val bkUrl: String = s"$bUrl"
    logger.debug(s"Using return url : $rUrl")
    logger.debug(s"Using back url : $bkUrl")

    val createVATJourneyRequest: CreateVATJourneyRequest = CreateVATJourneyRequest(userId    = vrn.value, returnUrl = rUrl, backUrl = bkUrl)
    logger.debug(s"Calling direct-debit-backend start journey for vrn $vrn")
    val startJourneyURL: String = s"$serviceUrl$sjUrl"
    logger.debug(s"Calling direct-debit-backend start journey for vrn with url $startJourneyURL)")
    httpClient.post(url"$startJourneyURL").withBody(Json.toJson(createVATJourneyRequest)).execute[NextUrl]
  }

}
