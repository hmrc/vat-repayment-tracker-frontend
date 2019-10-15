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

package service

import connectors.PaymentsOrchestratorConnector
import javax.inject.{Inject, Singleton}
import model.des._
import model.{AllRepaymentData, RepaymentData, Vrn}
import views.Views

import scala.concurrent.ExecutionContext

@Singleton
class PaymentsOrchestratorService @Inject() (
    desConnector: PaymentsOrchestratorConnector,
    views:        Views)
  (
    implicit
    ec: ExecutionContext) {

  def getAllRepaymentData(financialData: Option[FinancialData], repaymentDetails: Option[Seq[RepaymentDetailData]], vrn: Vrn): AllRepaymentData = {

    financialData match {
      case Some(fd) => {
        repaymentDetails match {
          case Some(rd) => {
            val data = getRepaymentData(fd, rd, vrn)
            val currentData: List[RepaymentData] = data.filter(f => f.riskingStatus == INITIAL.value || f.riskingStatus == SENT_FOR_RISKING.value || f.riskingStatus == CLAIM_QUERIED.value).toList
            val completed: List[RepaymentData] = data.filter(f => f.riskingStatus == REPAYMENT_ADJUSTED.value || f.riskingStatus == ADJUSTMENT_TO_TAX_DUE.value || f.riskingStatus == REPAYMENT_APPROVED.value).toList
            AllRepaymentData(
              currentData,
              completed
            )
          }
          case None => dealWithNodata
        }

      }
      case None => dealWithNodata
    }
  }

  private def getRepaymentData(financialData: FinancialData, repaymentDetails: Seq[RepaymentDetailData], vrn: Vrn): Seq[RepaymentData] = {

    for {
      fd <- financialData.financialTransactions.filter(f => f.chargeType == "VAT Return Credit Charge")
      rd <- repaymentDetails
      if (fd.periodKey == rd.periodKey)
    } yield (RepaymentData(fd.periodKeyDescription, fd.originalAmount, rd.returnCreationDate, rd.riskingStatus, fd.periodKeyDescription))

  }

  private def dealWithNodata: AllRepaymentData = {
    AllRepaymentData(List[RepaymentData](), List[RepaymentData]())
  }

}
