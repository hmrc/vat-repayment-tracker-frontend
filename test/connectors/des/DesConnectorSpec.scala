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

package connectors.des

import java.time.{Clock, LocalDate}

import model.Vrn
import model.des._
import play.api.libs.json.Json
import support.{DesData, ItSpec, WireMockResponses}

class DesConnectorSpec extends ItSpec {

  val desConnector = injector.instanceOf[DesConnector]

  val clock = injector.instanceOf[Clock]

  val vrn: Vrn = Vrn("968501689")

  "find some obligations" in {
    WireMockResponses.obligationsOk(vrn)
    val futureResponse: Option[VatObligations] = desConnector.getObligations(vrn).futureValue
    futureResponse match {
      case Some(response) => {
        response.obligations.head.obligationDetails.size shouldBe 4
        response.obligations.head.obligationDetails.last shouldBe DesData.obligationsDataOk(vrn, LocalDate.now(clock).toString).
          \("obligations").\(0).\("obligationDetails").\(3).as[ObligationDetail]
      }
      case None => "did not find any obligations data" shouldBe "test failed"
    }

  }

  "obligations not found" in {

    WireMockResponses.obligationsNotFound
    val futureResponse = desConnector.getObligations(vrn).futureValue
    futureResponse match {
      case Some(response) => "found obligaions data" shouldBe "test failed"
      case None           => "None" shouldBe "None"
    }
  }

  "find some financial transactions" in {

    WireMockResponses.financialsOkMultiple(vrn)
    val futureResponse: Option[FinancialData] = desConnector.getFinancialData(vrn).futureValue
    futureResponse match {
      case Some(response) => {
        response.financialTransactions.size shouldBe 5
        response.financialTransactions.last shouldBe DesData.financialDataOk(vrn).\("financialTransactions").\(4).as[Transaction]
      }
      case None => "did not find any financial data" shouldBe "test failed"
    }

  }

  "transactions not found" in {
    WireMockResponses.financialsNotFound
    val futureResponse = desConnector.getFinancialData(vrn).futureValue
    futureResponse match {
      case Some(response) => "found financial data" shouldBe "test failed"
      case None           => "None" shouldBe "None"
    }

  }

  "find some customer data" in {
    WireMockResponses.customerDataOkWithBankDetails(vrn)

    val futureResponse: Option[CustomerInformation] = desConnector.getCustomerData(vrn).futureValue
    futureResponse match {
      case Some(response) => {
        Json.toJson(response.approvedInformation) shouldBe Json.parse(
          s"""{"bankDetails":{"accountHolderName":"*********","bankAccountNumber":"****2490","sortCode":"40****"}}""".stripMargin)
      }
      case None => "did not find any customer data" shouldBe "test failed"
    }

  }

  "customer data not found" in {
    WireMockResponses.customerNotFound(vrn)
    val futureResponse = desConnector.getCustomerData(vrn).futureValue
    futureResponse match {
      case Some(response) => "found customer data" shouldBe "test failed"
      case None           => "None" shouldBe "None"
    }
  }

}
