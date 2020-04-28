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

package support

import javax.inject.{Inject, Singleton}
import model.Vrn
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse}
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TestConnector @Inject() (httpClient: HttpClient)(implicit executionContext: ExecutionContext) {

  val port = 19001

  def showResults(vrn: Vrn)(implicit hc: HeaderCarrier): Future[HttpResponse] = httpClient.GET(s"http://localhost:$port/vat-repayment-tracker-frontend/show-results/vrn/${vrn.value}")

  def viewRepaymentAccount(implicit hc: HeaderCarrier): Future[HttpResponse] = httpClient.GET(
    s"http://localhost:$port/vat-repayment-tracker/view-repayment-account")

  def viewDDAccount(implicit hc: HeaderCarrier): Future[HttpResponse] = httpClient.GET(
    s"http://localhost:$port/vat-repayment-tracker/view-dd-account")

  def startBankAccountCocJourney(implicit hc: HeaderCarrier): Future[HttpResponse] = httpClient.GET(
    s"http://localhost:$port/vat-repayment-tracker/bank-account-coc/start-journey/manage-or-track")

  def startPaymentsJourney(amountInPence: Long)(implicit hc: HeaderCarrier): Future[HttpResponse] = httpClient.GET(
    s"http://localhost:$port/vat-repayment-tracker/spj/$amountInPence)")

  def showVrt(implicit hc: HeaderCarrier): Future[HttpResponse] = httpClient.GET(s"http://localhost:$port/vat-repayment-tracker/show-vrt")

  def manageOrTrackVrt(implicit hc: HeaderCarrier): Future[HttpResponse] = httpClient.GET(s"http://localhost:$port/vat-repayment-tracker/manage-or-track-vrt")

  def manageOrTrack(vrn: Vrn)(implicit hc: HeaderCarrier): Future[HttpResponse] = httpClient.GET(s"http://localhost:$port/vat-repayment-tracker-frontend/manage-or-track/vrn/${vrn.value}")

}
