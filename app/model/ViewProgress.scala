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

package model

import cats.data.NonEmptyList
import model.des.RiskingStatus

import java.time.LocalDate

final case class ViewProgress(
    amount:                 BigDecimal,
    returnCreationDate:     LocalDate,
    estimatedRepaymentDate: LocalDate,
    period:                 String,
    whatsHappenedSoFar:     NonEmptyList[WhatsHappendSoFar]) {

  val isComplete: Boolean = whatsHappenedSoFar.exists(_.isComplete)
  val repaymentSuspended: Boolean = whatsHappenedSoFar.exists(_.riskingStatus == RiskingStatus.REPAYMENT_SUSPENDED)
}
