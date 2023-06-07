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

package service

import support.ItSpec

class CountriesServiceSpec extends ItSpec {

  val countryService: CountriesService = injector.instanceOf[CountriesService]
  "countryService" - {

    "should throw an exception when it does not recognise a country code" in {
      val exception = intercept[RuntimeException](countryService.getCountryName("AA"))
      exception.getMessage shouldBe "Unrecognised country code: AA"
    }

    "should Return a country when Given  a code " in {
      countryService.getCountryName("AD") shouldBe "Andorra"
      countryService.getCountryName("AE") shouldBe "United Arab Emirates"
      countryService.getCountryName("AF") shouldBe "Afghanistan"
      countryService.getCountryName("AG") shouldBe "Antigua and Barbuda"
      countryService.getCountryName("AI") shouldBe "Anguilla"
      countryService.getCountryName("AL") shouldBe "Albania"
      countryService.getCountryName("AM") shouldBe "Armenia"
      countryService.getCountryName("AN") shouldBe "Netherlands Antilles"
      countryService.getCountryName("AO") shouldBe "Angola"
      countryService.getCountryName("AQ") shouldBe "Antarctica"
      countryService.getCountryName("AR") shouldBe "Argentina"
      countryService.getCountryName("AS") shouldBe "American Samoa"
      countryService.getCountryName("AT") shouldBe "Austria"
      countryService.getCountryName("AU") shouldBe "Australia"
      countryService.getCountryName("AW") shouldBe "Aruba"
      countryService.getCountryName("AX") shouldBe "Åland Islands"
      countryService.getCountryName("AZ") shouldBe "Azerbaijan"
      countryService.getCountryName("BA") shouldBe "Bosnia and Herzegovina"
      countryService.getCountryName("BB") shouldBe "Barbados"
      countryService.getCountryName("BD") shouldBe "Bangladesh"
      countryService.getCountryName("BE") shouldBe "Belgium"
      countryService.getCountryName("BF") shouldBe "Burkina Faso"
      countryService.getCountryName("BG") shouldBe "Bulgaria"
      countryService.getCountryName("BH") shouldBe "Bahrain"
      countryService.getCountryName("BI") shouldBe "Burundi"
      countryService.getCountryName("BJ") shouldBe "Benin"
      countryService.getCountryName("BM") shouldBe "Bermuda"
      countryService.getCountryName("BN") shouldBe "Brunei"
      countryService.getCountryName("BO") shouldBe "Bolivia"
      countryService.getCountryName("BQ") shouldBe "Bonaire, Sint Eustatius and Saba"
      countryService.getCountryName("BR") shouldBe "Brazil"
      countryService.getCountryName("BS") shouldBe "The Bahamas"
      countryService.getCountryName("BT") shouldBe "Bhutan"
      countryService.getCountryName("BV") shouldBe "Bouvet Island"
      countryService.getCountryName("BW") shouldBe "Botswana"
      countryService.getCountryName("BY") shouldBe "Belarus"
      countryService.getCountryName("BZ") shouldBe "Belize"
      countryService.getCountryName("CA") shouldBe "Canada"
      countryService.getCountryName("CC") shouldBe "Cocos (Keeling) Islands (the)"
      countryService.getCountryName("CD") shouldBe "Congo (Democratic Republic)"
      countryService.getCountryName("CF") shouldBe "Central African Republic"
      countryService.getCountryName("CG") shouldBe "Congo"
      countryService.getCountryName("CH") shouldBe "Switzerland"
      countryService.getCountryName("CI") shouldBe "Ivory Coast"
      countryService.getCountryName("CK") shouldBe "Cook Islands (the)"
      countryService.getCountryName("CL") shouldBe "Chile"
      countryService.getCountryName("CM") shouldBe "Cameroon"
      countryService.getCountryName("CN") shouldBe "China"
      countryService.getCountryName("CO") shouldBe "Colombia"
      countryService.getCountryName("CR") shouldBe "Costa Rica"
      countryService.getCountryName("CS") shouldBe "CS"
      countryService.getCountryName("CU") shouldBe "Cuba"
      countryService.getCountryName("CV") shouldBe "Cape Verde"
      countryService.getCountryName("CW") shouldBe "Curaçao"
      countryService.getCountryName("CX") shouldBe "Christmas Island"
      countryService.getCountryName("CY") shouldBe "Cyprus"
      countryService.getCountryName("CZ") shouldBe "Czechia"
      countryService.getCountryName("DE") shouldBe "Germany"
      countryService.getCountryName("DJ") shouldBe "Djibouti"
      countryService.getCountryName("DK") shouldBe "Denmark"
      countryService.getCountryName("DM") shouldBe "Dominica"
      countryService.getCountryName("DO") shouldBe "Dominican Republic"
      countryService.getCountryName("DZ") shouldBe "Algeria"
      countryService.getCountryName("EC") shouldBe "Ecuador"
      countryService.getCountryName("EE") shouldBe "Estonia"
      countryService.getCountryName("EG") shouldBe "Egypt"
      countryService.getCountryName("EH") shouldBe "Western Sahara"
      countryService.getCountryName("ER") shouldBe "Eritrea"
      countryService.getCountryName("ES") shouldBe "Spain"
      countryService.getCountryName("ET") shouldBe "Ethiopia"
      countryService.getCountryName("EU") shouldBe "European Union"
      countryService.getCountryName("FI") shouldBe "Finland"
      countryService.getCountryName("FJ") shouldBe "Fiji"
      countryService.getCountryName("FK") shouldBe "Falkland Islands"
      countryService.getCountryName("FM") shouldBe "Micronesia"
      countryService.getCountryName("FO") shouldBe "Faroe Islands (the)"
      countryService.getCountryName("FR") shouldBe "France"
      countryService.getCountryName("GA") shouldBe "Gabon"
      countryService.getCountryName("GB") shouldBe "United Kingdom"
      countryService.getCountryName("GD") shouldBe "Grenada"
      countryService.getCountryName("GE") shouldBe "Georgia"
      countryService.getCountryName("GF") shouldBe "French Guiana"
      countryService.getCountryName("GG") shouldBe "Guernsey, Alderney, Sark"
      countryService.getCountryName("GH") shouldBe "Ghana"
      countryService.getCountryName("GI") shouldBe "Gibraltar"
      countryService.getCountryName("GL") shouldBe "Greenland"
      countryService.getCountryName("GM") shouldBe "The Gambia"
      countryService.getCountryName("GN") shouldBe "Guinea"
      countryService.getCountryName("GP") shouldBe "Guadeloupe"
      countryService.getCountryName("GQ") shouldBe "Equatorial Guinea"
      countryService.getCountryName("GR") shouldBe "Greece"
      countryService.getCountryName("GS") shouldBe "South Georgia and South Sandwich Islands"
      countryService.getCountryName("GT") shouldBe "Guatemala"
      countryService.getCountryName("GU") shouldBe "Guam"
      countryService.getCountryName("GW") shouldBe "Guinea-Bissau"
      countryService.getCountryName("GY") shouldBe "Guyana"
      countryService.getCountryName("HK") shouldBe "Hong Kong"
      countryService.getCountryName("HM") shouldBe "Heard Island and McDonald Islands"
      countryService.getCountryName("HN") shouldBe "Honduras"
      countryService.getCountryName("HR") shouldBe "Croatia"
      countryService.getCountryName("HT") shouldBe "Haiti"
      countryService.getCountryName("HU") shouldBe "Hungary"
      countryService.getCountryName("ID") shouldBe "Indonesia"
      countryService.getCountryName("IE") shouldBe "Ireland"
      countryService.getCountryName("IL") shouldBe "Israel"
      countryService.getCountryName("IM") shouldBe "Isle of Man"
      countryService.getCountryName("IN") shouldBe "India"
      countryService.getCountryName("IO") shouldBe "British Indian Ocean Territory"
      countryService.getCountryName("IQ") shouldBe "Iraq"
      countryService.getCountryName("IR") shouldBe "Iran"
      countryService.getCountryName("IS") shouldBe "Iceland"
      countryService.getCountryName("IT") shouldBe "Italy"
      countryService.getCountryName("JE") shouldBe "Jersey"
      countryService.getCountryName("JM") shouldBe "Jamaica"
      countryService.getCountryName("JO") shouldBe "Jordan"
      countryService.getCountryName("JP") shouldBe "Japan"
      countryService.getCountryName("KE") shouldBe "Kenya"
      countryService.getCountryName("KG") shouldBe "Kyrgyzstan"
      countryService.getCountryName("KH") shouldBe "Cambodia"
      countryService.getCountryName("KI") shouldBe "Kiribati"
      countryService.getCountryName("KM") shouldBe "Comoros"
      countryService.getCountryName("KN") shouldBe "St Kitts and Nevis"
      countryService.getCountryName("KP") shouldBe "North Korea"
      countryService.getCountryName("KR") shouldBe "South Korea"
      countryService.getCountryName("KW") shouldBe "Kuwait"
      countryService.getCountryName("KY") shouldBe "Cayman Islands"
      countryService.getCountryName("KZ") shouldBe "Kazakhstan"
      countryService.getCountryName("LA") shouldBe "Laos"
      countryService.getCountryName("LB") shouldBe "Lebanon"
      countryService.getCountryName("LC") shouldBe "St Lucia"
      countryService.getCountryName("LI") shouldBe "Liechtenstein"
      countryService.getCountryName("LK") shouldBe "Sri Lanka"
      countryService.getCountryName("LR") shouldBe "Liberia"
      countryService.getCountryName("LS") shouldBe "Lesotho"
      countryService.getCountryName("LT") shouldBe "Lithuania"
      countryService.getCountryName("LU") shouldBe "Luxembourg"
      countryService.getCountryName("LV") shouldBe "Latvia"
      countryService.getCountryName("LY") shouldBe "Libya"
      countryService.getCountryName("MA") shouldBe "Morocco"
      countryService.getCountryName("MC") shouldBe "Monaco"
      countryService.getCountryName("MD") shouldBe "Moldova"
      countryService.getCountryName("ME") shouldBe "Montenegro"
      countryService.getCountryName("MF") shouldBe "Saint Martin (French part)"
      countryService.getCountryName("MG") shouldBe "Madagascar"
      countryService.getCountryName("MH") shouldBe "Marshall Islands"
      countryService.getCountryName("MK") shouldBe "North Macedonia"
      countryService.getCountryName("ML") shouldBe "Mali"
      countryService.getCountryName("MM") shouldBe "Myanmar (Burma)"
      countryService.getCountryName("MN") shouldBe "Mongolia"
      countryService.getCountryName("MO") shouldBe "Macao"
      countryService.getCountryName("MP") shouldBe "Northern Mariana Islands (the)"
      countryService.getCountryName("MQ") shouldBe "Martinique"
      countryService.getCountryName("MR") shouldBe "Mauritania"
      countryService.getCountryName("MS") shouldBe "Montserrat"
      countryService.getCountryName("MT") shouldBe "Malta"
      countryService.getCountryName("MU") shouldBe "Mauritius"
      countryService.getCountryName("MV") shouldBe "Maldives"
      countryService.getCountryName("MW") shouldBe "Malawi"
      countryService.getCountryName("MX") shouldBe "Mexico"
      countryService.getCountryName("MY") shouldBe "Malaysia"
      countryService.getCountryName("MZ") shouldBe "Mozambique"
      countryService.getCountryName("NA") shouldBe "Namibia"
      countryService.getCountryName("NC") shouldBe "New Caledonia"
      countryService.getCountryName("NE") shouldBe "Niger"
      countryService.getCountryName("NF") shouldBe "Norfolk Island"
      countryService.getCountryName("NG") shouldBe "Nigeria"
      countryService.getCountryName("NI") shouldBe "Nicaragua"
      countryService.getCountryName("NL") shouldBe "Netherlands"
      countryService.getCountryName("NO") shouldBe "Norway"
      countryService.getCountryName("NP") shouldBe "Nepal"
      countryService.getCountryName("NR") shouldBe "Nauru"
      countryService.getCountryName("NT") shouldBe "Neutral Zone"
      countryService.getCountryName("NU") shouldBe "Niue"
      countryService.getCountryName("NZ") shouldBe "New Zealand"
      countryService.getCountryName("OM") shouldBe "Oman"
      countryService.getCountryName("PA") shouldBe "Panama"
      countryService.getCountryName("PE") shouldBe "Peru"
      countryService.getCountryName("PF") shouldBe "French Polynesia"
      countryService.getCountryName("PG") shouldBe "Papua New Guinea"
      countryService.getCountryName("PH") shouldBe "Philippines"
      countryService.getCountryName("PK") shouldBe "Pakistan"
      countryService.getCountryName("PL") shouldBe "Poland"
      countryService.getCountryName("PM") shouldBe "Saint Pierre and Miquelon"
      countryService.getCountryName("PN") shouldBe "Pitcairn, Henderson, Ducie and Oeno Islands"
      countryService.getCountryName("PR") shouldBe "Puerto Rico"
      countryService.getCountryName("PS") shouldBe "Palestine, State of"
      countryService.getCountryName("PT") shouldBe "Portugal"
      countryService.getCountryName("PW") shouldBe "Palau"
      countryService.getCountryName("PY") shouldBe "Paraguay"
      countryService.getCountryName("QA") shouldBe "Qatar"
      countryService.getCountryName("RE") shouldBe "Réunion"
      countryService.getCountryName("RO") shouldBe "Romania"
      countryService.getCountryName("RS") shouldBe "Serbia"
      countryService.getCountryName("RU") shouldBe "Russia"
      countryService.getCountryName("RW") shouldBe "Rwanda"
      countryService.getCountryName("SA") shouldBe "Saudi Arabia"
      countryService.getCountryName("SB") shouldBe "Solomon Islands"
      countryService.getCountryName("SC") shouldBe "Seychelles"
      countryService.getCountryName("SD") shouldBe "Sudan"
      countryService.getCountryName("SE") shouldBe "Sweden"
      countryService.getCountryName("SG") shouldBe "Singapore"
      countryService.getCountryName("SH") shouldBe "St Helena, Ascension and Tristan da Cunha"
      countryService.getCountryName("SI") shouldBe "Slovenia"
      countryService.getCountryName("SJ") shouldBe "Svalbard and Jan Mayen"
      countryService.getCountryName("SK") shouldBe "Slovakia"
      countryService.getCountryName("SL") shouldBe "Sierra Leone"
      countryService.getCountryName("SM") shouldBe "San Marino"
      countryService.getCountryName("SN") shouldBe "Senegal"
      countryService.getCountryName("SO") shouldBe "Somalia"
      countryService.getCountryName("SR") shouldBe "Suriname"
      countryService.getCountryName("SS") shouldBe "South Sudan"
      countryService.getCountryName("ST") shouldBe "Sao Tome and Principe"
      countryService.getCountryName("SV") shouldBe "El Salvador"
      countryService.getCountryName("SX") shouldBe "Sint Maarten (Dutch part)"
      countryService.getCountryName("SY") shouldBe "Syria"
      countryService.getCountryName("SZ") shouldBe "Eswatini"
      countryService.getCountryName("TC") shouldBe "Turks and Caicos Islands"
      countryService.getCountryName("TD") shouldBe "Chad"
      countryService.getCountryName("TF") shouldBe "French Southern Territories (the)"
      countryService.getCountryName("TG") shouldBe "Togo"
      countryService.getCountryName("TH") shouldBe "Thailand"
      countryService.getCountryName("TJ") shouldBe "Tajikistan"
      countryService.getCountryName("TK") shouldBe "Tokelau"
      countryService.getCountryName("TL") shouldBe "Timor-Leste"
      countryService.getCountryName("TM") shouldBe "Turkmenistan"
      countryService.getCountryName("TN") shouldBe "Tunisia"
      countryService.getCountryName("TO") shouldBe "Tonga"
      countryService.getCountryName("TP") shouldBe "East Timor"
      countryService.getCountryName("TR") shouldBe "Turkey"
      countryService.getCountryName("TT") shouldBe "Trinidad and Tobago"
      countryService.getCountryName("TV") shouldBe "Tuvalu"
      countryService.getCountryName("TW") shouldBe "Taiwan (Province of China)"
      countryService.getCountryName("TZ") shouldBe "Tanzania"
      countryService.getCountryName("UA") shouldBe "Ukraine"
      countryService.getCountryName("UG") shouldBe "Uganda"
      countryService.getCountryName("UM") shouldBe "United States Minor Outlying Islands (the)"
      countryService.getCountryName("UN") shouldBe "United Nations"
      countryService.getCountryName("US") shouldBe "United States"
      countryService.getCountryName("UY") shouldBe "Uruguay"
      countryService.getCountryName("UZ") shouldBe "Uzbekistan"
      countryService.getCountryName("VA") shouldBe "Vatican City"
      countryService.getCountryName("VC") shouldBe "St Vincent"
      countryService.getCountryName("VE") shouldBe "Venezuela (Bolivarian Republic of)"
      countryService.getCountryName("VG") shouldBe "British Virgin Islands"
      countryService.getCountryName("VI") shouldBe "Virgin Islands (U.S.)"
      countryService.getCountryName("VN") shouldBe "Vietnam"
      countryService.getCountryName("VU") shouldBe "Vanuatu"
      countryService.getCountryName("WF") shouldBe "Wallis and Futuna"
      countryService.getCountryName("WS") shouldBe "Samoa"
      countryService.getCountryName("YE") shouldBe "Yemen"
      countryService.getCountryName("YT") shouldBe "Mayotte"
      countryService.getCountryName("ZA") shouldBe "South Africa"
      countryService.getCountryName("ZM") shouldBe "Zambia"
      countryService.getCountryName("ZW") shouldBe "Zimbabwe"

    }
  }
}
