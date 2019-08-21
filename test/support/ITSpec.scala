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

import java.time.format.DateTimeFormatter
import java.time.{LocalDateTime, ZoneId, ZonedDateTime}

import com.google.inject.AbstractModule
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
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.ExecutionContext

/**
 * This is common spec for every test case which brings all of useful routines we want to use in our scenarios.
 */

trait ITSpec
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
  lazy val overridingsModule = new AbstractModule {
    override def configure(): Unit = ()
  }
  lazy val servicesConfig = fakeApplication.injector.instanceOf[ServicesConfig]
  lazy val config = fakeApplication.injector.instanceOf[Configuration]
  lazy val env = fakeApplication.injector.instanceOf[Environment]
  val baseUrl: String = s"http://localhost:$WireMockSupport.port"

  override implicit val patienceConfig = PatienceConfig(
    timeout  = scaled(Span(3, Seconds)),
    interval = scaled(Span(300, Millis)))

  implicit val emptyHC = HeaderCarrier()

  def httpClient = fakeApplication().injector.instanceOf[HttpClient]

  override def fakeApplication(): Application = new GuiceApplicationBuilder()
    .overrides(GuiceableModule.fromGuiceModules(Seq(overridingsModule)))
    .configure("microservice.services.auth.port" -> WireMockSupport.port, "microservice.services.des.port" -> WireMockSupport.port
    ).build()

  def injector: Injector = fakeApplication().injector

  val connector = injector.instanceOf[TestConnector]

  def fakeRequest: Request[AnyContentAsEmpty.type] = CSRFTokenHelper.addCSRFToken(FakeRequest())

  def status(of: Result) = of.header.status

}

