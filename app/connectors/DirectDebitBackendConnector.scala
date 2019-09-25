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
import model.dd.{CreateVATJourneyRequest, NextUrl}
import play.api.mvc.Request
import play.api.{Configuration, Logger}
import req.RequestSupport
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class DirectDebitBackendConnector @Inject() (
    servicesConfig: ServicesConfig,
    httpClient:     HttpClient,
    configuration:  Configuration)
  (implicit ec: ExecutionContext) {

  import req.RequestSupport._

  private val serviceURL: String = servicesConfig.baseUrl("direct-debit-backend")
  private val sjUrl: String = configuration.get[String]("microservice.services.direct-debit-backend.sj-url")

  def startJourney(vrn: Vrn)(implicit request: Request[_]): Future[NextUrl] = {

    val createVATJourneyRequest: CreateVATJourneyRequest = CreateVATJourneyRequest(userId    = vrn.value, returnUrl = s"/vat-repayment-tracker-frontend/manage-or-track/vrn/${vrn.value}")
    Logger.debug(s"Calling direct-debit-backend start journey for vrn ${vrn}")
    val startJourneyURL: String = s"$serviceURL$sjUrl"
    Logger.debug(s"Calling direct-debit-backend start journey for vrn with url ${startJourneyURL})")
    httpClient.POST[CreateVATJourneyRequest, NextUrl](startJourneyURL, createVATJourneyRequest)
  }

}
