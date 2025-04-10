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

import java.nio.charset.{StandardCharsets => SC}

import javax.inject.{Inject, Singleton}
import model.{PeriodKey, Vrn, VrtRepaymentDetailData}
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc.Request
import play.utils.UriEncoding
import uk.gov.hmrc.http.HttpReads.Implicits._
import uk.gov.hmrc.http.client.HttpClientV2
import uk.gov.hmrc.http.{HttpResponse, StringContextOps}
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class VatRepaymentTrackerBackendConnector @Inject() (
    servicesConfig: ServicesConfig,
    httpClient:     HttpClientV2)
  (implicit ec: ExecutionContext) {

  private val serviceURL: String = servicesConfig.baseUrl("vat-repayment-tracker-backend")

  private val logger = Logger(this.getClass)

  import req.RequestSupport._

  def store(vrtRepaymentDetailData: VrtRepaymentDetailData)(implicit request: Request[_]): Future[HttpResponse] = {
    val storeVRDDUrl: String = s"$serviceURL/vat-repayment-tracker-backend/store"
    logger.debug(s"""calling vat-repayment-tracker-backend find with url $storeVRDDUrl""")
    logger.debug(s"storing: ${vrtRepaymentDetailData.toString}")
    httpClient.post(url"$storeVRDDUrl").withBody(Json.toJson(vrtRepaymentDetailData)).execute[HttpResponse]
  }

  def find(vrn: Vrn, periodKey: PeriodKey)(implicit request: Request[_]): Future[List[VrtRepaymentDetailData]] = {
    logger.debug(s"Calling vat-repayment-tracker-backend find with vrn :${vrn.value} and periodKey:  ${periodKey.value}")

    val periodEncoded = UriEncoding.encodePathSegment(periodKey.value, SC.UTF_8)
    val findRDDUrl: String = s"$serviceURL/vat-repayment-tracker-backend/find/vrn/${vrn.value}/$periodEncoded"
    logger.debug(s"""calling vat-repayment-tracker-backend find with url $findRDDUrl""")

    httpClient.get(url"$findRDDUrl").execute[List[VrtRepaymentDetailData]]
  }

}
