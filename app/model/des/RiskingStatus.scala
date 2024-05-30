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

import enumeratum.PlayEnum

import scala.collection.immutable

object RiskingStatus extends PlayEnum[RiskingStatus] {
  case object INITIAL extends RiskingStatus {
    val complete = false
  } //S001

  case object SENT_FOR_RISKING extends RiskingStatus {
    val complete = false
  } //S002

  case object CLAIM_QUERIED extends RiskingStatus {
    val complete = false
  } //S003

  case object REPAYMENT_ADJUSTED extends RiskingStatus {
    val complete = true
  } //S004

  //This is spelt wrong in the DES schema !!!
  case object ADJUSMENT_TO_TAX_DUE extends RiskingStatus {
    val complete = true
  } //S005

  case object REPAYMENT_APPROVED extends RiskingStatus {
    val complete = true
  } //S006

  case object REPAYMENT_SUSPENDED extends RiskingStatus {
    val complete = false
  }

  override def values: immutable.IndexedSeq[RiskingStatus] = findValues
}

sealed trait RiskingStatus extends enumeratum.EnumEntry {
  val complete: Boolean
  def inProgress: Boolean = !complete
}
