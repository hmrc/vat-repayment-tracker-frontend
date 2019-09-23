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

package config

import com.google.inject.Inject
import uk.gov.hmrc.play.bootstrap.config.{RunMode, ServicesConfig}

case class ViewConfig(
    appName:                     String,
    assetsPrefix:                String,
    analyticsToken:              String,
    analyticsHost:               String,
    authUrl:                     String,
    frontendBaseUrl:             String,
    loginUrl:                    String,
    signOut:                     String,
    viewVatAccount:              String,
    updateCorrespondenceAddress: String) {

  val reportAProblemPartialUrl = s"$frontendBaseUrl/contact/problem_reports_ajax?service=$appName"
  val reportAProblemNonJSUrl = s"$frontendBaseUrl/contact/problem_reports_nonjs?service=$appName"
  val feedbackUrl = s"$frontendBaseUrl/contact/beta-feedback-unauthenticated?service=$appName"

  val supportLanguages: Boolean = false

  @Inject
  def this(servicesConfig: ServicesConfig, runMode: RunMode) = this(
    appName                     = servicesConfig.getString("appName"),
    assetsPrefix                = servicesConfig.getString(s"assets.url") + servicesConfig.getString(s"assets.version"),
    analyticsToken              = servicesConfig.getString(s"google-analytics.token"),
    analyticsHost               = servicesConfig.getString(s"google-analytics.host"),
    authUrl                     = servicesConfig.baseUrl("auth"),
    frontendBaseUrl             = if (runMode.env == "Dev") servicesConfig.getString("frontend-base-url") else "",
    loginUrl                    = servicesConfig.getString("urls.login"),
    signOut                     = servicesConfig.getString("urls.logout"),
    viewVatAccount              = servicesConfig.getString("urls.view-vat-account"),
    updateCorrespondenceAddress = servicesConfig.getString("urls.update-correspondence-address")

  )

}
