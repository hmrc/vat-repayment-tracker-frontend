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

package model

import javax.inject.{Inject, Singleton}
import play.api.libs.json._
import service.CountriesService

final case class Address(line1: Option[String], line2: Option[String], line3: Option[String], line4: Option[String], postCode: Option[String], countryCode: Option[String])

object Address {
  implicit val format: OFormat[Address] = Json.format[Address]
}

@Singleton
class AddressFormter @Inject() (countriesService: CountriesService) {

  val lineReturn = "<br>"

  def getFormattedAddress(vrn: String, address: Address): String = {
    address.line1.fold("")(_ + lineReturn) +
      address.line2.fold("")(_ + lineReturn) +
      address.line3.fold("")(_ + lineReturn) +
      address.line4.fold("")(_ + lineReturn) +
      address.postCode.fold("")(_ + lineReturn) +
      address.countryCode.fold("")(countriesService.getCountry(_) + lineReturn)
  }

}

