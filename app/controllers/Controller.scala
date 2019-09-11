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

package controllers

import connectors.des.DesConnector
import javax.inject.{Inject, Singleton}
import langswitch.ErrorMessages
import model.{AllRepaymentData, Vrn}
import model.des._
import play.api.Logger
import play.api.mvc._
import req.RequestSupport
import service.des.DesService
import uk.gov.hmrc.auth.core.{AuthorisationException, AuthorisedFunctions}
import views.Views

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class Controller @Inject() (
    cc:             ControllerComponents,
    errorHandler:   ErrorHandler,
    views:          Views,
    desConnector:   DesConnector,
    desService:     DesService,
    requestSupport: RequestSupport,
    af:             AuthorisedFunctions)(
    implicit
    ec: ExecutionContext)

  extends FrontendBaseController(cc) {

  import requestSupport._

  def manageOrTrack(vrn: Vrn): Action[AnyContent] = Action.async {
    implicit request: Request[_] =>
      af.authorised() {

        Future.successful(Ok("manageOrTrack"))
      }.recoverWith {
        case e: AuthorisationException => authorisationException(e)
      }
  }

  def showResults(vrn: Vrn): Action[AnyContent] = Action.async {
    implicit request: Request[_] =>
      af.authorised() {

        val financialDataF = desConnector.getFinancialData(vrn)
        val customerDataF = desService.getCustomerData(vrn)
        val obligationDataF = desConnector.getObligations(vrn)

        val result = for {
          financialData <- financialDataF
          customerData <- customerDataF
          obligationData <- obligationDataF
        } yield (
          computeView(desService.getAllRepaymentData(financialData, obligationData, vrn), customerData, vrn)
        )

        result

      }.recoverWith {
        case e: AuthorisationException => authorisationException(e)
      }
  }

  def viewRepaymentAccount(accountHolderName: AccountHolderName, bankAccountNumber: BankAccountNumber, sortCode: SortCode): Action[AnyContent] = Action.async {
    implicit request: Request[_] =>
      af.authorised() {
        val bankDetails: BankDetails = BankDetails(accountHolderName, bankAccountNumber, sortCode)
        Future.successful(Ok(views.view_repayment_account(bankDetails)))
      }.recoverWith {
        case e: AuthorisationException => authorisationException(e)
      }
  }

  def authorisationException(e: AuthorisationException)(
      implicit
      request: Request[_]): Future[Result] = {
    Logger.debug(s"Unauthorised because of ${
      e.reason
    }, $e")
    Future.successful(Unauthorized(
      errorHandler.standardErrorTemplate(
        ErrorMessages.`You do not have access to this service`.show,
        ErrorMessages.`You do not have access to this service`.show,
        "")))

  }

  def computeView(
      allRepaymentData: AllRepaymentData,
      customerData:     ApprovedInformation,
      vrn:              Vrn
  )(implicit request: Request[_]): Result = {

    val showCurrent = allRepaymentData.currentRepaymentData.isDefined
    val overDueSize = allRepaymentData.overDueRepaymentData.fold(0)(_.size)
    if ((showCurrent == false) && (overDueSize == 0)) {
      Ok(views.no_vat_repayments(customerData.bankDetailsExist, customerData.bankDetails))
    } else if (showCurrent && (overDueSize == 0)) {
      Ok(views.one_repayment(allRepaymentData.getCurrentRepaymentData,
                             customerData.bankDetailsExist,
                             customerData.bankDetails))
    } else throw new RuntimeException(s"""View not configured for overDueSize: ${overDueSize}, showCurrent: ${showCurrent}""")

  }

}
