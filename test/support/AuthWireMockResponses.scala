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

package support

import com.github.tomakehurst.wiremock.client.WireMock._
import com.github.tomakehurst.wiremock.http.{HttpHeader, HttpHeaders}
import com.github.tomakehurst.wiremock.stubbing.StubMapping
import model.Vrn

object AuthWireMockResponses {

  val expectedDetail = "SessionRecordNotFound"
  val oid: String = "556737e15500005500eaf68f"

  val headers: HttpHeaders = new HttpHeaders(
    new HttpHeader("WWW-Authenticate", s"""MDTP detail="$expectedDetail"""")
  // new HttpHeader("Failing-Enrolment", "SA")
  )

  def authLoginStubOk: StubMapping = {
    stubFor(get(urlMatching("/auth-login-stub/gg-sign-in/*"))
      .willReturn(aResponse()
        .withStatus(200)))
  }

  def authFailed: StubMapping = {
    stubFor(post(urlEqualTo("/auth/authorise"))
      .willReturn(aResponse()
        .withStatus(401)
        .withHeaders(headers)))
  }

  def authOkNoEnrolments(affinityGroup: String = "Individual", wireMockBaseUrlAsString: String): StubMapping = {
    stubFor(post(urlEqualTo("/auth/authorise"))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(
          s"""
             {
               "new-session":"/auth/oid/$oid/session",
               "enrolments":"/auth/oid/$oid/enrolments",
               "uri":"/auth/oid/$oid",
               "loggedInAt":"2016-06-20T10:44:29.634Z",
               "optionalCredentials":{
                 "providerId": "12345",
                 "providerType": "GovernmentGateway"
               },
               "accounts":{
               },
               "lastUpdated":"2016-06-20T10:44:29.634Z",
               "credentialStrength":"strong",
               "confidenceLevel":50,
               "userDetailsLink":"$wireMockBaseUrlAsString/user-details/id/$oid",
               "levelOfAssurance":"1",
               "previouslyLoggedInAt":"2016-06-20T09:48:37.112Z",
               "groupIdentifier": "groupId",
               "affinityGroup": "$affinityGroup",
               "allEnrolments": []
             }
       """.stripMargin)))

  }

  def authOkWithEnrolments(affinityGroup: String = "Individual", wireMockBaseUrlAsString: String, vrn: Vrn, enrolment: String): StubMapping = {
    stubFor(post(urlEqualTo("/auth/authorise"))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(
          s"""
             {
               "new-session":"/auth/oid/$oid/session",
               "enrolments":"/auth/oid/$oid/enrolments",
               "uri":"/auth/oid/$oid",
               "loggedInAt":"2016-06-20T10:44:29.634Z",
               "optionalCredentials":{
                 "providerId": "12345",
                 "providerType": "GovernmentGateway"
               },
               "accounts":{
               },
               "lastUpdated":"2016-06-20T10:44:29.634Z",
               "credentialStrength":"strong",
               "confidenceLevel":50,
               "userDetailsLink":"$wireMockBaseUrlAsString/user-details/id/$oid",
               "levelOfAssurance":"1",
               "previouslyLoggedInAt":"2016-06-20T09:48:37.112Z",
               "groupIdentifier": "groupId",
               "affinityGroup": "$affinityGroup",
               "allEnrolments": [
                        {
                          "key": "${enrolment}",
                          "identifiers": [
                            {
                              "key": "VRN",
                              "value": "${vrn.value}"
                            }
                          ],
                          "state": "Activated"
                        }
                      ]
             }
       """.stripMargin)))

  }

}
