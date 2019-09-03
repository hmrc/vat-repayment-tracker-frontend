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

package connectors

import java.time.LocalDate

import connectors.des.DesConnector
import model._
import model.des._
import play.api.libs.json.Json
import support.{ITSpec, WireMockResponses}

class DesConnectorSpec extends ITSpec {

  val desConnector = injector.instanceOf[DesConnector]

  "find some obligations" in {
    WireMockResponses.obligationsOk
    val response: VatObligations = desConnector.getObligations("968501689", LocalDate.now(), LocalDate.now()).futureValue
    response.obligations.head.obligationDetails.size shouldBe 4
    Json.toJson(response.obligations.head.obligationDetails.last) shouldBe Json.parse(
      s"""{
         | "status": "O",
         | "inboundCorrespondenceFromDate": "2018-01-01",
         | "inboundCorrespondenceToDate": "2018-01-31",
         | "inboundCorrespondenceDateReceived": "2018-04-15",
         | "inboundCorrespondenceDueDate": "2018-03-07",
         | "periodKey": "18AA"
                }""".stripMargin)
  }

  "obligations not found" in {
    WireMockResponses.obligationsNotFound
    val response = desConnector.getObligations("968501689", LocalDate.now(), LocalDate.now()).failed.futureValue
    response.getMessage contains "returned 403"
  }

  "find some financial transactions" in {
    WireMockResponses.financialsOk
    val futureResponse: Option[FinancialData] = desConnector.getFinancialData("968501689").futureValue
    futureResponse match {
      case Some(response) => {
        response.financialTransactions.head.size shouldBe 5
        Json.toJson(response.financialTransactions.head.last) shouldBe Json.parse(
          s"""{
             |            "periodKey": "18AC",
             |            "periodKeyDescription": "March 2018",
             |            "taxPeriodFrom": "2018-03-01",
             |            "taxPeriodTo": "2018-03-31",
             |            "originalAmount": 5.56,
             |            "outstandingAmount": 5.56,
             |            "items": [
             |                {
             |                    "subItem": "000",
             |                    "dueDate": "2018-08-24",
             |                    "amount": 5.56
             |                }
             |            ]
             |        }""".stripMargin)
      }
      case None => "did not find any financial data" shouldBe "test failed"
    }

  }

  "transactions not found" in {
    WireMockResponses.financialsNotFound
    val futureResponse = desConnector.getFinancialData("968501689").futureValue
    futureResponse match {
      case Some(response) => "found financial data" shouldBe "test failed"
      case None           => "None" shouldBe "None"
    }

  }

  "find some customer data" in {
    WireMockResponses.customerDataOk

    val futureResponse: Option[CustomerInformation] = desConnector.getCustomerData("968501689").futureValue
    futureResponse match {
      case Some(response) => {
        Json.toJson(response.approvedInformation) shouldBe Json.parse(
          s"""{"PPOB":{"address":{"line1":"VAT PPOB Line1","line2":"VAT PPOB Line2","line3":"VAT PPOB Line3",
             |"line4":"VAT PPOB Line4","postCode":"TF3 4ER","countryCode":"GB"}},
             |"bankDetails":{"accountHolderName":"*********","bankAccountNumber":"****2490","sortCode":"40****"}}""".stripMargin)
      }
      case None => "did not find any customer data" shouldBe "test failed"
    }

  }

  "customer data not found" in {
    WireMockResponses.customerNotFound
    val futureResponse = desConnector.getCustomerData("968501689").futureValue
    futureResponse match {
      case Some(response) => "found customer data" shouldBe "test failed"
      case None           => "None" shouldBe "None"
    }
  }

}
