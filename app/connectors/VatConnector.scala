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

import javax.inject.Inject
import model.Vrn
import model.vat.{CalendarData, VatDesignatoryDetailsAddress}
import play.api.Logger
import uk.gov.hmrc.http.{HeaderCarrier, StringContextOps}
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig
import uk.gov.hmrc.http.HttpReads.Implicits._
import uk.gov.hmrc.http.client.HttpClientV2

import scala.concurrent.{ExecutionContext, Future}

class VatConnector @Inject() (
    servicesConfig: ServicesConfig,
    httpClient:     HttpClientV2)
  (implicit ec: ExecutionContext) {

  lazy private val vatUrl: String = servicesConfig.baseUrl("vat")

  private val logger = Logger(this.getClass)

  def calendar(vrn: Vrn)(implicit hc: HeaderCarrier): Future[Option[CalendarData]] = {
    val url: String = vatUrl + s"/vat/${vrn.value}/calendar"
    logger.debug(s"calling vat service with url $url")
    httpClient.get(url"$url").execute[Option[CalendarData]]
  }

  def designatoryDetails(vrn: Vrn)(implicit hc: HeaderCarrier): Future[VatDesignatoryDetailsAddress] = {
    val url: String = vatUrl + s"/vat/${vrn.value}/designatoryDetails"
    logger.debug(s"calling vat service with url $url")
    httpClient.get(url"$url").execute[VatDesignatoryDetailsAddress]
  }

}
