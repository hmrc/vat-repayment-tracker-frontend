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
import org.openqa.selenium.WebDriver
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatest.{BeforeAndAfterEach, FreeSpecLike, Matchers}
import org.scalatestplus.play.guice.GuiceOneServerPerTest
import play.api.inject.Injector
import play.api.inject.guice.{GuiceApplicationBuilder, GuiceableModule}
import play.api.mvc.{AnyContentAsEmpty, Request, Result}
import play.api.test.{CSRFTokenHelper, FakeRequest}
import play.api.{Application, Configuration, Environment}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig
import uk.gov.hmrc.http.HttpClient

import scala.concurrent.ExecutionContext

/**
 * This is common spec for every test case which brings all of useful routines we want to use in our scenarios.
 */
trait ItSpec
  extends FreeSpecLike
  with RichMatchers
  with BeforeAndAfterEach
  with GuiceOneServerPerTest
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
  val webdriverUr: String = s"http://localhost:$port"
  val connector: TestConnector = injector.instanceOf[TestConnector]

  def httpClient: HttpClient = fakeApplication().injector.instanceOf[HttpClient]

  override def fakeApplication(): Application = new GuiceApplicationBuilder()
    .overrides(GuiceableModule.fromGuiceModules(Seq(overridingsModule)))
    .configure(configMap).build()

  def configMap: Map[String, Any] = Map[String, Any](
    "microservice.services.auth.port" -> WireMockSupport.port, "microservice.services.payments-orchestrator.port" -> WireMockSupport.port,
    "microservice.services.direct-debit-backend.port" -> WireMockSupport.port, "microservice.services.bank-account-coc.port" -> WireMockSupport.port,
    "microservice.services.vat-repayment-tracker-backend.port" -> WireMockSupport.port,
    "microservice.services.pay-api.port" -> WireMockSupport.port,
    "auditing.consumer.baseUri.port" -> WireMockSupport.port,
    "microservice.services.vat.port" -> WireMockSupport.port,
    "urls.login" -> "http://localhost:11111/auth-login-stub/gg-sign-in",
    "urls.frontend-base" -> "http://localhost:19001",
    "auditing.enabled" -> true
  )

  def injector: Injector = fakeApplication().injector

  def fakeRequest: Request[AnyContentAsEmpty.type] = CSRFTokenHelper.addCSRFToken(FakeRequest())

  def status(of: Result): Int = of.header.status

  protected implicit val webDriver: WebDriver = new HtmlUnitDriver(false)

  def goToViaPath(path: String): Unit = webDriver.get(s"$webdriverUr$path")

  implicit val request: FakeRequest[AnyContentAsEmpty.type] = FakeRequest()

}
