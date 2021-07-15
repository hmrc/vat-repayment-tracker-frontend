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

package views

import javax.inject.{Inject, Singleton}
import uk.gov.hmrc.play.views.html.helpers._
import uk.gov.hmrc.play.views.html.layouts._

@Singleton
class ViewsHelpers @Inject() (

    val languageSelection: _root_.views.html.partials.language_selection,

    //copied from uk.gov.hmrc.play.views.html.helpers
    val errorSummary:       ErrorSummary,
    val form:               FormWithCSRF,
    val reportAProblemLink: ReportAProblemLink,

    //copied from uk.gov.hmrc.play.views.html.layouts
    val betaBanner:              BetaBanner,
    val footer:                  Footer,
    val footerLinks:             FooterLinks,
    val headWithTrackingConsent: HeadWithTrackingConsent,
    val headerNav:               HeaderNav,
    val mainContent:             MainContent,
    val mainContentHeader:       MainContentHeader,
    val serviceInfo:             ServiceInfo,
    val sidebar:                 Sidebar
)
