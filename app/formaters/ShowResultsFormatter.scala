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
import play.api.Logger
import play.api.mvc.{Request, Result, Results}
import views.Views

import scala.concurrent.ExecutionContext

@Singleton
class ShowResultsFormatter @Inject() (views:        Views,
                                      desFormatter: DesFormatter)(implicit ec: ExecutionContext) extends Results {

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
      case Some(bd) => if (!(bd.accountHolderName.isDefined)) Logger.warn(s"VRT no account holder name for vrn : ${vrn.value}")
      case None     =>
    }

    if ((allRepaymentData.inProgressRepaymentData.size > 0) && (allRepaymentData.completedRepaymentData.size > 0)) {
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
    } else if ((allRepaymentData.inProgressRepaymentData.size == 0) && (allRepaymentData.completedRepaymentData.size > 0)) {
      Ok(views.completed(
        allRepaymentData.completedRepaymentData,
        bankDetailsExist,
        bankDetails,
        addressDetails,
        addressDetailsExist,
        vrn,
        inflightBankDetails
      ))
    } else if ((allRepaymentData.inProgressRepaymentData.size > 0) && (allRepaymentData.completedRepaymentData.size == 0)) {
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
