/*
 * Copyright 2022 HM Revenue & Customs
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
import formaters.DesFormatter

import javax.inject.{Inject, Singleton}
import model._
import model.des._
import play.api.data.Form
import play.api.data.Forms.{mapping, optional, text}
import play.api.i18n.{Lang, Langs, Messages, MessagesApi, MessagesImpl, MessagesProvider}
import play.api.mvc.{Action, _}
import req.RequestSupport

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ManageOrTrackController @Inject() (
    cc:                          ControllerComponents,
    manage_or_track:             views.html.manage_or_track,
    desConnector:                PaymentsOrchestratorConnector,
    requestSupport:              RequestSupport,
    desFormatter:                DesFormatter,
    actions:                     Actions,
    viewConfig:                  ViewConfig,
    directDebitBackendConnector: DirectDebitBackendConnector)(
    implicit
    ec: ExecutionContext)
  extends FrontendBaseController(cc) {

  def manageOrTrackVrt: Action[AnyContent] =
    actions.securedActionMtdVrnCheckWithoutShutterCheck.async { implicit request: AuthenticatedRequest[_] =>
      import requestSupport._
      if (viewConfig.isShuttered)
        manageOrTrackViewShuttered(request.typedVrn.vrn, manageOrTrackForm.fill(ManageOrTrack(None)))
      else
        manageOrTrackView(request.typedVrn.vrn, manageOrTrackForm.fill(ManageOrTrack(None)))
    }

  def manageOrTrackSubmit(): Action[AnyContent] = actions.securedActionMtdVrnCheck.async {
    implicit request: AuthenticatedRequest[_] =>
      import requestSupport._
      manageOrTrackForm.bindFromRequest().fold(
        formWithErrors => {
          manageOrTrackView(request.typedVrn.vrn, formWithErrors)
        },
        {
          valueInForm =>
            {
              valueInForm.choice match {
                case Some(choice) =>
                  choice match {
                    case ManageOrTrackOptions.vrt.value    => Redirect(routes.Controller.showVrt)
                    case ManageOrTrackOptions.bank.value   => Redirect(routes.Controller.viewRepaymentAccount())
                    case ManageOrTrackOptions.nobank.value => Redirect(routes.BankAccountCocController.startBankAccountCocJourney(ReturnPage("manage-or-track-vrt")))
                    case ManageOrTrackOptions.nodd.value =>
                      for {
                        nextUrl <- directDebitBackendConnector.startJourney(request.typedVrn.vrn)
                      } yield Redirect(nextUrl.nextUrl)
                    case ManageOrTrackOptions.dd.value =>
                      for {
                        nextUrl <- directDebitBackendConnector.startJourney(request.typedVrn.vrn)
                      } yield Redirect(nextUrl.nextUrl)
                  }
                case None =>
                  manageOrTrackView(request.typedVrn.vrn, manageOrTrackForm.fill(ManageOrTrack(None)).withError("manage", Messages("manage_or_track_controller.choose_an_option")))
              }
            }
        }
      )

  }

  private def manageOrTrackViewShuttered(vrn: Vrn, form: Form[ManageOrTrack])(
      implicit
      messages: Messages,
      request:  Request[_]): Future[Result] = {

    Ok(manage_or_track(vrn, None, None, form, inflightBankDetails = false))

  }

  private def manageOrTrackView(vrn: Vrn, form: Form[ManageOrTrack])(
      implicit
      request: Request[_], messages: Messages): Future[Result] = {

    val customerDataF = desConnector.getCustomerData(vrn)
    val ddDataF = desConnector.getDDData(vrn)
    val chosenUrl = for {
      customerData <- customerDataF
      ddData <- ddDataF
    } yield {

      val bankDetails: Option[BankDetails] = desFormatter.getBankDetails(customerData)
      val ddDetails: Option[BankDetails] = desFormatter.getDDData(ddData)
      Ok(manage_or_track(vrn, bankDetails, ddDetails, form, desFormatter.bankDetailsInFlight(customerData)))
    }
    chosenUrl

  }

  private def manageOrTrackForm(implicit request: Request[_], messages: Messages): Form[ManageOrTrack] = {
    Form(mapping(
      "manage" -> optional(text).verifying(Messages("manage_or_track_controller.choose_an_option"), _.nonEmpty))(ManageOrTrack.apply)(ManageOrTrack.unapply))
  }

  //------------------------------------------------------------------------------------------------------------------------------

  //deprecate this when the URL changes to vat-repayment-tracker
  def manageOrTrack(vrn: Vrn): Action[AnyContent] =
    actions.securedActionMtdVrnCheckWithoutShutterCheck.async { implicit request: AuthenticatedRequest[_] =>
      import requestSupport._

      if (viewConfig.isShuttered)
        manageOrTrackViewShuttered(request.typedVrn.vrn, manageOrTrackForm.fill(ManageOrTrack(None)))
      else
        manageOrTrackView(request.typedVrn.vrn, manageOrTrackForm.fill(ManageOrTrack(None)))
    }
}
