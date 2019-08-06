package support

import com.github.tomakehurst.wiremock.client.WireMock.{aResponse, post, stubFor, urlEqualTo}
import com.github.tomakehurst.wiremock.http.{HttpHeader, HttpHeaders}
import com.github.tomakehurst.wiremock.stubbing.StubMapping

object WireMockResponses {

  val expectedDetail = "SessionRecordNotFound"
  val oid: String = "556737e15500005500eaf68f"

  val headers: HttpHeaders = new HttpHeaders(
    new HttpHeader("WWW-Authenticate", s"""MDTP detail="$expectedDetail"""")
    // new HttpHeader("Failing-Enrolment", "SA")
  )

  def authFailed: StubMapping = {
    stubFor(post(urlEqualTo("/auth/authorise"))
      .willReturn(aResponse()
        .withStatus(401)
        .withHeaders(headers)))
  }

  def authOk(affinityGroup: String = "SA", wireMockBaseUrlAsString: String): StubMapping = {
    stubFor(post(urlEqualTo("/auth/authorise"))
      .willReturn(aResponse()
        .withStatus(200)
        .withBody(
          s"""
             |{
             |  "new-session":"/auth/oid/$oid/session",
             |  "enrolments":"/auth/oid/$oid/enrolments",
             |  "uri":"/auth/oid/$oid",
             |  "loggedInAt":"2016-06-20T10:44:29.634Z",
             |  "optionalCredentials":{
             |    "providerId": "12345",
             |    "providerType": "GovernmentGateway"
             |  },
             |  "accounts":{
             |  },
             |  "lastUpdated":"2016-06-20T10:44:29.634Z",
             |  "credentialStrength":"strong",
             |  "confidenceLevel":50,
             |  "userDetailsLink":"$wireMockBaseUrlAsString/user-details/id/$oid",
             |  "levelOfAssurance":"1",
             |  "previouslyLoggedInAt":"2016-06-20T09:48:37.112Z",
             |  "groupIdentifier": "groupId",
             |  "affinityGroup": "$affinityGroup",
             |  "allEnrolments": []
             |}
       """.stripMargin)))

  }

}
