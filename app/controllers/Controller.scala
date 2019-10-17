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

import config.ViewConfig
import connectors.{BankAccountCocConnector, DirectDebitBackendConnector, PaymentsOrchestratorConnector, VatRepaymentTrackerBackendConnector}
import controllers.action.Actions
import format.{AddressFormter, DesFormatter}
import javax.inject.{Inject, Singleton}
import langswitch.ErrorMessages
import model._
import model.des.{CustomerInformation, _}
import play.api.Logger
import play.api.data.Form
import play.api.data.Forms.{mapping, optional, text}
import play.api.mvc._
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
    vatRepaymentTrackerBackendConnector: VatRepaymentTrackerBackendConnector)(
    implicit
    ec: ExecutionContext)

  extends FrontendBaseController(cc) {

  import requestSupport._

  def viewProgress(vrn: Vrn, periodKey: PeriodKey): Action[AnyContent] =
    actions.securedAction(vrn).async { implicit request =>
      Logger.warn(s"""received vrn : ${vrn.value}, periodKey: ${periodKey.value}""")
      for {
        vrd <- vatRepaymentTrackerBackendConnector.find(vrn, periodKey)
      } yield {
        vrd.map (m => Logger.debug(s"Returned ${m.toString}"))
        Ok("Not implemented yet")
      }
    }

  def startBankAccountCocJourney(vrn: Vrn, returnPage: ReturnPage): Action[AnyContent] =
    actions.securedAction(vrn).async { implicit request =>

      for {
        nextUrl <- bankAccountCocConnector.startJourney(vrn, returnPage)
      } yield Redirect(nextUrl.nextUrl)

    }

  def signout: Action[AnyContent] =
    Action.async { implicit request =>
      Redirect(viewConfig.feedbackUrlForLogout).withNewSession
    }

  def manageOrTrack(vrn: Vrn): Action[AnyContent] =
    actions.securedAction(vrn).async { implicit request =>

      manageOrTrackView(vrn, manageOrTrackForm.fill(ManageOrTrack(None)))
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
      Ok(views.manage_or_track(vrn, bankDetails, ddDetails, form)).addingToSession(("vrn", vrn.value))
    }
    chosenUrl

  }

  private def manageOrTrackForm(implicit request: Request[_]): Form[ManageOrTrack] = {
    Form(mapping(
      "manage" -> optional(text).verifying(ErrorMessages.`choose an option`.show, _.nonEmpty))(ManageOrTrack.apply)(ManageOrTrack.unapply))
  }

  def manageOrTrackSubmit(): Action[AnyContent] = actions.securedActionFromSession.async {
    implicit request =>

      val vrn = request.session.get("vrn") match {
        case Some(vrnString) => Vrn(vrnString)
        case None            => throw new RuntimeException("Could not get VRN from session")
      }

      manageOrTrackForm.bindFromRequest().fold(
        formWithErrors => {
          manageOrTrackView(vrn, formWithErrors)
        },
        {
          valueInForm =>
            {
              valueInForm.choice match {
                case Some(choice) => {
                  choice match {
                    case ManageOrTrackOptions.vrt.value    => Redirect(routes.Controller.showResults(vrn))
                    case ManageOrTrackOptions.bank.value   => Redirect(routes.Controller.viewRepaymentAccount(vrn))
                    case ManageOrTrackOptions.nobank.value => Redirect(routes.Controller.startBankAccountCocJourney(vrn, ReturnPage("manage-or-track")))
                    case ManageOrTrackOptions.nodd.value =>
                      for {
                        nextUrl <- directDebitBackendController.startJourney(vrn)
                      } yield Redirect(nextUrl.nextUrl)
                    case ManageOrTrackOptions.dd.value =>
                      for {
                        nextUrl <- directDebitBackendController.startJourney(vrn)
                      } yield Redirect(nextUrl.nextUrl)
                  }
                }
                case None => {
                  manageOrTrackView(vrn, manageOrTrackForm.fill(ManageOrTrack(None)).withError("manage", ErrorMessages.`choose an option`.show))
                }
              }
            }
        }
      )

  }

  //------------------------------------------------------------------------------------------------------------------------------

  def showResults(vrn: Vrn): Action[AnyContent] = actions.securedAction(vrn).async {
    implicit request: Request[_] =>
      val financialDataF = desConnector.getFinancialData(vrn)
      val customerDataF = desConnector.getCustomerData(vrn)
      val repaymentDetailsF = desConnector.getRepaymentsDetails(vrn)

      val result = for {
        financialData <- financialDataF
        customerData <- customerDataF
        repaymentDetails <- repaymentDetailsF
      } yield (
        computeView(paymentsOrchestratorService.getAllRepaymentData(financialData, repaymentDetails, vrn), customerData, vrn)
      )

      result

  }

  private def computeView(
      allRepaymentData: AllRepaymentData,
      customerData:     Option[CustomerInformation],
      vrn:              Vrn
  )(implicit request: Request[_]): Result = {

    val bankDetailsExist = desFormatter.getBankDetailsExist(customerData)
    val bankDetails = desFormatter.getBankDetails(customerData)
    val addressDetails = desFormatter.getAddressDetails(customerData)
    val addressDetailsExist = desFormatter.getAddressDetailsExist(customerData)

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
        vrn
      ))
    } else if ((allRepaymentData.inProgressRepaymentData.size == 0) && (allRepaymentData.completedRepaymentData.size > 0)) {
      Ok(views.completed(
        allRepaymentData.completedRepaymentData,
        bankDetailsExist,
        bankDetails,
        addressDetails,
        addressDetailsExist,
        vrn
      ))
    } else if ((allRepaymentData.inProgressRepaymentData.size > 0) && (allRepaymentData.completedRepaymentData.size == 0)) {
      Ok(views.inprogress(
        allRepaymentData.inProgressRepaymentData,
        bankDetailsExist,
        bankDetails,
        addressDetails,
        addressDetailsExist,
        vrn
      ))
    } else {
      Ok(
        views.no_vat_repayments(
          bankDetailsExist,
          bankDetails,
          addressDetails,
          addressDetailsExist,
          vrn
        ))

    }
  }

  def viewRepaymentAccount(vrn: Vrn): Action[AnyContent] = actions.securedAction(vrn).async {
    implicit request: Request[_] =>

      val customerDataF = desConnector.getCustomerData(vrn)
      val url = for {
        customerData <- customerDataF
      } yield {
        val bankDetails = desFormatter.getBankDetails(customerData)
        Ok(views.view_repayment_account(bankDetails, vrn, ReturnPage("view-repayment-account")))
      }

      url

  }

}
