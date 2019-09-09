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

import java.time.LocalDate

import connectors.des.DesConnector
import javax.inject.{Inject, Singleton}
import model.Vrn
import model.des.{ApprovedInformation, ObligationDates}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class DesService @Inject() (desConnector: DesConnector)(implicit ec: ExecutionContext) {

  def getObligations(vrn: Vrn, periodKey: String): Future[ObligationDates] = {
    for {
      obligations <- desConnector.getObligations(vrn)
      result <- obligations match {
        case None => Future.successful(ObligationDates("", ""))
        case Some(res) => {
          val obligationDetail = res.obligations(0).obligationDetails.find(f => f.periodKey == periodKey)
          val obligationDates = obligationDetail match {
            case None         => ObligationDates("", "")
            case Some(detail) => ObligationDates(detail.inboundCorrespondenceDateReceived.toString, estimatedRepaymentDate(detail.inboundCorrespondenceDateReceived))
          }
          Future.successful(obligationDates)
        }
      }
    } yield (result)
  }

  def getCustomerData(vrn: Vrn): Future[ApprovedInformation] = {
    for {
      futureCustomerData <- desConnector.getCustomerData(vrn)

      customerData: ApprovedInformation = futureCustomerData.getOrElse(throw new RuntimeException(
        s"""No Customer data found for VRN: ${
          vrn
        }""")).unWrap(vrn)
    } yield (customerData)
  }

  private def estimatedRepaymentDate(receivedDate: LocalDate): String = {
    receivedDate.plusDays(30).toString
  }

}
