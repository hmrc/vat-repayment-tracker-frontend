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
import formaters.{AddressFormter, DesFormatter, ShowResultsFormatter, ViewProgressFormatter}
import javax.inject.{Inject, Singleton}
import langswitch.ErrorMessages
import model._
import model.des._
import play.api.Logger
import play.api.data.Form
import play.api.data.Forms.{mapping, optional, text}
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
    desConnector:                        PaymentsOrchestratorConnector,
    requestSupport:                      RequestSupport,
    addressFormater:                     AddressFormter,
    desFormatter:                        DesFormatter,
    actions:                             Actions,
    viewConfig:                          ViewConfig,
    directDebitBackendController:        DirectDebitBackendConnector,
    bankAccountCocConnector:             BankAccountCocConnector,
    paymentsOrchestratorService:         VrtService,
    vatRepaymentTrackerBackendConnector: VatRepaymentTrackerBackendConnector,
    viewProgressFormatter:               ViewProgressFormatter,
    showResultsFormatter:                ShowResultsFormatter,
    payApiConnector:                     PayApiConnector,
    auditor:                             Auditor)(
    implicit
    ec: ExecutionContext)

  extends FrontendBaseController(cc) {

  import requestSupport._

  def nonMtdUser(): Action[AnyContent] = actions.loggedIn.async { implicit request =>
    Future.successful(Ok(views.non_mtd_user()))
  }

  def showVrt: Action[AnyContent] = actions.securedAction.async {
    implicit request: AuthenticatedRequest[_] =>

      Logger.debug(s"IsPartialMigration set to ${request.isPartialMigration}")
      request.typedVrn match {
        case TypedVrn.ClassicVrn(vrn) => {
          Logger.debug("Received a classic VRN")
          Future.successful(Ok(views.non_mtd_user()))
        }
        case TypedVrn.MtdVrn(vrn) => {
          Logger.debug("Received a  MTD VRN")
          val customerDataF = desConnector.getCustomerData(request.typedVrn.vrn)
          val repaymentDetailsF = desConnector.getRepaymentsDetails(request.typedVrn.vrn)
          val financialDataF = desConnector.getFinancialData(request.typedVrn.vrn)
          for {
            customerData <- customerDataF
            repaymentDetails <- repaymentDetailsF
            financialData <- financialDataF
          } yield (
            showResultsFormatter.computeView(paymentsOrchestratorService.getAllRepaymentData(repaymentDetails, request.typedVrn.vrn, financialData), customerData, request.typedVrn.vrn)
          )
        }
      }

  }

  def startPaymentsJourney(amountInPence: Long): Action[AnyContent] =
    actions.securedActionMtdVrnCheck.async { implicit request: AuthenticatedRequest[_] =>
      for {
        response <- payApiConnector.startJourney(amountInPence, request.typedVrn.vrn)
      } yield {
        Logger.debug(s"received ${response.toString}")
        Redirect(response.nextUrl)
      }
    }

  def viewProgress(periodKey: PeriodKey): Action[AnyContent] =
    actions.securedActionMtdVrnCheck.async { implicit request: AuthenticatedRequest[_] =>

      val customerDataF = desConnector.getCustomerData(request.typedVrn.vrn)
      val financialDataF = desConnector.getFinancialData(request.typedVrn.vrn)
      Logger.debug(s"""received vrn : ${request.typedVrn.vrn.value}, periodKey: ${periodKey.value}""")
      for {
        customerData <- customerDataF
        financialData <- financialDataF
        vrd <- vatRepaymentTrackerBackendConnector.find(request.typedVrn.vrn, periodKey)
      } yield {
        viewProgressFormatter.computeViewProgress(request.typedVrn.vrn, periodKey, vrd, customerData, financialData)
      }
    }

  def startBankAccountCocJourney(returnPage: ReturnPage, audit: Boolean): Action[AnyContent] =
    actions.securedActionMtdVrnCheck.async { implicit request: AuthenticatedRequest[_] =>

      if (audit) {
        Logger.debug("startBankAccountCocJourney... trying to audit")
        val repaymentDetailsF = desConnector.getRepaymentsDetails(request.typedVrn.vrn)
        val financialDataF = desConnector.getFinancialData(request.typedVrn.vrn)

        for {
          repaymentDetails <- repaymentDetailsF
          financialData <- financialDataF
          allRepayments = paymentsOrchestratorService.getAllRepaymentData(repaymentDetails, request.typedVrn.vrn, financialData)
          auditRes <- auditor.audit(allRepayments.inProgressRepaymentData, "initiateChangeVATRepaymentBankAccount", "initiate-change-vat-repayment-bank-account")
          nextUrl <- bankAccountCocConnector.startJourney(request.typedVrn.vrn, returnPage)
        } yield {
          Redirect(nextUrl.nextUrl)
        }
      } else {
        Logger.debug("startBankAccountCocJourney... will not audit")
        for {
          nextUrl <- bankAccountCocConnector.startJourney(request.typedVrn.vrn, returnPage)
        } yield Redirect(nextUrl.nextUrl)
      }

    }

  def signout: Action[AnyContent] =
    Action.async { implicit request =>
      Redirect(viewConfig.feedbackUrlForLogout).withNewSession
    }

  def manageOrTrackVrt: Action[AnyContent] =
    actions.securedActionMtdVrnCheck.async { implicit request: AuthenticatedRequest[_] =>

      manageOrTrackView(request.typedVrn.vrn, manageOrTrackForm.fill(ManageOrTrack(None)))
    }

  //deprecate this when the URL changes to vat-repayment-tracker
  def manageOrTrack(vrn: Vrn): Action[AnyContent] =
    actions.securedActionMtdVrnCheck.async { implicit request: AuthenticatedRequest[_] =>

      manageOrTrackView(request.typedVrn.vrn, manageOrTrackForm.fill(ManageOrTrack(None)))
    }

  private def manageOrTrackView(vrn: Vrn, form: Form[ManageOrTrack])(
      implicit
      request: Request[_]): Future[Result] = {

    val customerDataF = desConnector.getCustomerData(vrn)
    val ddDataF = desConnector.getDDData(vrn)
    val chosenUrl = for {
      customerData <- customerDataF
      ddData <- ddDataF
    } yield {

      val bankDetails: Option[BankDetails] = desFormatter.getBankDetails(customerData)
      val ddDetails: Option[BankDetails] = desFormatter.getDDData(ddData)
      Ok(views.manage_or_track(vrn, bankDetails, ddDetails, form, desFormatter.bankDetailsInFlight(customerData)))
    }
    chosenUrl

  }

  private def manageOrTrackForm(implicit request: Request[_]): Form[ManageOrTrack] = {
    Form(mapping(
      "manage" -> optional(text).verifying(ErrorMessages.`choose an option`.show, _.nonEmpty))(ManageOrTrack.apply)(ManageOrTrack.unapply))
  }

  def manageOrTrackSubmit(): Action[AnyContent] = actions.securedActionMtdVrnCheck.async {
    implicit request: AuthenticatedRequest[_] =>

      manageOrTrackForm.bindFromRequest().fold(
        formWithErrors => {
          manageOrTrackView(request.typedVrn.vrn, formWithErrors)
        },
        {
          valueInForm =>
            {
              valueInForm.choice match {
                case Some(choice) => {
                  choice match {
                    case ManageOrTrackOptions.vrt.value    => Redirect(routes.Controller.showVrt())
                    case ManageOrTrackOptions.bank.value   => Redirect(routes.Controller.viewRepaymentAccount(false))
                    case ManageOrTrackOptions.nobank.value => Redirect(routes.Controller.startBankAccountCocJourney(ReturnPage("manage-or-track-vrt")))
                    case ManageOrTrackOptions.nodd.value =>
                      for {
                        nextUrl <- directDebitBackendController.startJourney(request.typedVrn.vrn)
                      } yield Redirect(nextUrl.nextUrl)
                    case ManageOrTrackOptions.dd.value =>
                      for {
                        nextUrl <- directDebitBackendController.startJourney(request.typedVrn.vrn)
                      } yield Redirect(nextUrl.nextUrl)
                  }
                }
                case None => {
                  manageOrTrackView(request.typedVrn.vrn, manageOrTrackForm.fill(ManageOrTrack(None)).withError("manage", ErrorMessages.`choose an option`.show))
                }
              }
            }
        }
      )

  }

  //------------------------------------------------------------------------------------------------------------------------------

  //deprecate this when the URL changes to vat-repayment-tracker
  def showResults(vrn: Vrn): Action[AnyContent] = actions.securedActionMtdVrnCheck.async {
    implicit request: AuthenticatedRequest[_] =>
      val customerDataF = desConnector.getCustomerData(request.typedVrn.vrn)
      val repaymentDetailsF = desConnector.getRepaymentsDetails(request.typedVrn.vrn)
      val financialDataF = desConnector.getFinancialData(request.typedVrn.vrn)

      val result = for {
        customerData <- customerDataF
        repaymentDetails <- repaymentDetailsF
        financialData <- financialDataF
      } yield (
        showResultsFormatter.computeView(paymentsOrchestratorService.getAllRepaymentData(repaymentDetails, request.typedVrn.vrn, financialData), customerData, request.typedVrn.vrn)
      )

      result

  }

  def viewRepaymentAccount(audit: Boolean): Action[AnyContent] = actions.securedActionMtdVrnCheck.async {
    implicit request: AuthenticatedRequest[_] =>

      val customerDataF = desConnector.getCustomerData(request.typedVrn.vrn)
      val url = for {
        customerData <- customerDataF
      } yield {
        val bankDetails = desFormatter.getBankDetails(customerData)
        Ok(views.view_repayment_account(bankDetails, request.typedVrn.vrn, ReturnPage("view-repayment-account"), audit))
      }

      url

  }

}
