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
import model.Vrn
import model.des.{ApprovedInformation, Transaction}
import play.api.Logger
import play.api.i18n.Messages
import play.api.mvc._
import req.RequestSupport
import service.des.DesService
import uk.gov.hmrc.auth.core.{AuthorisationException, AuthorisedFunctions}
import views.views.Views

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class Controller @Inject() (cc:                ControllerComponents,
                            val authConnector: FrontendAuthConnector,
                            errorHandler:      ErrorHandler,
                            val views:         Views,
                            desConnector:      DesConnector,
                            desService:        DesService,
                            requestSupport:    RequestSupport)(
    implicit
    ec: ExecutionContext)

  extends FrontendBaseController(cc) with AuthorisedFunctions {

  import requestSupport._

  def showResults(vrn: Vrn): Action[AnyContent] = Action.async {
    implicit request =>
      authorised() {

        for {
          futureFinancialData <- desConnector.getFinancialData(vrn)
          futureCustomerData <- desConnector.getCustomerData(vrn)

          //should not happen as we are logged in.
          customerData: ApprovedInformation = futureCustomerData.getOrElse(throw new RuntimeException(
            s"""No Customer data found for VRN: ${
              vrn
            }""")).unWrap(vrn)

          result <- futureFinancialData match {

            case Some(data) => {
              val transactions = data.financialTransactions.getOrElse(Seq[Transaction]())
              transactions.size match {
                case 0 => Future.successful(Ok(views.no_vat_repayments(customerData.bankDetailsExist, customerData.bankDetails)))
                case 1 => {
                  for {
                    obligationDates <- desService.getObligations(vrn, transactions(0).periodKey)
                  } yield Ok(views.one_payment(transactions(0).originalAmount.toString(),
                                               obligationDates,
                                               transactions(0).periodKeyDescription,
                                               customerData.bankDetailsExist,
                                               customerData.bankDetails))
                }
                case _ => throw new RuntimeException("todo: implement multiple page")
              }

            }

            case None => {
              Future.successful(Ok(views.no_vat_repayments(customerData.bankDetailsExist, customerData.bankDetails)))
            }
          }
        } yield (result)

      }.recoverWith {
        case e: AuthorisationException =>
          Logger.debug(s"Unauthorised because of ${
            e.reason
          }, $e")
          Future.successful(Unauthorized(
            errorHandler.standardErrorTemplate(
              Messages("unauthorised.title"),
              Messages("unauthorised.heading"),
              "")))

      }
  }

}
