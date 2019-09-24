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

import connectors.PaymentsOrchestratorConnector
import controllers.action.Actions
import format.{AddressFormter, DesFormatter}
import javax.inject.{Inject, Singleton}
import langswitch.ErrorMessages
import model._
import model.des.{CustomerInformation, _}
import play.api.data.Form
import play.api.data.Forms.{mapping, optional, text}
import play.api.mvc._
import req.RequestSupport
import service.PaymentsOrchestratorService
import views.Views

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class Controller @Inject() (
    cc:              ControllerComponents,
    errorHandler:    ErrorHandler,
    views:           Views,
    desConnector:    PaymentsOrchestratorConnector,
    desService:      PaymentsOrchestratorService,
    requestSupport:  RequestSupport,
    addressFormater: AddressFormter,
    desFormatter:    DesFormatter,
    actions:         Actions)(
    implicit
    ec: ExecutionContext)

  extends FrontendBaseController(cc) {

  import requestSupport._

  def signout: Action[AnyContent] =
    Action.async { implicit request =>
      Future.successful(Ok("Signed out"))
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
                    case ManageOrTrackOptions.vrt.value  => Redirect(routes.Controller.showResults(vrn))
                    case ManageOrTrackOptions.bank.value => Redirect(routes.Controller.viewRepaymentAccount(vrn))
                    case ManageOrTrackOptions.dd.value   => Future.successful(Ok(s"""TODO: manage_or_track_submit ${choice} ${vrn.value} """))
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
      val obligationDataF = desConnector.getObligations(vrn)

      val result = for {
        financialData <- financialDataF
        customerData <- customerDataF
        obligationData <- obligationDataF
      } yield (
        computeView(desService.getAllRepaymentData(financialData, obligationData, vrn), customerData, vrn)
      )

      result

  }

  private def computeView(
      allRepaymentData: AllRepaymentData,
      customerData:     Option[CustomerInformation],
      vrn:              Vrn
  )(implicit request: Request[_]): Result = {

    val showCurrent = allRepaymentData.currentRepaymentData.isDefined
    val overDueSize = allRepaymentData.overDueRepaymentData.fold(0)(_.size)

    val bankDetailsExist = desFormatter.getBankDetailsExist(customerData)
    val bankDetails = desFormatter.getBankDetails(customerData)
    val addressDetails = desFormatter.getAddressDetails(customerData)
    val addressDetailsExist = desFormatter.getAddressDetailsExist(customerData)

    if ((showCurrent == false) && (overDueSize == 0)) {
      Ok(
        views.no_vat_repayments(
          bankDetailsExist,
          bankDetails,
          addressDetails,
          addressDetailsExist,
          vrn
        ))
    } else if (showCurrent && (overDueSize == 0)) {
      Ok(views.one_repayment(
        allRepaymentData.getCurrentRepaymentData,
        bankDetailsExist,
        bankDetails,
        addressDetails,
        addressDetailsExist,
        vrn
      ))
    } else if ((showCurrent == false) && (overDueSize == 1)) {
      Ok(views.one_repayment_delayed(
        allRepaymentData.getOverDueRepaymentData(0),
        bankDetailsExist,
        bankDetails,
        addressDetails,
        addressDetailsExist,
        vrn
      ))
    } else if ((showCurrent == false) && (overDueSize > 1)) {
      Ok(views.multiple_delayed(
        allRepaymentData.getOverDueRepaymentData,
        bankDetailsExist,
        bankDetails,
        addressDetails,
        addressDetailsExist,
        vrn
      ))
    } else if (showCurrent && (overDueSize == 1)) {
      Ok(views.one_repayment_one_dealyed(
        allRepaymentData.getCurrentRepaymentData,
        allRepaymentData.getOverDueRepaymentData(0),
        bankDetailsExist,
        bankDetails,
        addressDetails,
        addressDetailsExist,
        vrn
      ))
    } else if (showCurrent && (overDueSize > 1)) {
      Ok(views.one_repayment_multiple_delayed(
        allRepaymentData.getCurrentRepaymentData,
        allRepaymentData.getOverDueRepaymentData,
        bankDetailsExist,
        bankDetails,
        addressDetails,
        addressDetailsExist,
        vrn
      ))
    } else throw new RuntimeException(s"""View not configured for overDueSize: ${overDueSize}, showCurrent: ${showCurrent}""")

  }

  def viewRepaymentAccount(vrn: Vrn): Action[AnyContent] = actions.securedAction(vrn).async {
    implicit request: Request[_] =>

      val customerDataF = desConnector.getCustomerData(vrn)
      val url = for {
        customerData <- customerDataF
      } yield {
        val bankDetails = desFormatter.getBankDetails(customerData)
        Ok(views.view_repayment_account(bankDetails))
      }

      url

  }

}
