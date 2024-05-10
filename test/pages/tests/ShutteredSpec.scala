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

package pages.tests

import model.{EnrolmentKeys, Vrn}
import pages.{ManageOrTrack, Shuttered}
import support.{AuditWireMockResponses, AuthWireMockResponses, BrowserSpec, VatRepaymentTrackerBackendWireMockResponses}

class ShutteredSpec extends BrowserSpec {

  val vrn: Vrn = Vrn("234567890")

  override def configMap: Map[String, Any] =
    super
      .configMap
      .updated("is-shuttered", "true")

  "Check shuttered page is shown show-vrt" in {
    val path = s"""/vat-repayment-tracker/show-vrt"""
    AuditWireMockResponses.auditIsAvailable
    goToViaPath(path)
    Shuttered.uniqueToPage
  }
  "Check shuttered page is shown non-mtd-user " in {
    val path = s"""/vat-repayment-tracker/non-mtd-user"""
    AuditWireMockResponses.auditIsAvailable
    goToViaPath(path)
    Shuttered.uniqueToPage
  }
  "Check shuttered page is shown view-repayment-account  " in {
    val path = s"""/vat-repayment-tracker/view-repayment-account"""
    AuditWireMockResponses.auditIsAvailable
    goToViaPath(path)
    Shuttered.uniqueToPage
  }
  "Check shuttered page is shown view-progress " in {
    val path = s"""/vat-repayment-tracker/view-progress/12ac"""
    AuditWireMockResponses.auditIsAvailable
    goToViaPath(path)
    Shuttered.uniqueToPage
  }
  "Check shuttered page is shown manage-or-track-vrt " in {
    val path = s"""/vat-repayment-tracker/manage-or-track-vrt"""
    AuditWireMockResponses.auditIsAvailable
    VatRepaymentTrackerBackendWireMockResponses.storeOk()
    AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
    login()
    goToViaPath(path)
    ManageOrTrack.assertPageIsDisplayed(noddDisplayed   = true, nobankDisplayed = true, isShuttered = true)
  }

}
