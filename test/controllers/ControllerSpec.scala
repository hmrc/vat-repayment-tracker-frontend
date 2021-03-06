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

package controllers

import java.time.LocalDate
import model.des.RiskingStatus.INITIAL
import model.{EnrolmentKeys, PeriodKey, Vrn}
import play.api.http.Status
import support._

class ControllerSpec extends ItSpec {
  val vrn: Vrn = Vrn("2345678890")
  val periodKey: PeriodKey = PeriodKey("18AG")

  "1. Get ShowResults authorised" in {
    AuditWireMockResponses.auditIsAvailable
    AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
    PaymentsOrchestratorStub.customerDataOkWithBankDetails(vrn)
    PaymentsOrchestratorStub.repaymentDetailS1(vrn, LocalDate.now().toString, INITIAL, periodKey)
    PaymentsOrchestratorStub.financialsOkCredit(vrn)
    VatRepaymentTrackerBackendWireMockResponses.storeOk()
    val result = connector.showResults(vrn).futureValue
    result.status shouldBe Status.OK
  }

  "2. Get ShowResults not authorised redirect to login stub" in {
    AuthWireMockResponses.authFailed
    AuditWireMockResponses.auditIsAvailable
    GgStub.signInPage(19001, vrn)
    val result = connector.showResults(vrn).futureValue
    result.body should include ("Sign in using Government Gateway")
    result.status shouldBe Status.OK
  }

  "3. Get view repayment account authorised" in {
    AuditWireMockResponses.auditIsAvailable
    AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
    PaymentsOrchestratorStub.customerDataOkWithBankDetails(vrn)
    val result = connector.viewRepaymentAccount.futureValue
    result.status shouldBe Status.OK
  }

  "4. Get startBankAccountCocJourney authorised" in {
    AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
    BankAccountCocWireMockResponses.bankOk
    PaymentsOrchestratorStub.customerDataOkWithBankDetails(vrn)
    AuditWireMockResponses.auditIsAvailable
    val result = connector.startBankAccountCocJourney.futureValue
    result.status shouldBe Status.OK
  }

  "5. Get showVrt authorised" in {
    AuditWireMockResponses.auditIsAvailable
    AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
    PaymentsOrchestratorStub.customerDataOkWithBankDetails(vrn)
    PaymentsOrchestratorStub.repaymentDetailS1(vrn, LocalDate.now().toString, INITIAL, periodKey)
    VatRepaymentTrackerBackendWireMockResponses.storeOk()
    PaymentsOrchestratorStub.financialsOkCredit(vrn)
    val result = connector.showVrt.futureValue
    result.status shouldBe Status.OK
  }

  //Update to match classic changes later
  "6. Get showVrt authorised no-mtd, no vat repayments" in {
    AuditWireMockResponses.auditIsAvailable
    AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.vatVarEnrolmentKey)
    PaymentsOrchestratorStub.customerDataOkWithBankDetails(vrn)
    PaymentsOrchestratorStub.repaymentDetailS1(vrn, LocalDate.now().toString, INITIAL, periodKey)
    VatRepaymentTrackerBackendWireMockResponses.storeOk()
    VatWireMockResponses.calendar404(vrn)
    VatWireMockResponses.designatoryDetailsOk(vrn)
    val result = connector.showVrt.futureValue
    result.body should include("No VAT repayments in progress")
  }

  "7. Get manageOrTrackVRT authorised" in {
    AuditWireMockResponses.auditIsAvailable
    AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
    PaymentsOrchestratorStub.customerDataOkWithBankDetails(vrn)
    PaymentsOrchestratorStub.repaymentDetailS1(vrn, LocalDate.now().toString, INITIAL, periodKey)
    VatRepaymentTrackerBackendWireMockResponses.storeOk()
    PaymentsOrchestratorStub.ddOk(vrn)
    val result = connector.manageOrTrackVrt.futureValue
    result.status shouldBe Status.OK
  }

  "8. Get showVrt manageOrTrackVRT  no-mtd" in {
    AuditWireMockResponses.auditIsAvailable
    AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.vatVarEnrolmentKey)
    PaymentsOrchestratorStub.customerDataOkWithBankDetails(vrn)
    PaymentsOrchestratorStub.repaymentDetailS1(vrn, LocalDate.now().toString, INITIAL, periodKey)
    VatRepaymentTrackerBackendWireMockResponses.storeOk()
    val result = connector.manageOrTrackVrt.futureValue
    result.body should include ("You cannot access this service")
    result.status shouldBe Status.OK
  }

  "9. Get manageOrTrack authorised" in {
    AuditWireMockResponses.auditIsAvailable
    AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
    PaymentsOrchestratorStub.customerDataOkWithBankDetails(vrn)
    PaymentsOrchestratorStub.repaymentDetailS1(vrn, LocalDate.now().toString, INITIAL, periodKey)
    VatRepaymentTrackerBackendWireMockResponses.storeOk()
    val result = connector.manageOrTrack(vrn).futureValue
    result.status shouldBe Status.OK
  }

  "10. Get showVrt manageOrTrack no-mtd" in {
    AuditWireMockResponses.auditIsAvailable
    AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.vatVarEnrolmentKey)
    PaymentsOrchestratorStub.customerDataOkWithBankDetails(vrn)
    PaymentsOrchestratorStub.repaymentDetailS1(vrn, LocalDate.now().toString, INITIAL, periodKey)
    VatRepaymentTrackerBackendWireMockResponses.storeOk()
    PaymentsOrchestratorStub.ddOk(vrn)
    val result = connector.manageOrTrack(vrn).futureValue
    result.body should include ("You cannot access this service")
    result.status shouldBe Status.OK
  }

}
