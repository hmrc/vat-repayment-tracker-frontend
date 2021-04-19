/*
 * Copyright 2021 HM Revenue & Customs
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
import play.api.mvc.Request
import req.RequestSupport
import uk.gov.hmrc.play.bootstrap.config.{RunMode, ServicesConfig}

case class ViewConfig(
    appName:                     String,
    assetsPrefix:                String,
    authUrl:                     String,
    frontendBaseUrl:             String,
    loginUrl:                    String,
    viewVatAccount:              String,
    updateCorrespondenceAddress: String,
    feedbackBaseUrl:             String,
    contactBaseUrl:              String,
    paymentHistoryUrl:           String,
    btaUrl:                      String,
    signupUrl:                   String,
    variationsUrlPrefix:         String,
    isShuttered:                 Boolean,
    timeoutDialogTimeout:        Int,
    timeoutDialogCountdown:      Int) {

  val reportAProblemPartialUrl = s"$contactBaseUrl/contact/problem_reports_ajax?service=$appName"
  val reportAProblemNonJSUrl = s"$contactBaseUrl/contact/problem_reports_nonjs?service=$appName"
  val reportAccessibilityProblemUrl = s"$frontendBaseUrl/contact/accessibility?service=$appName"
  val feedbackUrlForLogout = s"$feedbackBaseUrl/feedback/$appName"
  val feedbackUrl = s"$contactBaseUrl/contact/beta-feedback?service=$appName"
  val supportLanguages: Boolean = true
  val signOut = s"$frontendBaseUrl/vat-repayment-tracker/signout"
  val showResultsUrl = s"$frontendBaseUrl/vat-repayment-tracker/show-vrt"
  val nonMtdUser = s"$frontendBaseUrl/vat-repayment-tracker/non-mtd-user"
  def vatVariationsUrl(vrn: Vrn) = s"${variationsUrlPrefix}/vat-variations/org/${vrn.value}/introduction"

  @Inject
  def this(servicesConfig: ServicesConfig, runMode: RunMode) = this(
    appName                     = servicesConfig.getString("appName"),
    assetsPrefix                = servicesConfig.getString(s"assets.url") + servicesConfig.getString(s"assets.version"),
    authUrl                     = servicesConfig.baseUrl("auth"),
    frontendBaseUrl             = servicesConfig.getString("urls.frontend-base"),
    loginUrl                    = servicesConfig.getString("urls.login"),
    viewVatAccount              = servicesConfig.getString("urls.view-vat-account"),
    updateCorrespondenceAddress = servicesConfig.getString("urls.update-correspondence-address"),
    feedbackBaseUrl             = servicesConfig.getString("urls.feedback-base"),
    contactBaseUrl              = servicesConfig.getString("urls.contact-frontend"),
    paymentHistoryUrl           = servicesConfig.getString("urls.payments-history"),
    btaUrl                      = servicesConfig.getString("urls.bta"),
    signupUrl                   = servicesConfig.getString("urls.signup"),
    variationsUrlPrefix         = servicesConfig.getString("urls.variationsUrlPrefix"),
    isShuttered                 = servicesConfig.getBoolean("is-shuttered"),
    timeoutDialogTimeout        = servicesConfig.getInt("timeout-dialog.timeout"),
    timeoutDialogCountdown      = servicesConfig.getInt("timeout-dialog.countdown")
  )

}
