
# vat-repayment-tracker-frontend

### What is vat repayment tracker

Vat repayment tracker is a collection of services that allows users with a vat enrolment (MTD/NonMtd) to check the status of their vat repayments.
The other services that are related to it's functionality are:
* [vat-repayment-tracker-backend](https://github.com/hmrc/vat-repayment-tracker-backend)
    * contains the mongodb which stores state of users repayments and user's status is cached each time they use the service
* [payments-orchestrator](https://github.com/hmrc/payments-orchestrator)
    * service used to talk to DES
    * GET financial-data 
    * GET repayment information



---

### Contents

* [Running the locally](https://github.com/hmrc/vat-repayment-tracker-frontend#running-locally)
* [Running tests](https://github.com/hmrc/vat-repayment-tracker-frontend#running-tests)
* [Test data](https://github.com/hmrc/vat-repayment-tracker-frontend#test-data)
* [Further information](https://github.com/hmrc/vat-repayment-tracker-frontend#further-information)
* [Test suites](https://github.com/hmrc/vat-repayment-tracker-frontend#test-suits)

---

### Running Locally

You can run the service locally using sbt: `sbt run`

If running locally, the service runs on port `9863`

Service manager profile: `sm2 --start VRT_ALL`

*make sure you have mongo running

***To avoid memory leak use 'sbt -mem 2048 clean test compile'***

---

### Running tests

You can run the tests locally using sbt: sbt test

To run a specific test file, run `sbt 'testOnly *<SpecName>'`, e.g. `sbt 'testOnly *ControllerSpec'`

---

### Test data
[Test data](https://confluence.tools.tax.service.gov.uk/display/OPS/VRT+Test+Data)

---

### Further information
[See further information about this service on confluence](https://confluence.tools.tax.service.gov.uk/display/OPS/VAT+repayment+tracker+frontend)

---
### Test suites
Has unit tests within the repo as well as:

[performance tests](https://github.com/hmrc/vat-repayment-tracker-performance-tests)

[ui tests](https://github.com/hmrc/vat-repayment-tracker-ui-tests)

---


### License     

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
