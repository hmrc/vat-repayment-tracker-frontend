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

import model.Vrn
import play.api.libs.json.{JsValue, Json}

object DesData {

  // language=JSON
  val customerDataNotFound: JsValue = Json.parse(s"""
                                                   {
                                                       "code": "NOT_FOUND",
                                                       "reason": "The back end has indicated that No subscription can be found."
                                                   }
       """.stripMargin)

  // language=JSON
  val customerDataOk: JsValue = Json.parse(s"""
     {
         "approvedInformation": {
             "customerDetails": {
                 "nameIsReadOnly": true,
                 "organisationName": "TAXPAYER NAME_1",
                 "dataOrigin": "0001",
                 "mandationStatus": "1",
                 "registrationReason": "0001",
                 "effectiveRegistrationDate": "2017-01-02",
                 "businessStartDate": "2017-01-01",
                 "welshIndicator": true,
                 "partyType": "50",
                 "optionToTax": true,
                 "isPartialMigration": false,
                 "isInsolvent": false,
                 "overseasIndicator": true
             },
             "PPOB": {
                 "address": {
                     "line1": "VAT PPOB Line1",
                     "line2": "VAT PPOB Line2",
                     "line3": "VAT PPOB Line3",
                     "line4": "VAT PPOB Line4",
                     "postCode": "TF3 4ER",
                     "countryCode": "GB"
                 },
                 "contactDetails": {
                     "primaryPhoneNumber": "012345678901",
                     "mobileNumber": "012345678902",
                     "faxNumber": "012345678903",
                     "emailAddress": "lewis.hay@digital.hmrc.gov.uk",
                     "emailVerified": true
                 },
                 "websiteAddress": "www.tumbleweed.com"
             },
             "bankDetails": {
                 "accountHolderName": "*********",
                 "bankAccountNumber": "****2490",
                 "sortCode": "40****"
             },
             "businessActivities": {
                 "primaryMainCode": "10410",
                 "mainCode2": "10611",
                 "mainCode3": "10710",
                 "mainCode4": "10720"
             },
             "flatRateScheme": {
                 "FRSCategory": "003",
                 "FRSPercentage": 59.99,
                 "startDate": "0001-01-01",
                 "endDate": "9999-12-31",
                 "limitedCostTrader": true
             },
             "returnPeriod": {
                 "stdReturnPeriod": "MM"
             }
         },
         "inFlightInformation": {
             "changeIndicators": {
                 "organisationDetails": false,
                 "PPOBDetails": false,
                 "correspondenceContactDetails": false,
                 "bankDetails": true,
                 "returnPeriod": false,
                 "flatRateScheme": false,
                 "businessActivities": false,
                 "deregister": false,
                 "effectiveDateOfRegistration": false,
                 "mandationStatus": true
             },
             "inFlightChanges": {
                 "bankDetails": {
                     "formInformation": {
                         "formBundle": "092000001020",
                         "dateReceived": "2019-03-04"
                     },
                     "accountHolderName": "******",
                     "bankAccountNumber": "****2490",
                     "sortCode": "****84"
                 },
                 "mandationStatus": {
                     "formInformation": {
                         "formBundle": "092000002124",
                         "dateReceived": "2019-08-15"
                     },
                     "mandationStatus": "3"
                 }
             }
         }
     }
       """.stripMargin)

  val customerDataOkWithoutBankDetails: JsValue = Json.parse(s"""
     {
         "approvedInformation": {
             "customerDetails": {
                 "nameIsReadOnly": true,
                 "organisationName": "TAXPAYER NAME_1",
                 "dataOrigin": "0001",
                 "mandationStatus": "1",
                 "registrationReason": "0001",
                 "effectiveRegistrationDate": "2017-01-02",
                 "businessStartDate": "2017-01-01",
                 "welshIndicator": true,
                 "partyType": "50",
                 "optionToTax": true,
                 "isPartialMigration": false,
                 "isInsolvent": false,
                 "overseasIndicator": true
             },
             "PPOB": {
                 "address": {
                     "line1": "VAT PPOB Line1",
                     "line2": "VAT PPOB Line2",
                     "line3": "VAT PPOB Line3",
                     "line4": "VAT PPOB Line4",
                     "postCode": "TF3 4ER",
                     "countryCode": "GB"
                 },
                 "contactDetails": {
                     "primaryPhoneNumber": "012345678901",
                     "mobileNumber": "012345678902",
                     "faxNumber": "012345678903",
                     "emailAddress": "lewis.hay@digital.hmrc.gov.uk",
                     "emailVerified": true
                 },
                 "websiteAddress": "www.tumbleweed.com"
             },
             "businessActivities": {
                 "primaryMainCode": "10410",
                 "mainCode2": "10611",
                 "mainCode3": "10710",
                 "mainCode4": "10720"
             },
             "flatRateScheme": {
                 "FRSCategory": "003",
                 "FRSPercentage": 59.99,
                 "startDate": "0001-01-01",
                 "endDate": "9999-12-31",
                 "limitedCostTrader": true
             },
             "returnPeriod": {
                 "stdReturnPeriod": "MM"
             }
         },
         "inFlightInformation": {
             "changeIndicators": {
                 "organisationDetails": false,
                 "PPOBDetails": false,
                 "correspondenceContactDetails": false,
                 "bankDetails": true,
                 "returnPeriod": false,
                 "flatRateScheme": false,
                 "businessActivities": false,
                 "deregister": false,
                 "effectiveDateOfRegistration": false,
                 "mandationStatus": true
             },
             "inFlightChanges": {
                 "bankDetails": {
                     "formInformation": {
                         "formBundle": "092000001020",
                         "dateReceived": "2019-03-04"
                     },
                     "accountHolderName": "******",
                     "bankAccountNumber": "****2490",
                     "sortCode": "****84"
                 },
                 "mandationStatus": {
                     "formInformation": {
                         "formBundle": "092000002124",
                         "dateReceived": "2019-08-15"
                     },
                     "mandationStatus": "3"
                 }
             }
         }
     }
       """.stripMargin)

  // language=JSON
  val obligationsDataNotFound: JsValue = Json.parse(s"""
                                                      {
                                                          "code": "NOT_FOUND",
                                                          "reason": "The remote endpoint has indicated that no data can be found."
                                                      }
       """.stripMargin)

  // language=JSON
  def obligationsDataOk(vrn: Vrn): JsValue = Json.parse(s"""
                                                 {
                                                     "obligations": [
                                                         {
                                                             "identification": {"incomeSourceType": "ITSA","referenceNumber": "${vrn.value}","referenceType": "VRN"},
                                                             "obligationDetails": [
                                                                 {
                                                                     "status": "O",
                                                                     "inboundCorrespondenceFromDate": "2018-04-01",
                                                                     "inboundCorrespondenceToDate": "2018-04-30",
                                                                     "inboundCorrespondenceDateReceived": "2018-04-15",
                                                                     "inboundCorrespondenceDueDate": "2018-06-07",
                                                                     "periodKey": "18AD"
                                                                 },
                                                                 {
                                                                     "status": "O",
                                                                     "inboundCorrespondenceFromDate": "2018-03-01",
                                                                     "inboundCorrespondenceToDate": "2018-03-31",
                                                                     "inboundCorrespondenceDateReceived": "2018-04-12",
                                                                     "inboundCorrespondenceDueDate": "2018-05-07",
                                                                     "periodKey": "18AC"
                                                                 },
                                                                 {
                                                                     "status": "O",
                                                                     "inboundCorrespondenceFromDate": "2018-02-01",
                                                                     "inboundCorrespondenceToDate": "2018-02-28",
                                                                     "inboundCorrespondenceDateReceived": "2018-04-12",
                                                                     "inboundCorrespondenceDueDate": "2018-04-07",
                                                                     "periodKey": "18AB"
                                                                 },
                                                                 {
                                                                     "status": "O",
                                                                     "inboundCorrespondenceFromDate": "2018-01-01",
                                                                     "inboundCorrespondenceToDate": "2018-01-31",
                                                                     "inboundCorrespondenceDateReceived": "2018-04-15",
                                                                     "inboundCorrespondenceDueDate": "2018-03-07",
                                                                     "periodKey": "18AA"
                                                                 }
                                                             ]
                                                         }
                                                     ]
                                                 }
       """.stripMargin)

  // language=JSON
  val financialDataNotFound: JsValue = Json.parse(s"""
                                                     {
                                                        "code": "NOT_FOUND",
                                                        "reason": "The remote endpoint has indicated that no data can be found."
                                                    }
       """.stripMargin)

  // language=JSON
  def financialDataOk(vrn: Vrn): JsValue = Json.parse(s"""
                                                 {
                                                   "idType": "VRN",
                                                   "idNumber": "${vrn.value}",
                                                   "regimeType": "VATC",
                                                   "processingDate": "2019-08-20T10:44:05Z",
                                                   "financialTransactions": [
                                                       {
                                                           "chargeType": "VAT Protective Assessment",
                                                           "mainType": "VAT Protective Assessment",
                                                           "periodKey": "18AC",
                                                           "periodKeyDescription": "March 2018",
                                                           "taxPeriodFrom": "2018-03-01",
                                                           "taxPeriodTo": "2018-03-31",
                                                           "businessPartner": "0100113120",
                                                           "contractAccountCategory": "33",
                                                           "contractAccount": "091700000405",
                                                           "contractObjectType": "ZVAT",
                                                           "contractObject": "00000180000000000165",
                                                           "sapDocumentNumber": "002720000571",
                                                           "sapDocumentNumberItem": "0001",
                                                           "chargeReference": "XB002616013425",
                                                           "mainTransaction": "4733",
                                                           "subTransaction": "1174",
                                                           "originalAmount": 796.0,
                                                           "outstandingAmount": 796.0,
                                                           "accruedInterest": 24.23,
                                                           "items": [
                                                               {
                                                                   "subItem": "000",
                                                                   "dueDate": "2018-09-12",
                                                                   "amount": 796.0
                                                               }
                                                           ]
                                                       },
                                                       {
                                                           "chargeType": "VAT PA Default Interest",
                                                           "mainType": "VAT PA Default Interest",
                                                           "periodKey": "18AC",
                                                           "periodKeyDescription": "March 2018",
                                                           "taxPeriodFrom": "2018-03-01",
                                                           "taxPeriodTo": "2018-03-31",
                                                           "businessPartner": "0100113120",
                                                           "contractAccountCategory": "33",
                                                           "contractAccount": "091700000405",
                                                           "contractObjectType": "ZVAT",
                                                           "contractObject": "00000180000000000165",
                                                           "sapDocumentNumber": "002720000571",
                                                           "sapDocumentNumberItem": "0002",
                                                           "chargeReference": "XB002616013425",
                                                           "mainTransaction": "4708",
                                                           "subTransaction": "1175",
                                                           "originalAmount": 3.59,
                                                           "outstandingAmount": 3.59,
                                                           "items": [
                                                               {
                                                                   "subItem": "000",
                                                                   "dueDate": "2018-08-13",
                                                                   "amount": 3.59
                                                               }
                                                           ]
                                                       },
                                                       {
                                                           "chargeType": "VAT Return Debit Charge",
                                                           "mainType": "VAT Return Charge",
                                                           "periodKey": "18AC",
                                                           "periodKeyDescription": "March 2018",
                                                           "taxPeriodFrom": "2018-03-01",
                                                           "taxPeriodTo": "2018-03-31",
                                                           "businessPartner": "0100113120",
                                                           "contractAccountCategory": "33",
                                                           "contractAccount": "091700000405",
                                                           "contractObjectType": "ZVAT",
                                                           "contractObject": "00000180000000000165",
                                                           "sapDocumentNumber": "003030001189",
                                                           "sapDocumentNumberItem": "0001",
                                                           "chargeReference": "XJ002610110056",
                                                           "mainTransaction": "4700",
                                                           "subTransaction": "1174",
                                                           "originalAmount": 10169.45,
                                                           "outstandingAmount": 10169.45,
                                                           "items": [
                                                               {
                                                                   "subItem": "001",
                                                                   "dueDate": "2018-05-07",
                                                                   "amount": 135.0
                                                               },
                                                               {
                                                                   "subItem": "000",
                                                                   "dueDate": "2018-05-07",
                                                                   "amount": 10034.45
                                                               }
                                                           ]
                                                       },
                                                       {
                                                           "chargeType": "VAT Protective Assessment",
                                                           "mainType": "VAT Protective Assessment",
                                                           "periodKey": "18AC",
                                                           "periodKeyDescription": "March 2018",
                                                           "taxPeriodFrom": "2018-03-01",
                                                           "taxPeriodTo": "2018-03-31",
                                                           "businessPartner": "0100113120",
                                                           "contractAccountCategory": "33",
                                                           "contractAccount": "091700000405",
                                                           "contractObjectType": "ZVAT",
                                                           "contractObject": "00000180000000000165",
                                                           "sapDocumentNumber": "003360001206",
                                                           "sapDocumentNumberItem": "0001",
                                                           "chargeReference": "XV002616013469",
                                                           "mainTransaction": "4733",
                                                           "subTransaction": "1174",
                                                           "originalAmount": 796.0,
                                                           "outstandingAmount": 796.0,
                                                           "accruedInterest": 23.45,
                                                           "items": [
                                                               {
                                                                   "subItem": "000",
                                                                   "dueDate": "2018-09-23",
                                                                   "amount": 796.0
                                                               }
                                                           ]
                                                       },
                                                       {
                                                           "chargeType": "VAT PA Default Interest",
                                                           "mainType": "VAT PA Default Interest",
                                                           "periodKey": "18AC",
                                                           "periodKeyDescription": "March 2018",
                                                           "taxPeriodFrom": "2018-03-01",
                                                           "taxPeriodTo": "2018-03-31",
                                                           "businessPartner": "0100113120",
                                                           "contractAccountCategory": "33",
                                                           "contractAccount": "091700000405",
                                                           "contractObjectType": "ZVAT",
                                                           "contractObject": "00000180000000000165",
                                                           "sapDocumentNumber": "003360001206",
                                                           "sapDocumentNumberItem": "0002",
                                                           "chargeReference": "XV002616013469",
                                                           "mainTransaction": "4708",
                                                           "subTransaction": "1175",
                                                           "originalAmount": 5.56,
                                                           "outstandingAmount": 5.56,
                                                           "items": [
                                                               {
                                                                   "subItem": "000",
                                                                   "dueDate": "2018-08-24",
                                                                   "amount": 5.56
                                                               }
                                                           ]
                                                       }
                                                   ]
                                               }
       """.stripMargin)

  // language=JSON
  def financialDataSingleOk(vrn: Vrn): JsValue = Json.parse(s"""
                                                 {
                                                   "idType": "VRN",
                                                   "idNumber": "${vrn.value}",
                                                   "regimeType": "VATC",
                                                   "processingDate": "2019-08-20T10:44:05Z",
                                                   "financialTransactions": [
                                                       {
                                                           "chargeType": "VAT PA Default Interest",
                                                           "mainType": "VAT PA Default Interest",
                                                           "periodKey": "18AC",
                                                           "periodKeyDescription": "March 2018",
                                                           "taxPeriodFrom": "2018-03-01",
                                                           "taxPeriodTo": "2018-03-31",
                                                           "businessPartner": "0100113120",
                                                           "contractAccountCategory": "33",
                                                           "contractAccount": "091700000405",
                                                           "contractObjectType": "ZVAT",
                                                           "contractObject": "00000180000000000165",
                                                           "sapDocumentNumber": "003360001206",
                                                           "sapDocumentNumberItem": "0002",
                                                           "chargeReference": "XV002616013469",
                                                           "mainTransaction": "4708",
                                                           "subTransaction": "1175",
                                                           "originalAmount": 5.56,
                                                           "outstandingAmount": 5.56,
                                                           "items": [
                                                               {
                                                                   "subItem": "000",
                                                                   "dueDate": "2018-08-24",
                                                                   "amount": 5.56
                                                               }
                                                           ]
                                                       }
                                                   ]
                                               }
       """.stripMargin)
}

