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

package controllers

import config.ViewConfig
import connectors._
import controllers.action.{Actions, AuthenticatedRequest}
import formaters.{DesFormatter, ShowResultsFormatter, ViewProgressFormatter}
import javax.inject.{Inject, Singleton}
import model._
import play.api.Logger
import play.api.mvc.{Action, _}
import req.RequestSupport
import service.VrtService
import views.Views

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class Controller @Inject() (
    cc:                                  ControllerComponents,
    errorHandler:                        ErrorHandler,
    views:                               Views,
    paymentsOrchestratorConnector:       PaymentsOrchestratorConnector,
    requestSupport:                      RequestSupport,
    desFormatter:                        DesFormatter,
    actions:                             Actions,
    viewConfig:                          ViewConfig,
    vrtService:                          VrtService,
    vatRepaymentTrackerBackendConnector: VatRepaymentTrackerBackendConnector,
    viewProgressFormatter:               ViewProgressFormatter,
    showResultsFormatter:                ShowResultsFormatter,
    vatConnector:                        VatConnector)(
    implicit
    ec: ExecutionContext)

  extends FrontendBaseController(cc) {

  import requestSupport._

  def nonMtdUser(): Action[AnyContent] = actions.loggedIn.async { implicit request =>
    Future.successful(Ok(views.non_mtd_user()))
  }

  def signout: Action[AnyContent] =
    Action.async { implicit request =>
      Redirect(viewConfig.feedbackUrlForLogout).withNewSession
    }

  def showVrt: Action[AnyContent] = actions.securedAction.async {
    implicit request: AuthenticatedRequest[_] =>

      Logger.debug(s"IsPartialMigration set to ${request.isPartialMigration}")
      request.typedVrn match {

        case TypedVrn.ClassicVrn(vrnNonMtd) =>
          Logger.debug("Received a classic VRN")
          val calendarDataF = vatConnector.calendar(vrnNonMtd)
          val designatoryDetailsF = vatConnector.designatoryDetails(vrnNonMtd)
          for {
            calendarData <- calendarDataF
            designatoryDetails <- designatoryDetailsF
          } yield showResultsFormatter.computeViewClassic(vrnNonMtd, calendarData, designatoryDetails)

        case TypedVrn.MtdVrn(vrnMtd) =>
          Logger.debug("Received a  MTD VRN")
          val customerDataF = paymentsOrchestratorConnector.getCustomerData(vrnMtd)
          val repaymentDetailsF = paymentsOrchestratorConnector.getRepaymentsDetails(vrnMtd)
          val financialDataF = paymentsOrchestratorConnector.getFinancialData(vrnMtd)
          for {
            customerData <- customerDataF
            repaymentDetails <- repaymentDetailsF
            financialData <- financialDataF
          } yield showResultsFormatter.computeView(vrtService.getAllRepaymentData(repaymentDetails, vrnMtd, financialData), customerData, vrnMtd)
      }

  }

  def viewProgress(periodKey: PeriodKey): Action[AnyContent] =
    actions.securedActionMtdVrnCheck.async { implicit request: AuthenticatedRequest[_] =>

      val customerDataF = paymentsOrchestratorConnector.getCustomerData(request.typedVrn.vrn)
      val financialDataF = paymentsOrchestratorConnector.getFinancialData(request.typedVrn.vrn)
      Logger.debug(s"""received vrn : ${request.typedVrn.vrn.value}, periodKey: ${periodKey.value}""")
      for {
        customerData <- customerDataF
        financialData <- financialDataF
        vrd <- vatRepaymentTrackerBackendConnector.find(request.typedVrn.vrn, periodKey)
      } yield {
        viewProgressFormatter.computeViewProgress(request.typedVrn.vrn, periodKey, vrd, customerData, financialData)
      }
    }

  def viewRepaymentAccount(audit: Boolean): Action[AnyContent] = actions.securedActionMtdVrnCheck.async {
    implicit request: AuthenticatedRequest[_] =>

      val customerDataF = paymentsOrchestratorConnector.getCustomerData(request.typedVrn.vrn)
      val url = for {
        customerData <- customerDataF
      } yield {
        val bankDetails = desFormatter.getBankDetails(customerData)
        Ok(views.view_repayment_account(bankDetails, request.typedVrn.vrn, ReturnPage("view-repayment-account"), audit))
      }

      url

  }

  //------------------------------------------------------------------------------------------------------------------------------

  //deprecate this when the URL changes to vat-repayment-tracker
  def showResults(vrn: Vrn): Action[AnyContent] = actions.securedActionMtdVrnCheck.async {
    implicit request: AuthenticatedRequest[_] =>
      val customerDataF = paymentsOrchestratorConnector.getCustomerData(request.typedVrn.vrn)
      val repaymentDetailsF = paymentsOrchestratorConnector.getRepaymentsDetails(request.typedVrn.vrn)
      val financialDataF = paymentsOrchestratorConnector.getFinancialData(request.typedVrn.vrn)

      val result = for {
        customerData <- customerDataF
        repaymentDetails <- repaymentDetailsF
        financialData <- financialDataF
      } yield showResultsFormatter.computeView(vrtService.getAllRepaymentData(repaymentDetails, request.typedVrn.vrn, financialData), customerData, request.typedVrn.vrn)

      result

  }

}
