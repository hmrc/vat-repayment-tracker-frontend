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
import model.Vrn
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

  def showResults(vrn: Vrn): Action[AnyContent] = Action.async {
    implicit request: Request[_] =>
      af.authorised() {

        val financialF = desConnector.getFinancialData(vrn)
        val customerDataF = desService.getCustomerData(vrn)

        for {
          futureFinancialData <- financialF
          customerData <- customerDataF
          result <- futureFinancialData match {
            case Some(data) => computeView(data, customerData, vrn)
            case None       => Future.successful(Ok(views.no_vat_repayments(customerData.bankDetailsExist, customerData.bankDetails)))
          }
        } yield (result)

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
      data:         FinancialData,
      customerData: ApprovedInformation,
      vrn:          Vrn)(
      implicit
      request: Request[_]): Future[Result] = data.financialTransactions.size match {
    case 0 => Future.successful(Ok(views.no_vat_repayments(customerData.bankDetailsExist, customerData.bankDetails)))
    case 1 => {
      for {
        obligationDates <- desService.getObligations(vrn, data.financialTransactions(0).periodKey)
      } yield Ok(views.one_repayment(data.financialTransactions(0).originalAmount.toString(),
                                     obligationDates,
                                     data.financialTransactions(0).periodKeyDescription,
                                     customerData.bankDetailsExist,
                                     customerData.bankDetails))
    }
    case _ => throw new RuntimeException("todo: implement multiple page")
  }

}
