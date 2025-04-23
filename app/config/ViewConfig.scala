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

package config

import com.google.inject.Inject
import model.Vrn
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

case class ViewConfig(
    appName:                       String,
    authUrl:                       String,
    frontendBaseUrl:               String,
    loginUrl:                      String,
    viewVatAccount:                String,
    updateCorrespondenceAddress:   String,
    feedbackBaseUrl:               String,
    contactBaseUrl:                String,
    paymentHistoryUrl:             String,
    btaUrl:                        String,
    signupUrl:                     String,
    variationsUrlPrefix:           String,
    accessibilityStatementBaseUrl: String,
    accessibilityStatementPath:    String,
    isShuttered:                   Boolean,
    timeoutDialogTimeout:          Int,
    timeoutDialogCountdown:        Int,
    webchatUrl:                    String,
    basGatewayBaseUrl:             String
) {

  val feedbackUrlForLogout = s"$feedbackBaseUrl/feedback/$appName"
  val feedbackUrl = s"$contactBaseUrl/contact/beta-feedback?service=$appName"
  val showResultsUrl = s"$frontendBaseUrl/vat-repayment-tracker/show-vrt"

  def vatVariationsUrl(vrn: Vrn) = s"${variationsUrlPrefix}/vat-variations/org/${vrn.value}/introduction"

  def signOutWithRedirect(continueUrl: String): String =
    basGatewayBaseUrl + s"/bas-gateway/sign-out-without-state?continue=$frontendBaseUrl$continueUrl"

  @Inject
  def this(servicesConfig: ServicesConfig) = this(
    appName                       = servicesConfig.getString("appName"),
    authUrl                       = servicesConfig.baseUrl("auth"),
    frontendBaseUrl               = servicesConfig.getString("urls.frontend-base"),
    loginUrl                      = servicesConfig.getString("urls.login"),
    viewVatAccount                = servicesConfig.getString("urls.view-vat-account"),
    updateCorrespondenceAddress   = servicesConfig.getString("urls.update-correspondence-address"),
    feedbackBaseUrl               = servicesConfig.getString("urls.feedback-base"),
    contactBaseUrl                = servicesConfig.getString("urls.contact-frontend"),
    paymentHistoryUrl             = servicesConfig.getString("urls.payments-history"),
    btaUrl                        = servicesConfig.getString("urls.bta"),
    signupUrl                     = servicesConfig.getString("urls.signup"),
    variationsUrlPrefix           = servicesConfig.getString("urls.variationsUrlPrefix"),
    isShuttered                   = servicesConfig.getBoolean("is-shuttered"),
    timeoutDialogTimeout          = servicesConfig.getInt("timeout-dialog.timeout"),
    timeoutDialogCountdown        = servicesConfig.getInt("timeout-dialog.countdown"),
    accessibilityStatementBaseUrl = servicesConfig.getString("accessibility-statement-frontend.url"),
    accessibilityStatementPath    = servicesConfig.getString("accessibility-statement-frontend.path"),
    webchatUrl                    = servicesConfig.getString("urls.webchatUrl"),
    basGatewayBaseUrl             = servicesConfig.baseUrl("bas-gateway-frontend")

  )

}
