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

package formaters

import connectors.Auditor.`repayment-type`

import javax.inject.{Inject, Singleton}
import model._
import model.des.CustomerInformation
import model.vat.{CalendarData, VatDesignatoryDetailsAddress}
import play.api.Logger
import play.api.i18n.Messages
import play.api.mvc.{Request, Result, Results}

@Singleton
class ShowResultsFormatter @Inject() (
    no_vat_repayments:    views.html.no_vat_repayments,
    classic_none:         views.html.classic.no_vat_repayments_classic,
    classic_some:         views.html.classic.vat_repayments_classic,
    inprogress_completed: views.html.inprogress_completed,
    completed:            views.html.completed,
    inprogress:           views.html.inprogress,
    desFormatter:         DesFormatter,
    addressFormatter:     AddressFormatter) extends Results {

  private val logger = Logger(this.getClass)

  def computeViewClassic(
      vrn:                          Vrn,
      calendarData:                 Option[CalendarData],
      vatDesignatoryDetailsAddress: VatDesignatoryDetailsAddress
  )(implicit request: Request[_], message: Messages): Result = {

    val address = addressFormatter.getFormattedAddressNonMtd(vatDesignatoryDetailsAddress)

    calendarData match {
      case Some(data) => if (data.countReturns == 0) Ok(classic_none(vrn, address)) else Ok(classic_some(vrn, data.latestReceivedOnFormatted, address))
      case None       => Ok(classic_none(vrn, address))
    }
  }

  def computeEngmtClassic(
      calendarData: Option[CalendarData]
  ): String = {
    calendarData match {
      case Some(data) => if (data.countReturns == 0) `repayment-type`.none_in_progress else `repayment-type`.in_progress_classic
      case None       => `repayment-type`.none_in_progress
    }
  }

  def computeView(
      allRepaymentData: AllRepaymentData,
      customerData:     Option[CustomerInformation],
      vrn:              Vrn
  )(implicit request: Request[_], message: Messages): Result = {

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
      Ok(inprogress_completed(
        allRepaymentData.hasSuspendedPayment,
        allRepaymentData.inProgressRepaymentData,
        allRepaymentData.completedRepaymentData,
        bankDetailsExist,
        bankDetails,
        addressDetails,
        addressDetailsExist,
        inflightBankDetails
      ))
    } else if (allRepaymentData.inProgressRepaymentData.isEmpty && allRepaymentData.completedRepaymentData.nonEmpty) {
      Ok(completed(
        allRepaymentData.completedRepaymentData,
        bankDetailsExist,
        bankDetails,
        addressDetails,
        addressDetailsExist,
        inflightBankDetails
      ))
    } else if (allRepaymentData.inProgressRepaymentData.nonEmpty && allRepaymentData.completedRepaymentData.isEmpty) {
      Ok(inprogress(
        allRepaymentData.hasSuspendedPayment,
        allRepaymentData.inProgressRepaymentData,
        bankDetailsExist,
        bankDetails,
        addressDetails,
        addressDetailsExist,
        inflightBankDetails
      ))
    } else {
      Ok(
        no_vat_repayments(
          bankDetailsExist,
          bankDetails,
          addressDetails,
          addressDetailsExist,
          inflightBankDetails
        ))

    }
  }

  def computeEngmt(
      allRepaymentData: AllRepaymentData
  ): String = {
    if (allRepaymentData.inProgressRepaymentData.nonEmpty || allRepaymentData.completedRepaymentData.nonEmpty) {
      `repayment-type`.one_in_progress_multiple_delayed
    } else {
      `repayment-type`.none_in_progress
    }
  }

}
