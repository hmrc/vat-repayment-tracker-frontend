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

import com.github.tomakehurst.wiremock.client.WireMock._
import com.github.tomakehurst.wiremock.stubbing.StubMapping
import org.scalatest.matchers.should.Matchers
import play.api.libs.json.Json

import scala.jdk.CollectionConverters._

object AuditWireMockResponses extends Matchers {

  def auditIsAvailable: StubMapping = {
    stubFor(post(urlEqualTo("/write/audit")).willReturn(aResponse().withStatus(204)))
    stubFor(post(urlEqualTo("/write/audit/merged")).willReturn(aResponse().withStatus(204)))
  }

  def bacWasAudited(details: Map[String, String]): Unit = {

    verify(postRequestedFor(urlEqualTo("/write/audit")))
    val auditWrites = findAll(postRequestedFor(urlEqualTo("/write/audit"))).asScala.toList
    val mayPaymentAuditEvent = auditWrites.find(_.getBodyAsString.contains("initiateChangeVATRepaymentBankAccount"))
    mayPaymentAuditEvent shouldBe defined
    val jsBody = Json.parse(mayPaymentAuditEvent.get.getBodyAsString)

    details foreach { e =>
      (jsBody \ "detail" \ e._1).as[String] shouldBe e._2
    }

  }

  def bacWasAuditedNoDetails(): Unit = {
    verify(postRequestedFor(urlEqualTo("/write/audit")))
    val auditWrites = findAll(postRequestedFor(urlEqualTo("/write/audit"))).asScala.toList
    val mayPaymentAuditEvent = auditWrites.find(_.getBodyAsString.contains("initiateChangeVATRepaymentBankAccount"))
    mayPaymentAuditEvent shouldBe defined
    val jsBody = Json.parse(mayPaymentAuditEvent.get.getBodyAsString)
    (jsBody \ "detail").asOpt[String] shouldBe None
    ()
  }

  def engagementStatusAudited(transactionName: String, details: Map[String, String]): Unit = {
    verify(postRequestedFor(urlEqualTo("/write/audit")))

    val auditWrites = findAll(postRequestedFor(urlEqualTo("/write/audit"))).asScala.toList
    val mayPaymentAuditEvent = auditWrites.find(_.getBodyAsString.contains(transactionName))
    mayPaymentAuditEvent shouldBe defined
    val jsBody = Json.parse(mayPaymentAuditEvent.get.getBodyAsString)

    (jsBody \ "auditType").as[String] shouldBe "EngagementStatus"
    (jsBody \ "tags" \ "transactionName").as[String] shouldBe transactionName

    details foreach { e =>
      (jsBody \ "detail" \ e._1).as[String] shouldBe e._2
    }
  }

}
