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

import model.dd.CreateVATJourneyRequest
import model.{EnrolmentKeys, ReturnPage}
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import support.{AuditWireMockResponses, AuthWireMockResponses, DDBackendWireMockResponses, DeregisteredBehaviour, ItSpec, PaymentsOrchestratorStub, VatRepaymentTrackerBackendWireMockResponses}
import play.api.http.Status
import play.api.mvc.{AnyContentAsFormUrlEncoded, Result}
import play.api.test.Helpers._
import play.api.test.Helpers.status

import scala.concurrent.Future
import scala.jdk.CollectionConverters.IteratorHasAsScala

class ManageOrTrackControllerSpec extends ItSpec with DeregisteredBehaviour {
  import support.VatData.vrn

  val controller: ManageOrTrackController = injector.instanceOf[ManageOrTrackController]

  val serviceBaseUrl = s"${configMap("urls.frontend-base")}/vat-repayment-tracker"
  val nonMtdUserPageUrl = "/vat-repayment-tracker/non-mtd-user"

  def testRadioButtonOptions(
      doc:                         Document,
      expectedRadioLabelsAndHints: List[(String, Option[String])]
  ): Unit = {
    val radios = doc.select(".govuk-radios__item").iterator().asScala.toList
    val labelsAndHints = radios.map(r =>
      r.select(".govuk-label").text() ->
        Option(r.select(".govuk-hint").text()).filter(_.nonEmpty)
    )

    labelsAndHints shouldBe expectedRadioLabelsAndHints
    ()
  }

  "GET /vat-repayment-tracker/manage-or-track-vrt" - {

    "authorised gets 'Manage or track VRT' when" - {

      "there are repayment bank details and dd details" in {
        AuditWireMockResponses.auditIsAvailable
        AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
        PaymentsOrchestratorStub.customerDataOkWithBankDetails(vrn)
        PaymentsOrchestratorStub.ddOk(vrn)
        VatRepaymentTrackerBackendWireMockResponses.storeOk()

        val result = controller.manageOrTrackVrt(fakeRequest)
        status(result) shouldBe Status.OK

        val doc = Jsoup.parse(contentAsString(result))

        testRadioButtonOptions(
          doc,
          List(
            "Manage your Direct Debit" -> Some("Account: ****2490, Sort code: 40 ** **"),
            "Manage your repayment bank account" -> Some("Account: ****2222, Sort code: 66 ** **"),
            "Track your VAT repayments" -> Some("View what HMRC owe you")
          )
        )
      }

      "there are no dd details" in {
        AuditWireMockResponses.auditIsAvailable
        AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
        PaymentsOrchestratorStub.customerDataOkWithBankDetails(vrn)
        PaymentsOrchestratorStub.ddNotFound(vrn)
        VatRepaymentTrackerBackendWireMockResponses.storeOk()

        val result = controller.manageOrTrackVrt(fakeRequest)
        status(result) shouldBe Status.OK

        val doc = Jsoup.parse(contentAsString(result))

        testRadioButtonOptions(
          doc,
          List(
            "Set up a Direct Debit" -> Some("HMRC will automatically collect your VAT Return payments when due"),
            "Manage your repayment bank account" -> Some("Account: ****2222, Sort code: 66 ** **"),
            "Track your VAT repayments" -> Some("View what HMRC owe you")
          )
        )
      }

      "there are no repayment bank details and there are no repayment bank details in-flight" in {
        AuditWireMockResponses.auditIsAvailable
        AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
        PaymentsOrchestratorStub.customerDataOkWithoutBankDetails(vrn)
        PaymentsOrchestratorStub.ddOk(vrn)
        VatRepaymentTrackerBackendWireMockResponses.storeOk()

        val result = controller.manageOrTrackVrt(fakeRequest)
        status(result) shouldBe Status.OK

        val doc = Jsoup.parse(contentAsString(result))

        testRadioButtonOptions(
          doc,
          List(
            "Manage your Direct Debit" -> Some("Account: ****2490, Sort code: 40 ** **"),
            "Set up a repayment bank account" -> Some("Tell HMRC where to pay your money"),
            "Track your VAT repayments" -> Some("View what HMRC owe you")
          )
        )
      }

      "there are no repayment bank details but there are repayment bank details in-flight" in {
        AuditWireMockResponses.auditIsAvailable
        AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)
        PaymentsOrchestratorStub.customerDataOkWithoutBankDetailsInflight(vrn)
        PaymentsOrchestratorStub.ddOk(vrn)
        VatRepaymentTrackerBackendWireMockResponses.storeOk()

        val result = controller.manageOrTrackVrt(fakeRequest)
        status(result) shouldBe Status.OK

        val doc = Jsoup.parse(contentAsString(result))

        testRadioButtonOptions(
          doc,
          List(
            "Manage your Direct Debit" -> Some("Account: ****2490, Sort code: 40 ** **"),
            "Track your VAT repayments" -> Some("View what HMRC owe you")
          )
        )
      }

    }

    "deregistered redirected to 'vrt vat registration cancelled' page" in {
      assertDeregisteredRedirectedIn(controller.manageOrTrackVrt, vrn)
    }

  }

  "POST /vat-repayment-tracker/manager-or-track-vrt" - {

      def performAction(formData: (String, String)*): Future[Result] = {
        AuditWireMockResponses.auditIsAvailable
        AuthWireMockResponses.authOkWithEnrolments(wireMockBaseUrlAsString = wireMockBaseUrlAsString, vrn = vrn, enrolment = EnrolmentKeys.mtdVatEnrolmentKey)

        controller.manageOrTrackSubmit(fakeRequest.withMethod(POST).withBody(AnyContentAsFormUrlEncoded(formData.toMap.map{ case (k, v) => k -> Seq(v) })))
      }

    "should show a form error if nothing has been submitted" in {
      val result = performAction()
      val doc = Jsoup.parse(contentAsString(result))

      val expectedErrorMessage = "Select whether to manage your accounts or track a VAT repayment"
      doc.select(".govuk-error-summary").select("a").text() shouldBe expectedErrorMessage
      doc.select(".govuk-error-message").text() shouldBe s"Error:$expectedErrorMessage"
    }

    "should redirect to the showVrt page if the user selects 'manage VRT'" in {
      val result = performAction("manage" -> "vrt")
      status(result) shouldBe SEE_OTHER
      redirectLocation(result) shouldBe Some(routes.Controller.showVrt.url)
    }

    "should redirect to the viewRepaymentAccount endpoint page if the user selects 'manage repayment account'" in {
      val result = performAction("manage" -> "bank")
      status(result) shouldBe SEE_OTHER
      redirectLocation(result) shouldBe Some(routes.Controller.viewRepaymentAccount.url)
    }

    "should redirect to the startBankCocJourney endpoint if the user selects 'set up a repayment account'" in {
      val expectedRedirectUrl =
        routes.BankAccountCocController.startBankAccountCocJourney(ReturnPage("manage-or-track-vrt")).url

      val result = performAction("manage" -> "nobank")
      status(result) shouldBe SEE_OTHER
      redirectLocation(result) shouldBe Some(expectedRedirectUrl)
    }

    "should redirect to the start a direct debit journey endpoint if the user selects 'set up a DD'" in {
      DDBackendWireMockResponses.ddOk

      val result = performAction("manage" -> "nodd")
      status(result) shouldBe SEE_OTHER
      redirectLocation(result) shouldBe Some(DDBackendWireMockResponses.nextUrl)

      DDBackendWireMockResponses.verifyStartJourneyCalled(
        CreateVATJourneyRequest(
          vrn.value,
          "VRN",
          "https://www.tax.service.gov.uk/vat-through-software/vat-overview",
          "http://localhost:9863/vat-repayment-tracker/manage-or-track-vrt"
        )
      )
    }

    "should redirect to the start a direct debit journey endpoint if the user selects 'manage up a DD'" in {
      DDBackendWireMockResponses.ddOk

      val result = performAction("manage" -> "dd")
      status(result) shouldBe SEE_OTHER
      redirectLocation(result) shouldBe Some(DDBackendWireMockResponses.nextUrl)

      DDBackendWireMockResponses.verifyStartJourneyCalled(
        CreateVATJourneyRequest(
          vrn.value,
          "VRN",
          "https://www.tax.service.gov.uk/vat-through-software/vat-overview",
          "http://localhost:9863/vat-repayment-tracker/manage-or-track-vrt"
        )
      )
    }

    "should return an error if the submitted option is not recognised" in {
      val result = performAction("manage" -> "???")

      an[IllegalArgumentException] shouldBe thrownBy(await(result))
    }

  }

}
