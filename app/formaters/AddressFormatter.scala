/*
 * Copyright 2024 HM Revenue & Customs
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

package formaters

import javax.inject.{Inject, Singleton}
import model.des.Address
import model.vat.VatDesignatoryDetailsAddress
import service.CountriesService

@Singleton
class AddressFormatter @Inject() (countriesService: CountriesService) {

  val lineReturn = "<br>"

  def getFormattedAddressMtd(address: Address): String = {

    val countryName: String = address.countryCode match {
      case Some(code) => if (code == "GB") "" else if (code == "GBR") "" else countriesService.getCountryName(code)
      case None       => ""
    }
    address.line1.fold("")(_ + lineReturn) +
      address.line2.fold("")(_ + lineReturn) +
      address.line3.fold("")(_ + lineReturn) +
      address.line4.fold("")(_ + lineReturn) +
      address.postCode.fold("")(_ + lineReturn) + countryName

  }

  def getFormattedAddressNonMtd(address: VatDesignatoryDetailsAddress): String = {

    address.addressLine1 + lineReturn +
      address.addressLine2.fold("")(_ + lineReturn) +
      address.addressLine3.fold("")(_ + lineReturn) +
      address.addressLine4.fold("")(_ + lineReturn) +
      address.addressLine5.fold("")(_ + lineReturn) +
      address.postcode

  }

}
