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

package connectors

import model.vat.{CalendarData, VatDesignatoryDetailsAddress}
import model.Vrn
import support.{AuditWireMockResponses, ItSpec, VatData, VatWireMockResponses}

class VatConnectorSpec extends ItSpec {

  val vrn: Vrn = Vrn("2345678890")

  val vatConnector: VatConnector = injector.instanceOf[VatConnector]

  "Get Calendar ok" in {
    VatWireMockResponses.calendarOk(vrn, isCurrent = false)
    AuditWireMockResponses.auditIsAvailable
    val result: Option[CalendarData] = vatConnector.calendar(vrn).futureValue
    result match {
      case Some(x) => x shouldBe VatData.calendarData
      case None    => "expected a calendar" shouldBe "got None "
    }
  }

  "Get Calendar 404" in {
    VatWireMockResponses.calendar404(vrn)
    AuditWireMockResponses.auditIsAvailable
    val result: Option[CalendarData] = vatConnector.calendar(vrn).futureValue
    result match {
      case Some(x) => s"expected None got ${x.toString}"
      case None    => "None" shouldBe "None"
    }

  }

  "Get designatoryDetails ok" in {
    VatWireMockResponses.designatoryDetailsOk(vrn)
    AuditWireMockResponses.auditIsAvailable
    val result: VatDesignatoryDetailsAddress = vatConnector.designatoryDetails(vrn).futureValue
    result shouldBe VatData.vatDesignatoryDetailsAddress
  }

  "Get designatoryDetails 404" in {
    VatWireMockResponses.designatoryDetails404(vrn)
    AuditWireMockResponses.auditIsAvailable
    val result: Throwable = vatConnector.designatoryDetails(vrn).failed.futureValue
    result.getMessage should include("returned 404")

  }

}
