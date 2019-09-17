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
import format.{AddressFormter, CutomerInformationFormatter}
import javax.inject.{Inject, Singleton}
import langswitch.ErrorMessages
import model.des.{CustomerInformation, _}
import model.{AllRepaymentData, ManageOrTrack, ManageOrTrackOptions, Vrn}
import play.api.Logger
import play.api.data.Form
import play.api.data.Forms.{mapping, optional, text}
import play.api.mvc._
import req.RequestSupport
import service.des.DesService
import uk.gov.hmrc.auth.core.{AuthorisationException, AuthorisedFunctions}
import views.Views

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class Controller @Inject() (
    cc:                           ControllerComponents,
    errorHandler:                 ErrorHandler,
    views:                        Views,
    desConnector:                 DesConnector,
    desService:                   DesService,
    requestSupport:               RequestSupport,
    af:                           AuthorisedFunctions,
    addressFormater:              AddressFormter,
    customerInformationFormatter: CutomerInformationFormatter)(
    implicit
    ec: ExecutionContext)

  extends FrontendBaseController(cc) {

  import requestSupport._

  def manageOrTrack(vrn: Vrn): Action[AnyContent] = Action.async {
    implicit request: Request[_] =>
      af.authorised() {

        manageOrTrackView(vrn, manageOrTrackForm.fill(ManageOrTrack(None, vrn.value)))

      }.recoverWith {
        case e: AuthorisationException => authorisationException(e)
      }
  }

  def manageOrTrackSubmit(): Action[AnyContent] = Action.async {
    implicit request =>
      af.authorised() {
        val mtFormOptional = manageOrTrackForm.bindFromRequest()
        mtFormOptional.value match {
          case Some(mtForm) => mtForm.choice match {
            case Some(choice) => {
              choice match {
                case ManageOrTrackOptions.vrt.value  => Redirect(routes.Controller.showResults(Vrn(mtForm.vrn)))
                case ManageOrTrackOptions.bank.value => Future.successful(Ok(s"""TODO: manage_or_track_submit ${choice} ${mtForm.vrn} """))
                case ManageOrTrackOptions.dd.value   => Future.successful(Ok(s"""TODO: manage_or_track_submit ${choice} ${mtForm.vrn} """))
              }
            }
            case None => {
              manageOrTrackView(Vrn(mtForm.vrn), manageOrTrackForm.fill(ManageOrTrack(None, mtForm.vrn)).withError("manage", ErrorMessages.`choose an option`.show))
            }
          }
          case None => throw new RuntimeException("Could not get form from request")
        }

      }.recoverWith {
        case e: AuthorisationException => authorisationException(e)
      }
  }

  private def manageOrTrackView(vrn: Vrn, form: Form[ManageOrTrack])(
      implicit
      request: Request[_]): Future[Result] =
    {

      val customerDataF = desConnector.getCustomerData(vrn)
      val nextUrl = for {
        customerData <- customerDataF
      } yield {
        val bankDetailsExist = customerInformationFormatter.getBankDetailsExist(customerData)
        val bankDetails: Option[BankDetails] = customerInformationFormatter.getBankDetails(customerData)

        Ok(views.manage_or_track(vrn, false, bankDetailsExist, bankDetails, form))
      }
      nextUrl

    }

  private def manageOrTrackForm(implicit request: Request[_]): Form[ManageOrTrack] = {
    Form(mapping(
      "manage" -> optional(text),
      "vrn" -> text.verifying(ErrorMessages.`VRN missing`.show, _.nonEmpty)
    )(ManageOrTrack.apply)(ManageOrTrack.unapply))
  }

  //------------------------------------------------------------------------------------------------------------------------------

  def showResults(vrn: Vrn): Action[AnyContent] = Action.async {
    implicit request: Request[_] =>
      af.authorised() {

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

  private def authorisationException(e: AuthorisationException)(
      implicit
      request: Request[_]): Future[Result] =
    {
      Logger.debug(s"Unauthorised because of ${
        e.reason
      }, $e")
      Future.successful(Unauthorized(
        errorHandler.standardErrorTemplate(
          ErrorMessages.`You do not have access to this service`.show,
          ErrorMessages.`You do not have access to this service`.show,
          "")))

    }

  private def computeView(
      allRepaymentData: AllRepaymentData,
      customerData:     Option[CustomerInformation],
      vrn:              Vrn
  )(implicit request: Request[_]): Result = {

    val showCurrent = allRepaymentData.currentRepaymentData.isDefined
    val overDueSize = allRepaymentData.overDueRepaymentData.fold(0)(_.size)

    val bankDetailsExist = customerInformationFormatter.getBankDetailsExist(customerData)
    val bankDetails = customerInformationFormatter.getBankDetails(customerData)
    val addressDetails = customerInformationFormatter.getAddressDetails(customerData)
    val addressDetailsExist = customerInformationFormatter.getAddressDetailsExist(customerData)

    if ((showCurrent == false) && (overDueSize == 0)) {
      Ok(
        views.no_vat_repayments(
          bankDetailsExist,
          bankDetails,
          addressDetails,
          addressDetailsExist
        ))
    } else if (showCurrent && (overDueSize == 0)) {
      Ok(views.one_repayment(
        allRepaymentData.getCurrentRepaymentData,
        bankDetailsExist,
        bankDetails,
        addressDetails,
        addressDetailsExist
      ))
    } else if ((showCurrent == false) && (overDueSize == 1)) {
      Ok(views.one_repayment_delayed(
        allRepaymentData.getOverDueRepaymentData(0),
        bankDetailsExist,
        bankDetails,
        addressDetails,
        addressDetailsExist
      ))
    } else if ((showCurrent == false) && (overDueSize > 1)) {
      Ok(views.multiple_delayed(
        allRepaymentData.getOverDueRepaymentData,
        bankDetailsExist,
        bankDetails,
        addressDetails,
        addressDetailsExist
      ))
    } else if (showCurrent && (overDueSize == 1)) {
      Ok(views.one_repayment_one_dealyed(
        allRepaymentData.getCurrentRepaymentData,
        allRepaymentData.getOverDueRepaymentData(0),
        bankDetailsExist,
        bankDetails,
        addressDetails,
        addressDetailsExist
      ))
    } else if (showCurrent && (overDueSize > 1)) {
      Ok(views.one_repayment_multiple_delayed(
        allRepaymentData.getCurrentRepaymentData,
        allRepaymentData.getOverDueRepaymentData,
        bankDetailsExist,
        bankDetails,
        addressDetails,
        addressDetailsExist
      ))
    } else throw new RuntimeException(s"""View not configured for overDueSize: ${overDueSize}, showCurrent: ${showCurrent}""")

  }

}
