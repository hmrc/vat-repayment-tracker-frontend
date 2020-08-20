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

package formaters

import javax.inject.{Inject, Singleton}
import model._
import model.des.CustomerInformation
import model.vat.{CalendarData, VatDesignatoryDetailsAddress}
import play.api.Logger
import play.api.mvc.{Request, Result, Results}
import views.Views

import scala.concurrent.ExecutionContext

@Singleton
class ShowResultsFormatter @Inject() (views:            Views,
                                      desFormatter:     DesFormatter,
                                      addressFormatter: AddressFormatter)(implicit ec: ExecutionContext) extends Results {

  private val logger = Logger(this.getClass)

  def computeViewClassic(
      vrn:                          Vrn,
      calendarData:                 Option[CalendarData],
      vatDesignatoryDetailsAddress: VatDesignatoryDetailsAddress
  )(implicit request: Request[_]): Result = {

    val address = addressFormatter.getFormattedAddressNonMtd(vatDesignatoryDetailsAddress)

    calendarData match {
      case Some(data) => if (data.countReturns == 0) Ok(views.classic_none(vrn, address)) else Ok(views.classic_some(vrn, data.latestReceivedOnFormatted, address))
      case None       => Ok(views.classic_none(vrn, address))
    }
  }

  def computeView(
      allRepaymentData: AllRepaymentData,
      customerData:     Option[CustomerInformation],
      vrn:              Vrn
  )(implicit request: Request[_]): Result = {

    val bankDetailsExist = desFormatter.getBankDetailsExist(customerData)
    val bankDetails = desFormatter.getBankDetails(customerData)
    val addressDetails = desFormatter.getAddressDetails(customerData)
    val addressDetailsExist = desFormatter.getAddressDetailsExist(customerData)
    val inflightBankDetails = desFormatter.bankDetailsInFlight(customerData)

    bankDetails match {
      case Some(bd) => if (bd.accountHolderName.isEmpty) logger.warn(s"VRT no account holder name for vrn : ${vrn.value}")
      case None     =>
    }

    if (allRepaymentData.inProgressRepaymentData.nonEmpty && allRepaymentData.completedRepaymentData.nonEmpty) {
      Ok(views.inprogress_completed(
        allRepaymentData.inProgressRepaymentData,
        allRepaymentData.completedRepaymentData,
        bankDetailsExist,
        bankDetails,
        addressDetails,
        addressDetailsExist,
        vrn,
        inflightBankDetails
      ))
    } else if (allRepaymentData.inProgressRepaymentData.isEmpty && allRepaymentData.completedRepaymentData.nonEmpty) {
      Ok(views.completed(
        allRepaymentData.completedRepaymentData,
        bankDetailsExist,
        bankDetails,
        addressDetails,
        addressDetailsExist,
        vrn,
        inflightBankDetails
      ))
    } else if (allRepaymentData.inProgressRepaymentData.nonEmpty && allRepaymentData.completedRepaymentData.isEmpty) {
      Ok(views.inprogress(
        allRepaymentData.inProgressRepaymentData,
        bankDetailsExist,
        bankDetails,
        addressDetails,
        addressDetailsExist,
        vrn,
        inflightBankDetails
      ))
    } else {
      Ok(
        views.no_vat_repayments(
          bankDetailsExist,
          bankDetails,
          addressDetails,
          addressDetailsExist,
          vrn,
          inflightBankDetails
        ))

    }
  }

}
