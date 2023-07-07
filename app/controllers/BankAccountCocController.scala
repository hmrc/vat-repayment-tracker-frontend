/*
 * Copyright 2023 HM Revenue & Customs
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

import connectors._
import controllers.action.{Actions, AuthenticatedRequest}

import javax.inject.{Inject, Singleton}
import model._
import play.api.Logger
import play.api.mvc.{Action, _}
import service.VrtService
import req.RequestSupport

import scala.concurrent.ExecutionContext

@Singleton
class BankAccountCocController @Inject() (
    cc:                            ControllerComponents,
    actions:                       Actions,
    bankAccountCocConnector:       BankAccountCocConnector,
    paymentsOrchestratorConnector: PaymentsOrchestratorConnector,
    auditor:                       Auditor,
    vrtService:                    VrtService,
    requestSupport:                RequestSupport,
)(
    implicit
    ec: ExecutionContext)

  extends FrontendBaseController(cc) {

  private val logger = Logger(this.getClass)

  def startBankAccountCocJourney(returnPage: ReturnPage, audit: Boolean): Action[AnyContent] =
    actions.securedActionMtdVrnCheckWithoutShutterCheck.async { implicit  request: AuthenticatedRequest[_] =>
      import requestSupport._
      logger.info(s"request: $request")
      logger.info(s"returnPage: ${returnPage.toString}")
      logger.info(s"audi: ${audit.toString}")
      logger.info(s"request.typedVrn.vrn - ${request.typedVrn.vrn}")

      if (audit) {
        logger.debug("startBankAccountCocJourney... trying to audit")
        val repaymentDetailsF = paymentsOrchestratorConnector.getRepaymentsDetails(request.typedVrn.vrn)
        val financialDataF = paymentsOrchestratorConnector.getFinancialData(request.typedVrn.vrn)

        for {
          repaymentDetails <- repaymentDetailsF
          financialData <- financialDataF
          allRepayments = vrtService.getAllRepaymentData(repaymentDetails, request.typedVrn.vrn, financialData)
          _ <- auditor.audit(allRepayments.inProgressRepaymentData, "initiateChangeVATRepaymentBankAccount", "initiate-change-vat-repayment-bank-account")
          nextUrl <- bankAccountCocConnector.startJourney(request.typedVrn.vrn, returnPage)
        } yield {
          Redirect(nextUrl.nextUrl)
        }
      } else {
        logger.debug("startBankAccountCocJourney... will not audit")
        logger.info(s"BankAccountCocController.startJourney - REACHED HERE")
        for {
          nextUrl <- bankAccountCocConnector.startJourney(request.typedVrn.vrn, returnPage)
        } yield Redirect(nextUrl.nextUrl)
      }

    }

}
