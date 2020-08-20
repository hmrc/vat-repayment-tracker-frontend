/*
 * Copyright 2020 HM Revenue & Customs
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
import play.api.mvc.Request
import play.api.Logger
import play.utils.UriEncoding
import uk.gov.hmrc.http.HttpResponse
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig
import uk.gov.hmrc.play.bootstrap.http.HttpClient
import uk.gov.hmrc.http.HttpReads.Implicits._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class VatRepaymentTrackerBackendConnector @Inject() (
    servicesConfig: ServicesConfig,
    httpClient:     HttpClient)
  (implicit ec: ExecutionContext) {

  private val serviceURL: String = servicesConfig.baseUrl("vat-repayment-tracker-backend")

  private val logger = Logger(this.getClass)

  import req.RequestSupport._

  def store(vrtRepaymentDetailData: VrtRepaymentDetailData)(implicit request: Request[_]): Future[HttpResponse] = {
    val storeVRDD: String = s"$serviceURL/vat-repayment-tracker-backend/store"
    logger.debug(s"""calling vat-repayment-tracker-backend find with url $storeVRDD""")
    logger.debug(s"storing: ${vrtRepaymentDetailData.toString}")
    httpClient.POST[VrtRepaymentDetailData, HttpResponse](storeVRDD, vrtRepaymentDetailData)
  }

  def find(vrn: Vrn, periodKey: PeriodKey)(implicit request: Request[_]): Future[List[VrtRepaymentDetailData]] = {
    logger.debug(s"Calling vat-repayment-tracker-backend find with vrn :${vrn.value} and periodKey:  ${periodKey.value}")

    val periodEncoded = UriEncoding.encodePathSegment(periodKey.value, SC.UTF_8)
    val findRDD: String = s"$serviceURL/vat-repayment-tracker-backend/find/vrn/${vrn.value}/$periodEncoded"
    logger.debug(s"""calling vat-repayment-tracker-backend find with url $findRDD""")

    httpClient.GET[List[VrtRepaymentDetailData]](findRDD)
  }

}
