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

package support

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

import java.time._
import java.time.format.DateTimeFormatter
import com.google.inject.{AbstractModule, Provides}

import javax.inject.Singleton
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.freespec.AnyFreeSpec
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.inject.Injector
import play.api.inject.guice.{GuiceApplicationBuilder, GuiceableModule}
import play.api.mvc.{AnyContentAsEmpty, Request}
import play.api.test.{CSRFTokenHelper, FakeRequest}
import play.api.{Application, Configuration, Environment}
import uk.gov.hmrc.http.{HeaderCarrier, SessionKeys}
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig
import org.scalatest.matchers.should.Matchers
import uk.gov.hmrc.http.client.HttpClientV2

import scala.annotation.unused
import scala.concurrent.ExecutionContext

/**
 * This is common spec for every test case which brings all of useful routines we want to use in our scenarios.
 */
trait ItSpec
  extends AnyFreeSpec
  with RichMatchers
  with BeforeAndAfterEach
  with GuiceOneServerPerSuite
  with WireMockSupport
  with Matchers {

  lazy val frozenZonedDateTime: ZonedDateTime = {
    val formatter = DateTimeFormatter.ISO_DATE_TIME
    LocalDateTime.parse("2018-11-02T16:28:55.185", formatter).atZone(ZoneId.of("Europe/London"))
  }

  implicit lazy val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

  lazy val servicesConfig: ServicesConfig = fakeApplication().injector.instanceOf[ServicesConfig]
  lazy val config: Configuration = fakeApplication().injector.instanceOf[Configuration]
  lazy val env: Environment = fakeApplication().injector.instanceOf[Environment]
  def frozenTimeString: String = "2027-11-02T16:33:51.880"
  lazy val overridingsModule: AbstractModule = new AbstractModule {

    override def configure(): Unit = ()

    @Provides
    @Singleton
    @unused
    def clock: Clock = {
      val fixedInstant = LocalDateTime.parse(frozenTimeString).toInstant(ZoneOffset.UTC)
      Clock.fixed(fixedInstant, ZoneId.systemDefault)
    }
  }

  val baseUrl: String = s"http://localhost:$WireMockSupport.port"

  override implicit val patienceConfig: PatienceConfig = PatienceConfig(
    timeout  = scaled(Span(3, Seconds)),
    interval = scaled(Span(300, Millis)))

  implicit val emptyHC: HeaderCarrier = HeaderCarrier()

  def httpClient: HttpClientV2 = fakeApplication().injector.instanceOf[HttpClientV2]

  override def fakeApplication(): Application = new GuiceApplicationBuilder()
    .configure(configMap)
    .overrides(GuiceableModule.fromGuiceModules(Seq(overridingsModule)))
    .build()

  def configMap: Map[String, Any] = Map[String, Any](
    "microservice.services.auth.port" -> WireMockSupport.port,
    "microservice.services.payments-orchestrator.port" -> WireMockSupport.port,
    "microservice.services.direct-debit-backend.port" -> WireMockSupport.port,
    "microservice.services.bank-account-coc.port" -> WireMockSupport.port,
    "microservice.services.vat-repayment-tracker-backend.port" -> WireMockSupport.port,
    "microservice.services.pay-api.port" -> WireMockSupport.port,
    "auditing.consumer.baseUri.port" -> WireMockSupport.port,
    "microservice.services.vat.port" -> WireMockSupport.port,
    "urls.login" -> "http://localhost:11111/auth-login-stub/gg-sign-in",
    "urls.frontend-base" -> "http://localhost:9863",
    "auditing.enabled" -> true
  )

  def injector: Injector = fakeApplication().injector

  def fakeRequest: Request[AnyContentAsEmpty.type] = CSRFTokenHelper.addCSRFToken(
    FakeRequest().withSession(SessionKeys.authToken -> "authToken")
  )

  implicit val request: FakeRequest[AnyContentAsEmpty.type] = FakeRequest()

}
