package connectors

import model.Vrn
import support.{DDBackendWireMockResponses, ItSpec}

class DirectDebitBackendConnectorSpec extends ItSpec {
  val vrn: Vrn = Vrn("2345678890")

  val directDebitBackendConnector: DirectDebitBackendConnector = injector.instanceOf[DirectDebitBackendConnector]

  "Start Journey" in {

    DDBackendWireMockResponses.ddOk
    val result = directDebitBackendConnector.startJourney(vrn).futureValue
    result.nextUrl shouldBe "https://www.qa.tax.service.gov.uk/direct-debit/enter-bank-details/11f4e440-1db1-4138-9a66-cb73db12174e"
  }

}
