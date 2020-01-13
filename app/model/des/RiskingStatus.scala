/*
 * Copyright 2020 HM Revenue & Customs
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

sealed trait RiskingStatus { def value: String }

case object INITIAL extends RiskingStatus { val value = "INITIAL" } //S001
case object SENT_FOR_RISKING extends RiskingStatus { val value = "SENT_FOR_RISKING" } //S002

case object CLAIM_QUERIED extends RiskingStatus { val value = "CLAIM_QUERIED" } //S003
case object REPAYMENT_ADJUSTED extends RiskingStatus { val value = "REPAYMENT_ADJUSTED" } //S004

//This is spelt wrong in the DES schema !!!
case object ADJUSMENT_TO_TAX_DUE extends RiskingStatus { val value = "ADJUSMENT_TO_TAX_DUE" } //S005
case object REPAYMENT_APPROVED extends RiskingStatus { val value = "REPAYMENT_APPROVED" } //S006
