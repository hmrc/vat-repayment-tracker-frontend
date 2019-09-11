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

package service.des
import java.time.Clock

import connectors.des.DesConnector
import javax.inject.{Inject, Singleton}
import model.{AllRepaymentData, RepaymentData, Vrn}
import model.des._
import play.api.Logger
import views.Views

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class DesService @Inject() (
    desConnector: DesConnector,
    views:        Views)
  (
    implicit
    ec:    ExecutionContext,
    clock: Clock

) {

  def getCustomerData(vrn: Vrn): Future[ApprovedInformation] = {
    for {
      futureCustomerData <- desConnector.getCustomerData(vrn)

      customerData: ApprovedInformation = futureCustomerData.getOrElse(throw new RuntimeException(
        s"""No Customer data found for VRN: ${
          vrn
        }""")).unWrap(vrn)
    } yield (customerData)
  }

  private def getRepaymentData(financialData: FinancialData, vatObligations: VatObligations, vrn: Vrn) = {

    financialData.financialTransactions.map{
      ft =>
        {
          RepaymentData((vatObligations.obligations.flatMap(x => x.obligationDetails).find(ob => ob.periodKey == ft.periodKey))
            .getOrElse(
              dealWithNoObligations (
                vrn)).inboundCorrespondenceDateReceived,
                        ft.periodKeyDescription,
                        ft.originalAmount)
        }
    }
  }

  def getAllRepaymentData(financialData: Option[FinancialData], vatObligations: Option[VatObligations], vrn: Vrn): AllRepaymentData = {

    financialData match {
      case Some(fd) => {
        vatObligations match {
          case Some(vo) => {
            val data = getRepaymentData(fd, vo, vrn)
            val currentData: Option[RepaymentData] = data.find(f => f.isOverdue == false)
            val overDrawn: List[RepaymentData] = data.filter(f => f.isOverdue == true).toList
            AllRepaymentData(
              currentData,
              if (overDrawn.size == 0) None else Some(overDrawn)
            )
          }
          case None => dealWithNoObligations(vrn)
        }
      }
      case None => AllRepaymentData(None, None)
    }
  }

  private def dealWithNoObligations(vrn: Vrn) = {
    throw new RuntimeException(s"""Problem getting obligationData for vrn ${vrn.value}""")
  }

}
