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

package support

import java.time.LocalDate

import model.{PeriodKey, Vrn, VrtRepaymentDetailData}
import model.des._
import play.api.libs.json.{JsValue, Json}

object DesData {

  private val bankDetails: BankDetails = BankDetails(Some("Account holder"), Some("11112222"), Some("667788"))
  private val address: Address = Address(Some("VAT PPOB Line1"), Some("VAT PPOB Line2"), Some("VAT PPOB Line3"), Some("VAT PPOB Line4"), Some("TF3 4ER"), Some("GB"))
  private val ppob: PPOB = PPOB(Some(address))
  private val customerDetails: CustomerDetails = CustomerDetails(Some(true), Some(false))
  private val approvedInformation: ApprovedInformation = ApprovedInformation(Some(customerDetails), Some(bankDetails), Some(ppob))
  private val changeIndicators: ChangeIndicators = ChangeIndicators(Some(true), Some(false))
  private val inFlightInformation: InFlightInformation = InFlightInformation(Some(changeIndicators))
  private val transaction: Transaction = Transaction("VAT Return Credit Charge", Option("18AC"))

  private val repaymentDetail: RepaymentDetailData = RepaymentDetailData(
    LocalDate.parse("2001-01-01"),
    Option(LocalDate.parse("2001-01-01")),
    Option(LocalDate.parse("2001-01-01")),
    "18AC",
    "INITIAL",
    BigDecimal(10),
    Option(1),
    100.02
  )

  private val vrn: Vrn = Vrn("2345678891")

  val approvedCustomerInformation: CustomerInformation = CustomerInformation(Some(approvedInformation), None)
  val customerInformation: CustomerInformation = CustomerInformation(Some(approvedInformation), Some(inFlightInformation))
  val financialData: FinancialData = FinancialData("VRN", "2345678890", "VATC", "2019-08-20T10:44:05Z", Seq(transaction))
  val directDebitData: DirectDebitData = DirectDebitData(Some(List(DirectDebitDetails("Tester Surname", "404784", "70872490"))))
  val vrtRepaymentDetailData: VrtRepaymentDetailData = VrtRepaymentDetailData(None, LocalDate.now(), vrn, repaymentDetail)

  //language=JSON
  val vrtRepaymentDetailDataJson: JsValue = Json.parse(
    s"""{
        "creationDate": "${LocalDate.now()}",
        "vrn": "2345678891",
        "repaymentDetailsData": {
          "returnCreationDate": "2001-01-01",
          "sentForRiskingDate": "2001-01-01",
          "lastUpdateReceivedDate": "2001-01-01",
          "periodKey": "18AC",
          "riskingStatus": "INITIAL",
          "vatToPay_BOX5": 10.00,
          "supplementDelayDays": 1,
          "originalPostingAmount": 100.02
      }
    }""".stripMargin
  )

  val repaymentsDetail: Seq[RepaymentDetailData] = Seq(repaymentDetail)

  //language=JSON
  val repaymentDetailJson: JsValue = Json.parse(
    s"""[
          {
            "returnCreationDate": "2001-01-01",
            "sentForRiskingDate": "2001-01-01",
            "lastUpdateReceivedDate": "2001-01-01",
            "periodKey": "18AC",
            "riskingStatus": "INITIAL",
            "vatToPay_BOX5": 10.00,
            "supplementDelayDays": 1,
            "originalPostingAmount": 100.02
          }
        ]""".stripMargin
  )

  //language=JSON
  def repaymentDetails2DifferentPeriods(date: String, date2: String, status1: String, status2: String): JsValue = Json.parse(
    s"""[
          {
             "returnCreationDate": "$date",
             "sentForRiskingDate": "$date",
             "lastUpdateReceivedDate": "$date",
             "periodKey": "18AF",
             "riskingStatus": "$status1",
             "vatToPay_BOX5": 10.00,
             "supplementDelayDays": 6,
             "originalPostingAmount": 5.56
          },
          {
            "returnCreationDate": "$date2",
            "sentForRiskingDate": "$date2",
            "lastUpdateReceivedDate": "$date2",
            "periodKey": "18AG",
            "riskingStatus": "$status2",
            "vatToPay_BOX5": 10.01,
            "supplementDelayDays": 6,
            "originalPostingAmount": 5.56
          }
      ]""".stripMargin
  )

  //language=JSON
  def repaymentDetailSingleCompleted: JsValue = Json.parse(
    s"""[{
        "returnCreationDate": "2001-01-01",
        "sentForRiskingDate": "2001-01-01",
        "lastUpdateReceivedDate": "2001-01-01",
        "periodKey": "18AG",
        "riskingStatus": "ADJUSMENT_TO_TAX_DUE",
        "vatToPay_BOX5": 6.56,
        "supplementDelayDays": 1,
        "originalPostingAmount": 5.56
      }]""".stripMargin
  )

  //language=JSON
  def repaymentDetailsMultipleInProgress: JsValue = Json.parse(
    s"""[
        {
          "returnCreationDate": "2001-01-01",
          "sentForRiskingDate": "2001-01-01",
          "lastUpdateReceivedDate": "2001-01-01",
          "periodKey": "18AA",
          "riskingStatus": "INITIAL",
          "vatToPay_BOX5": 6.56,
          "supplementDelayDays": 1,
          "originalPostingAmount": 5.56
        },
        {
          "returnCreationDate": "2001-01-01",
          "sentForRiskingDate": "2001-01-01",
          "lastUpdateReceivedDate": "2001-01-01",
          "periodKey": "18AD",
          "riskingStatus": "INITIAL",
          "vatToPay_BOX5": 6.56,
          "supplementDelayDays": 1,
          "originalPostingAmount": 5.56
      },
      {
         "returnCreationDate": "2001-01-01",
         "sentForRiskingDate": "2001-01-01",
         "lastUpdateReceivedDate": "2001-01-01",
         "periodKey": "18AG",
         "riskingStatus": "SENT_FOR_RISKING",
         "vatToPay_BOX5": 6.56,
         "supplementDelayDays": 1,
         "originalPostingAmount": 5.56
       },
      {
        "returnCreationDate": "2001-01-01",
        "sentForRiskingDate": "2001-01-01",
        "lastUpdateReceivedDate": "2001-01-01",
        "periodKey": "18AJ",
        "riskingStatus": "CLAIM_QUERIED",
        "vatToPay_BOX5": 6.56,
        "supplementDelayDays": 1,
        "originalPostingAmount": 5.56
      }
    ]""".stripMargin
  )

  //language=JSON
  def repaymentDetailsMultipleCompleted(): JsValue = Json.parse(
    s"""[
          {
            "returnCreationDate": "2001-01-01",
            "sentForRiskingDate": "2001-01-01",
            "lastUpdateReceivedDate": "2001-01-01",
            "periodKey": "18AA",
            "riskingStatus": "REPAYMENT_ADJUSTED",
            "vatToPay_BOX5": 6.56,
            "supplementDelayDays": 1,
            "originalPostingAmount": 5.56
          },
          {
            "returnCreationDate": "2001-01-01",
            "sentForRiskingDate": "2001-01-01",
            "lastUpdateReceivedDate": "2001-01-01",
            "periodKey": "18AD",
            "riskingStatus": "REPAYMENT_ADJUSTED",
            "vatToPay_BOX5": 6.56,
            "supplementDelayDays": 1,
            "originalPostingAmount": 5.56
          },
          {
             "returnCreationDate": "2001-01-01",
             "sentForRiskingDate": "2001-01-01",
             "lastUpdateReceivedDate": "2001-01-01",
             "periodKey": "18AG",
             "riskingStatus": "ADJUSMENT_TO_TAX_DUE",
             "vatToPay_BOX5": 6.56,
             "supplementDelayDays": 1,
             "originalPostingAmount":5.56
          },
          {
             "returnCreationDate": "2001-01-01",
             "sentForRiskingDate": "2001-01-01",
             "lastUpdateReceivedDate": "2001-01-01",
             "periodKey": "18AJ",
             "riskingStatus": "REPAYMENT_APPROVED",
             "vatToPay_BOX5": 6.56,
             "supplementDelayDays": 1,
             "originalPostingAmount": 5.56
          }
        ]""".stripMargin
  )

  //language=JSON
  def repaymentDetails3Inprogree1Completed(): JsValue = Json.parse(
    s"""[
        {
            "returnCreationDate": "2001-01-01",
            "sentForRiskingDate": "2001-01-01",
            "lastUpdateReceivedDate": "2001-01-01",
            "periodKey": "18AA",
            "riskingStatus": "INITIAL",
            "vatToPay_BOX5": 6.56,
            "supplementDelayDays": 1,
            "originalPostingAmount": 5.56
        },
        {
          "returnCreationDate": "2001-01-01",
          "sentForRiskingDate": "2001-01-01",
          "lastUpdateReceivedDate": "2001-01-01",
          "periodKey": "18AD",
          "riskingStatus": "SENT_FOR_RISKING",
          "vatToPay_BOX5": 6.56,
          "supplementDelayDays": 1,
          "originalPostingAmount": 5.56
        },
        {
          "returnCreationDate": "2001-01-01",
          "sentForRiskingDate": "2001-01-01",
          "lastUpdateReceivedDate": "2001-01-01",
          "periodKey": "18AG",
          "riskingStatus": "CLAIM_QUERIED",
          "vatToPay_BOX5": 6.56,
          "supplementDelayDays": 1,
          "originalPostingAmount": 5.56
        },
        {
           "returnCreationDate": "2001-01-01",
           "sentForRiskingDate": "2001-01-01",
           "lastUpdateReceivedDate": "2001-01-01",
           "periodKey": "18AJ",
           "riskingStatus": "REPAYMENT_APPROVED",
           "vatToPay_BOX5": 6.56,
           "supplementDelayDays": 1,
           "originalPostingAmount": 5.56
          }
        ]""".stripMargin
  )

  //language=JSON
  val directDebitDataJson: JsValue = Json.parse(
    s"""{
          "directDebitDetails": [
            {
              "accountHolderName": "Tester Surname",
              "sortCode": "404784",
              "accountNumber": "70872490"
            }
          ]
        }""".stripMargin)

  //language=JSON
  val approvedInformationJson: JsValue = Json.parse(
    s"""{
            "approvedInformation":{
              "customerDetails": {
                "welshIndicator": true,
                "isPartialMigration": false
              },
              "bankDetails":{
                "accountHolderName":"Account holder",
                "bankAccountNumber":"11112222",
                "sortCode":"667788"
              },
              "PPOB":{
                "address":{
                   "line1":"VAT PPOB Line1",
                   "line2":"VAT PPOB Line2",
                   "line3":"VAT PPOB Line3",
                   "line4":"VAT PPOB Line4",
                   "postCode":"TF3 4ER",
                   "countryCode":"GB"
                }
              }
            }
        }""".stripMargin)

  //language=JSON
  val financialDataJson: JsValue = Json.parse(
    s"""
       {
          "idType":"VRN",
          "idNumber":"2345678890",
          "regimeType":"VATC",
          "processingDate":"2019-08-20T10:44:05Z",
          "financialTransactions":[
             {
                "chargeType": "VAT Return Credit Charge",
                "periodKey":"18AC"
             }
          ]
       }
     """.stripMargin
  )

  // language=JSON
  val customerDataOkWithPartialBankDetails: JsValue = Json.parse(
    s"""
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
                 "bankAccountNumber": "11112222",
                 "sortCode": "667788"
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
                     "accountHolderName": "Account holder",
                     "bankAccountNumber": "11112222",
                     "sortCode": "667788"
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
  def customerDataOk(isPartial: Boolean = false): JsValue = Json.parse(
    s"""
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
                 "isPartialMigration": $isPartial,
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
                 "accountHolderName": "Account holder",
                 "bankAccountNumber": "11112222",
                 "sortCode": "667788"
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
                 "bankDetails": false,
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
                     "accountHolderName": "Account holder",
                     "bankAccountNumber": "11112222",
                     "sortCode": "667788"
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
  val customerDataOkWithoutBankDetails: JsValue = Json.parse(
    s"""
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
                 "bankDetails": false,
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
                     "accountHolderName": "Account holder",
                     "bankAccountNumber": "11112222",
                     "sortCode": "667788"
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
  val customerDataOkWithoutBankDetailsInflight: JsValue = Json.parse(
    s"""
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
                     "accountHolderName": "Account holder",
                     "bankAccountNumber": "11112222",
                     "sortCode": "667788"
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
  def ddOk: JsValue = Json.parse(
    s"""
       {
           "directDebitMandateFound": true,
           "directDebitDetails": [
               {
                   "directDebitInstructionNumber": "091700000409",
                   "directDebitPlanType": "VPP",
                   "dateCreated": "2019-09-20",
                   "accountHolderName": "Tester Surname",
                   "sortCode": "404784",
                   "accountNumber": "70872490"
               }
           ]
       }
       """.stripMargin)

  // language=JSON
  val ddOkNoMandate: JsValue = Json.parse(
    s"""{
          "directDebitMandateFound": false
        }""".stripMargin)

  //language=JSON
  def storedRepaymentDetails1(date: String, status1: String, periodKey: PeriodKey): JsValue = Json.parse(
    s"""[
             {
               "_id":"5db1c9826b00005f47616c61",
               "creationDate":"$date",
               "vrn":"101747008",
               "repaymentDetailsData":{
               "returnCreationDate":"$date",
               "sentForRiskingDate":"$date",
               "lastUpdateReceivedDate":"$date",
               "periodKey": "${periodKey.value}",
               "riskingStatus":"$status1",
               "vatToPay_BOX5":6.56,
               "supplementDelayDays":6,
               "originalPostingAmount":0
             }
           }
         ]""".stripMargin
  )

  //language=JSON
  def storedRepaymentDetails2(date: String, status1: String, status2: String): JsValue = Json.parse(
    s"""[
             {
               "_id":"5db1c9826b00005f47616c61",
               "creationDate":"$date",
               "vrn":"101747008",
               "repaymentDetailsData":{
                 "returnCreationDate":"$date",
                 "sentForRiskingDate":"$date",
                 "lastUpdateReceivedDate":"$date",
                 "periodKey":"18AC",
                 "riskingStatus":"$status1",
                 "vatToPay_BOX5":6.56,
                 "supplementDelayDays":6,
                 "originalPostingAmount":5.56
               }
             },
             {
               "_id":"5db1c9826b00005f47616c62",
               "creationDate":"$date",
               "vrn":"101747008",
               "repaymentDetailsData":{
                 "returnCreationDate":"$date",
                 "sentForRiskingDate":"$date",
                 "lastUpdateReceivedDate":"$date",
                 "periodKey":"18AC",
                 "riskingStatus":"$status2",
                 "vatToPay_BOX5":6.56,
                 "supplementDelayDays":6,
                 "originalPostingAmount":5.56
               }
             }
          ]""".stripMargin
  )

  //language=JSON
  def storedRepaymentDetails3(date: String, status1: String, status2: String, status3: String): JsValue = Json.parse(
    s"""[
             {
               "_id":"5db1c9826b00005f47616c61",
               "creationDate":"$date",
               "vrn":"101747008",
               "repaymentDetailsData":{
                 "returnCreationDate":"$date",
                 "sentForRiskingDate":"$date",
                 "lastUpdateReceivedDate":"$date",
                 "periodKey":"18AC",
                 "riskingStatus":"$status1",
                 "vatToPay_BOX5":6.56,
                 "supplementDelayDays":6,
                 "originalPostingAmount":5.56
               }
             },
             {
               "_id":"5db1c9826b00005f47616c62",
               "creationDate":"$date",
               "vrn":"101747008",
               "repaymentDetailsData":{
                 "returnCreationDate":"$date",
                 "sentForRiskingDate":"$date",
                 "lastUpdateReceivedDate":"$date",
                 "periodKey":"18AC",
                 "riskingStatus":"$status2",
                 "vatToPay_BOX5":6.56,
                 "supplementDelayDays":6,
                 "originalPostingAmount":5.56
               }
             },
             {
               "_id":"5db1c9826b00005f47616c63",
               "creationDate":"$date",
               "vrn":"101747008",
               "repaymentDetailsData":{
                 "returnCreationDate":"$date",
                 "sentForRiskingDate":"$date",
                 "lastUpdateReceivedDate":"2019-10-24",
                 "periodKey":"18AC",
                 "riskingStatus":"$status3",
                 "vatToPay_BOX5":6.56,
                 "supplementDelayDays":6,
                 "originalPostingAmount":5.56
               }
             }
        ]""".stripMargin
  )

  //language=JSON
  def repaymentDetails1(date: String, status1: String, periodKey: PeriodKey, negativeAmt: Boolean): JsValue = Json.parse(
    s"""[
          {
            "returnCreationDate": "$date",
            "sentForRiskingDate": "$date",
            "lastUpdateReceivedDate": "$date",
            "periodKey": "${periodKey.value}",
            "riskingStatus": "$status1",
            "vatToPay_BOX5": "${if (negativeAmt) -6.56 else 6.56}",
            "supplementDelayDays": 6,
            "originalPostingAmount": 0
        }
      ]""".stripMargin
  )

  //language=JSON
  def repaymentDetails2(date: String, status1: String, status2: String): JsValue = Json.parse(
    s"""[
         {
             "returnCreationDate": "$date",
             "sentForRiskingDate": "$date",
             "lastUpdateReceivedDate": "$date",
             "periodKey": "18AG",
             "riskingStatus": "$status1",
             "vatToPay_BOX5": 6.56,
             "supplementDelayDays": 6,
             "originalPostingAmount": 5.56
         },
         {
            "returnCreationDate": "$date",
            "sentForRiskingDate": "$date",
            "lastUpdateReceivedDate": "$date",
            "periodKey": "18AG",
            "riskingStatus": "$status2",
            "vatToPay_BOX5": 6.56,
            "supplementDelayDays": 6,
            "originalPostingAmount": 5.56
        }
    ]""".stripMargin
  )

  //language=JSON
  def repaymentDetails3(date: String, status1: String, status2: String, status3: String): JsValue = Json.parse(
    s"""[
          {
             "returnCreationDate": "$date",
             "sentForRiskingDate": "$date",
             "lastUpdateReceivedDate": "$date",
             "periodKey": "18AG",
             "riskingStatus": "$status1",
             "vatToPay_BOX5": 6.56,
             "supplementDelayDays": 6,
             "originalPostingAmount": 5.56
         },
         {
            "returnCreationDate": "$date",
            "sentForRiskingDate": "$date",
            "lastUpdateReceivedDate": "$date",
            "periodKey": "18AG",
            "riskingStatus": "$status2",
            "vatToPay_BOX5": 6.56,
            "supplementDelayDays": 6,
            "originalPostingAmount": 5.56
        },
        {
             "returnCreationDate": "$date",
             "sentForRiskingDate": "$date",
             "lastUpdateReceivedDate": "$date",
             "periodKey": "18AG",
             "riskingStatus": "$status3",
             "vatToPay_BOX5": 6.56,
             "supplementDelayDays": 6,
             "originalPostingAmount": 5.56
         }
      ]""".stripMargin
  )

  // language=JSON
  def financialDataSingleCredit(vrn: Vrn): JsValue =
    Json.parse(s"""
         {
           "idType": "VRN",
           "idNumber": "${vrn.value}",
           "regimeType": "VATC",
           "processingDate": "2019-08-20T10:44:05Z",
           "financialTransactions": [
               {
                   "chargeType": "VAT Return Credit Charge",
                   "mainType": "VAT PA Default Interest",
                   "periodKey": "18AG",
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
                   "originalAmount": 6.56,
                   "outstandingAmount": 6.56,
                   "items": [
                       {
                           "subItem": "000",
                           "dueDate": "2018-08-24",
                           "amount": 6.56,
                           "clearingDate": "2018-03-01"
                       }
                   ]
               }
           ]
       }""".stripMargin)

  // language=JSON
  def financialDataSingleDebit(vrn: Vrn): JsValue =
    Json.parse(s"""
       {
         "idType": "VRN",
         "idNumber": "${vrn.value}",
         "regimeType": "VATC",
         "processingDate": "2019-08-20T10:44:05Z",
         "financialTransactions": [
             {
                 "chargeType": "VAT Return Debit Charge",
                 "mainType": "VAT PA Default Interest",
                 "periodKey": "18AG",
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
                 "originalAmount": 6.56,
                 "outstandingAmount": 6.56,
                 "items": [
                     {
                         "subItem": "000",
                         "dueDate": "2018-08-24",
                         "amount": 6.56,
                         "clearingDate": "2018-03-01"
                     }
                 ]
             }
         ]
     }""".stripMargin)

}

