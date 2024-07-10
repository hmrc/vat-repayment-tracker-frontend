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

package model.des

import play.api.libs.json.Json
import support.{DesData, UnitSpec}

class FinancialDataSpec extends UnitSpec {

  "to json" in {
    Json.toJson(DesData.financialData) shouldBe DesData.financialDataJson
  }

  "to json with no clearing date" in {
    Json.toJson(DesData.financialDataNoClearingDate) shouldBe DesData.financialDataNoClearingDateJson
  }

  "from json" in {
    DesData.financialDataJson.as[FinancialData] shouldBe DesData.financialData
  }

  "from json with no clearing date" in {
    DesData.financialDataNoClearingDateJson.as[FinancialData] shouldBe DesData.financialDataNoClearingDate
  }
}
