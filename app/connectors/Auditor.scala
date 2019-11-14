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

import javax.inject.{Inject, Singleton}
import model.des.RiskingStatus
import play.api.Logger
import play.api.mvc.Request
import req.RequestSupport
import req.RequestSupport._
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import uk.gov.hmrc.play.audit.model.DataEvent

import scala.concurrent.ExecutionContext

@Singleton
class Auditor @Inject() (
    auditConnector: AuditConnector)(
    implicit
    ec: ExecutionContext) {

  def audit(
      riskingStatus:   String,
      auditTypeIn:     String,
      transactionName: String)(
      implicit
      request: Request[_]) = {

    Logger.debug(s"About to audit ${riskingStatus}, ${auditTypeIn}, ${transactionName}")
    auditConnector.sendEvent(
      DataEvent(
        auditSource = "vat-repayment-tracker-frontend",
        auditType   = auditTypeIn,
        tags        = Auditor.auditTags(request, transactionName),
        detail      = AuditData.auditData(riskingStatus)))

  }
}

object Auditor {

  def auditTags(request: Request[_], transactionName: String): Map[String, String] = {

    val hc: HeaderCarrier = RequestSupport.hc(request)
    Map(
      "Akamai-Reputation" -> hc.akamaiReputation.map(_.value).getOrElse("-"),
      "X-Request-ID" -> hc.requestId.map(_.value).getOrElse("-"),
      "X-Session-ID" -> hc.sessionId.map(_.value).getOrElse("-"),
      "clientIP" -> hc.trueClientIp.getOrElse("-"),
      "clientPort" -> hc.trueClientPort.getOrElse("-"),
      "deviceID" -> hc.deviceID.getOrElse("-"),
      "path" -> request.path,
      "transactionName" -> transactionName)
  }

}

object AuditData {

  def auditData(riskingStatus: String)(implicit request: Request[_]): Map[String, String] = {

    Map(
      "riskingStatus" -> riskingStatus
    )
  }
}
