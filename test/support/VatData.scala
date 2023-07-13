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

package support

import model.{PeriodKey, Vrn}

import java.time.LocalDate
import model.vat.{CalendarData, CalendarPeriod, VatDesignatoryDetailsAddress}
import play.api.libs.json.{JsValue, Json}

object VatData {
  val vrn: Vrn = Vrn("2345678890")
  val periodKey: PeriodKey = PeriodKey("18AG")

  val calendarPeriod: CalendarPeriod = CalendarPeriod(LocalDate.parse("2016-04-01"), LocalDate.parse("2016-06-30"), None)
  val previousPeriod1: CalendarPeriod = CalendarPeriod(LocalDate.parse("2016-01-01"), LocalDate.parse("2016-03-31"), Some(LocalDate.parse("2016-04-05")))
  val previousPeriod2: CalendarPeriod = CalendarPeriod(LocalDate.parse("2015-10-01"), LocalDate.parse("2015-12-31"), Some(LocalDate.parse("2016-01-05")))

  val calendarData: CalendarData = CalendarData(Some(calendarPeriod), Seq(previousPeriod1, previousPeriod2))

  //language=JSON
  val calendarJson: JsValue = Json.parse(
    s"""{
    "staggerCode": "0002",
    "directDebit": {
      "ddiEligibilityInd": true,
      "active": {
      "periodEndDate": "2016-06-31",
      "periodPaymentDate": "2016-08-09"
    }
    },
    "currentPeriod": {
      "periodStartDate": "2016-04-01",
      "periodEndDate": "2016-06-30",
      "periodAnnAccInd": false,
      "normalEPaymentCollectionDate": "2016-08-09"
    },
    "previousPeriods": [ {
    "periodStartDate": "2016-01-01",
    "periodEndDate": "2016-03-31",
    "returnReceivedDate": "2016-04-05",
    "periodAnnAccInd": false,
    "normalEPaymentCollectionDate": "2016-05-09"
  },
    {
      "periodStartDate": "2015-10-01",
      "periodEndDate": "2015-12-31",
      "returnReceivedDate": "2016-01-05",
      "periodAnnAccInd": false,
      "normalEPaymentCollectionDate": "2016-02-09"
    }
    ]
  }""".stripMargin)

  //language=JSON
  val calendarCurrentJson: JsValue = Json.parse(
    s"""{
    "staggerCode": "0002",
    "directDebit": {
      "ddiEligibilityInd": true,
      "active": {
      "periodEndDate": "2016-06-31",
      "periodPaymentDate": "2016-08-09"
    }
    },
    "currentPeriod": {
      "periodStartDate": "2016-04-01",
      "periodEndDate": "2016-06-30",
      "returnReceivedDate": "2016-07-05",
      "periodAnnAccInd": false,
      "normalEPaymentCollectionDate": "2016-08-09"
    },
    "previousPeriods": [ {
    "periodStartDate": "2016-01-01",
    "periodEndDate": "2016-03-31",
    "returnReceivedDate": "2016-04-05",
    "periodAnnAccInd": false,
    "normalEPaymentCollectionDate": "2016-05-09"
  },
    {
      "periodStartDate": "2015-10-01",
      "periodEndDate": "2015-12-31",
      "returnReceivedDate": "2016-01-05",
      "periodAnnAccInd": false,
      "normalEPaymentCollectionDate": "2016-02-09"
    }
    ]
  }""".stripMargin)

  val vatDesignatoryDetailsAddress: VatDesignatoryDetailsAddress = VatDesignatoryDetailsAddress("1 Johnson Close", Some("Stonesfield"), Some("Oxford"), None, None, "OX29 8PP")

  //language=JSON
  val vatDesignatoryDetailsAddressJson: JsValue = Json.parse(
    s"""
  {
    "address":{
      "addressLine1":"1 Johnson Close",
      "postcode":"OX29 8PP",
      "addressLine2":"Stonesfield",
      "addressLine3":"Oxford"
    },
    "contact":{
      "email":{
      "primary":"email@email.com"},
      "telephone":{
      "daytime":"02017362727",
      "mobile":"07836353421"
    }
    },
    "name":{}
  }""".stripMargin)

}
