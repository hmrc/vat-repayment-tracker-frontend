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

package controllers

import play.api.test.Helpers._
import model.EnrolmentKeys
import model.payapi.SpjRequestBtaVat
import support.VatData.vrn
import support.{AuditWireMockResponses, AuthWireMockResponses, ItSpec, PayApiWireMockResponses}

class PayApiControllerSpec extends ItSpec {

  lazy val controller = injector.instanceOf[PayApiController]

  "PayApiController when" - {

    "starting a payments journey should" - {

      "redirect to the nextUrl provided if the call is successful" in {
        AuditWireMockResponses.auditIsAvailable
        AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString, vrn, EnrolmentKeys.mtdVatEnrolmentKey)
        PayApiWireMockResponses.payOk

        val result = controller.startPaymentsJourney(100L)(fakeRequest)
        status(result) shouldBe SEE_OTHER
        redirectLocation(result) shouldBe Some(PayApiWireMockResponses.nextUrl)

        PayApiWireMockResponses.verifyStartJourneyCalled(
          SpjRequestBtaVat(
            100L,
            "http://localhost:9863/vat-repayment-tracker/show-vrt",
            "http://localhost:9863/vat-repayment-tracker/show-vrt",
            vrn
          )
        )

      }

    }

  }

}
