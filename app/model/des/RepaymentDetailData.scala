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

package model.des

import model.des.RiskingStatus._

import java.time.LocalDate
import play.api.libs.json.{Json, OFormat}

final case class RepaymentDetailData(
  returnCreationDate:     LocalDate,
  sentForRiskingDate:     Option[LocalDate],
  lastUpdateReceivedDate: Option[LocalDate],
  periodKey:              String,
  riskingStatus:          RiskingStatus,
  vatToPay_BOX5:          BigDecimal,
  supplementDelayDays:    Option[Int],
  originalPostingAmount:  BigDecimal
) {

  @SuppressWarnings(Array("org.wartremover.warts.JavaSerializable"))
  def getAmountForDisplay(status: RiskingStatus): BigDecimal =
    if (
      Seq(
        CLAIM_QUERIED,
        REPAYMENT_APPROVED,
        INITIAL,
        SENT_FOR_RISKING
      ).contains(status)
    ) {
      originalPostingAmount
    } else {
      vatToPay_BOX5
    }

  // For a status of initial or sent_for_risking , we might not have a  lastUpdateReceived date
  val sorted: Int =
    riskingStatus match {
      case INITIAL                                                        => 5
      case SENT_FOR_RISKING                                               => 4
      case CLAIM_QUERIED                                                  => 3
      case REPAYMENT_SUSPENDED                                            => 2
      case REPAYMENT_ADJUSTED | ADJUSMENT_TO_TAX_DUE | REPAYMENT_APPROVED => 1
    }
}

object RepaymentDetailData {
  implicit val format: OFormat[RepaymentDetailData] = Json.format[RepaymentDetailData]
}
