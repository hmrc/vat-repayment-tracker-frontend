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

package service

package service

import support.ITSpec

class CountriesServiceSpec extends ITSpec {

  val countryService = fakeApplication.injector.instanceOf[CountriesService]
  "countryService" - {

    "should Return a country when Given  a code " in {

      countryService.getCountry("GBR") shouldBe "United Kingdom"
    }
  }
}
