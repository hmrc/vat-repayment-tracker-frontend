/*
 * Copyright 2024 HM Revenue & Customs
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

import formaters.CommonFormatter

import javax.inject.{Inject, Singleton}
import model.{RepaymentDataNoRiskingStatus, Vrn}
import play.api.Logger
import play.api.mvc.Request
import req.RequestSupport
import req.RequestSupport._
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.audit.http.connector.{AuditConnector, AuditResult}
import uk.gov.hmrc.play.audit.model.DataEvent

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class Auditor @Inject() (auditConnector: AuditConnector)(implicit ec: ExecutionContext) {

  private val logger = Logger(this.getClass)

  def audit(
      inProgress:      List[RepaymentDataNoRiskingStatus],
      auditTypeIn:     String,
      transactionName: String,
      vrn:             Vrn
  )(implicit request: Request[_]): Future[AuditResult] = {
    logger.debug(s"About to audit ${RepaymentDataNoRiskingStatus.toString()}, $auditTypeIn, $transactionName")
    val event = DataEvent(
      auditSource = "vat-repayment-tracker-frontend",
      auditType   = auditTypeIn,
      tags        = Auditor.auditTags(request, transactionName),
      detail      = convertToMap(inProgress) ++ identifierDetails(vrn)
    )

    auditConnector.sendEvent(event)

  }

  def auditEngagement(transactionName: String, engmtType: String, vrn: Option[Vrn])(implicit request: Request[_]): Future[AuditResult] = {
    val auditTypeIn = "EngagementStatus"

    val event = DataEvent(
      auditSource = "vat-repayment-tracker-frontend",
      auditType   = auditTypeIn,
      tags        = Auditor.auditTags(request, transactionName),
      detail      = Auditor.engagementDetail(engmtType) ++ vrn.map(identifierDetails).getOrElse(Map.empty)
    )

    auditConnector.sendEvent(event)
  }

  private def identifierDetails(vrn: Vrn): Map[String, String] = Map("vrn" -> vrn.value)

  private def convertToMap(inProgressList: List[RepaymentDataNoRiskingStatus]): Map[String, String] = {
    inProgressList
      .zipWithIndex
      .map {
        case (row, index) =>
          val k = s"inprogress_$index"
          val v = s"returnCreationDate: ${CommonFormatter.formatDate(row.returnCreationDate)}, periodKey: ${row.periodKey}, amount: ${CommonFormatter.formatAmount(row.amount)}"
          k -> v
      }.toMap
  }
}

object Auditor {

  object `repayment-type` {
    val one_in_progress_multiple_delayed = "one_in_progress_multiple_delayed"
    val none_in_progress = "none_in_progress"
    val in_progress_classic = "in_progress_classic"
  }

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

  def engagementDetail(engmtType: String): Map[String, String] = {
    Map(
      "engmtType" -> engmtType
    )
  }

}

