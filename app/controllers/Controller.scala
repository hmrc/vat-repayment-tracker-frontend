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

package controllers

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

import cats.data.NonEmptyList
import config.ViewConfig
import connectors.Auditor.`repayment-type`
import connectors._
import controllers.action.{Actions, AuthenticatedRequest}
import formaters.{DesFormatter, ShowResultsFormatter, ViewProgressFormatter}
import javax.inject.{Inject, Singleton}
import model._
import model.des.CustomerInformation
import play.api.Logger
import play.api.mvc.{Action, _}
import req.RequestSupport
import scala.util.Try
import service.VrtService
import util.WelshDateUtil.StringOps

import scala.concurrent.ExecutionContext

@Singleton
class Controller @Inject() (
    cc:                                  ControllerComponents,
    views_non_mtd_user:                  views.html.non_mtd_user,
    view_repayment_account:              views.html.view_repayment_account,
    vrt_vat_registration_cancelled:      views.html.vrt_vat_registration_cancelled,
    paymentsOrchestratorConnector:       PaymentsOrchestratorConnector,
    requestSupport:                      RequestSupport,
    desFormatter:                        DesFormatter,
    actions:                             Actions,
    auditor:                             Auditor,
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

  private val logger = Logger(this.getClass)

  val nonMtdUser: Action[AnyContent] = actions.loggedIn.async { implicit request =>
    for {
      _ <- auditor.auditEngagement("nonMtdUser", `repayment-type`.none_in_progress, request.typedVrn.map(_.vrn))
    } yield Ok(views_non_mtd_user())
  }

  val signout: Action[AnyContent] = Action.async { _ =>
    Redirect(viewConfig.feedbackUrlForLogout)
  }

  val showVrt: Action[AnyContent] = actions.securedAction.async {
    implicit request: AuthenticatedRequest[_] =>

      logger.debug(s"IsPartialMigration set to ${request.isPartialMigration}")
      request.typedVrn match {

        case TypedVrn.ClassicVrn(vrnNonMtd) =>
          logger.debug("Received a classic VRN")
          val calendarDataF = vatConnector.calendar(vrnNonMtd)
          val designatoryDetailsF = vatConnector.designatoryDetails(vrnNonMtd)
          for {
            calendarData <- calendarDataF
            designatoryDetails <- designatoryDetailsF
            engmtType = showResultsFormatter.computeEngmtClassic(calendarData)
            _ <- auditor.auditEngagement("showVrt", engmtType, Some(request.typedVrn.vrn))
          } yield showResultsFormatter.computeViewClassic(vrnNonMtd, calendarData, designatoryDetails)

        case TypedVrn.MtdVrn(vrnMtd) =>
          logger.debug("Received a  MTD VRN")
          val customerDataF = paymentsOrchestratorConnector.getCustomerData(vrnMtd)
          val repaymentDetailsF = paymentsOrchestratorConnector.getRepaymentsDetails(vrnMtd)
          val financialDataF = paymentsOrchestratorConnector.getFinancialData(vrnMtd)
          for {
            customerData <- customerDataF
            repaymentDetails <- repaymentDetailsF
            financialData <- financialDataF

            allRepaymentData = vrtService.getAllRepaymentData(repaymentDetails, vrnMtd, financialData)
            engmtType = showResultsFormatter.computeEngmt(allRepaymentData)
            _ <- auditor.auditEngagement("showVrt", engmtType, Some(request.typedVrn.vrn))
          } yield showResultsFormatter.computeView(allRepaymentData, customerData, vrnMtd)
      }

  }

  def viewProgress(periodKey: PeriodKey): Action[AnyContent] =
    actions.securedActionMtdVrnCheck.async { implicit request: AuthenticatedRequest[_] =>

      val customerDataF = paymentsOrchestratorConnector.getCustomerData(request.typedVrn.vrn)
      val financialDataF = paymentsOrchestratorConnector.getFinancialData(request.typedVrn.vrn)
      logger.debug(s"""received vrn : ${request.typedVrn.vrn.value}, periodKey: ${periodKey.value}""")
      for {
        customerData <- customerDataF
        financialData <- financialDataF
        vrd <- vatRepaymentTrackerBackendConnector.find(request.typedVrn.vrn, periodKey)
      } yield {
        vrd match {
          case Nil =>
            logger.info("No VAT repayment data found from backend: redirecting to manage or track page")
            Redirect(routes.ManageOrTrackController.manageOrTrackVrt)

          case head :: tail =>
            viewProgressFormatter.computeViewProgress(periodKey, NonEmptyList(head, tail), customerData, financialData)
        }
      }
    }

  val viewRepaymentAccount: Action[AnyContent] = actions.securedActionMtdVrnCheck.async {
    implicit request: AuthenticatedRequest[_] =>

      val customerDataF = paymentsOrchestratorConnector.getCustomerData(request.typedVrn.vrn)

      for {
        customerData <- customerDataF
      } yield {
        val inFlight = desFormatter.bankDetailsInFlight(customerData)
        val bankDetails = desFormatter.getBankDetails(customerData)
        if (inFlight) {
          val dateToDisplay = getDateToDisplay(customerData)
          val welshDateToDisplay: String = dateToDisplay.welshMonth
          Ok(view_repayment_account(bankDetails, inFlight, ReturnPage("view-repayment-account"), Some(dateToDisplay), Some(welshDateToDisplay)))
        } else {
          Ok(view_repayment_account(bankDetails, inFlight, ReturnPage("view-repayment-account"), None, None))
        }
      }
  }

  private def getDateToDisplay(customerData: Option[CustomerInformation]): String = desFormatter.getInFlightDate(customerData) match {
    case Some(dateStr) =>
      Try(LocalDate.parse(dateStr).plusDays(40))
        .map(_.format(DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.UK)))
        .getOrElse {
          throw new RuntimeException(
            s"[Controller][viewRepaymentAccount] Failed to parse or format receivedDate [$dateStr] for in-flight changes."
          )
        }

    case None =>
      throw new RuntimeException(
        "[Controller][viewRepaymentAccount] No receivedDate for in-flight changes found for customer."
      )
  }

  val deregistered: Action[AnyContent] = actions.loggedIn.async { implicit request =>
    Ok(vrt_vat_registration_cancelled())
  }

}
