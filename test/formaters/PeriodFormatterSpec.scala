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

package formaters

import play.api.i18n.{Lang, MessagesApi, MessagesImpl, MessagesProvider}
import support.ItSpec

class PeriodFormatterSpec extends ItSpec {

  implicit val messagesProvider: MessagesProvider = {
    MessagesImpl(Lang.defaultLang, injector.instanceOf[MessagesApi])
  }

  val periodFormatter: PeriodFormatter = injector.instanceOf[PeriodFormatter]

  val monthly: Seq[(String, String)] = Seq(
    ("19AA", "1 January to 31 January 2019"),
    ("19AB", "1 February to 28 February 2019"),
    ("20AB", "1 February to 29 February 2020"),
    ("19AC", "1 March to 31 March 2019"),
    ("19AD", "1 April to 30 April 2019"),
    ("19AE", "1 May to 31 May 2019"),
    ("19AF", "1 June to 30 June 2019"),
    ("19AG", "1 July to 31 July 2019"),
    ("19AH", "1 August to 31 August 2019"),
    ("19AI", "1 September to 30 September 2019"),
    ("19AJ", "1 October to 31 October 2019"),
    ("19AK", "1 November to 30 November 2019"),
    ("19AL", "1 December to 31 December 2019")
  )

  val quarterly: Seq[(String, String)] = Seq(
    ("19A4", "1 November to 31 January 2019"),
    ("19B4", "1 December to 28 February 2019"),
    ("20B4", "1 December to 29 February 2020"),
    ("19C1", "1 January to 31 March 2019"),
    ("19A1", "1 February to 30 April 2019"),
    ("19B1", "1 March to 31 May 2019"),
    ("19C2", "1 April to 30 June 2019"),
    ("19A2", "1 May to 31 July 2019"),
    ("19B2", "1 June to 31 August 2019"),
    ("19C3", "1 July to 30 September 2019"),
    ("19A3", "1 August to 31 October 2019"),
    ("19B3", "1 September to 30 November 2019"),
    ("19C4", "1 October to 31 December 2019")
  )

  val yearly: Seq[(String, String)] = Seq(
    ("19YA", "January to December 2019"),
    ("19YB", "February to January 2020"),
    ("20YB", "February to January 2021"),
    ("19YC", "March to February 2020"),
    ("19YD", "April to March 2020"),
    ("19YE", "May to April 2020"),
    ("19YF", "June to May 2020"),
    ("19YG", "July to June 2020"),
    ("19YH", "August to July 2020"),
    ("19YI", "September to August 2020"),
    ("19YJ", "October to September 2020"),
    ("19YK", "November to October 2020"),
    ("19YL", "December to November 2020")
  )

  monthly foreach (period =>

    s"$period monthly check " in {
      periodFormatter.formatPeriodKey(period._1) shouldBe period._2
    }
  )

  quarterly foreach (period =>

    s"$period quarterly check " in {
      periodFormatter.formatPeriodKey(period._1) shouldBe period._2
    }
  )

  yearly foreach (period =>

    s"$period yearly check " in {
      periodFormatter.formatPeriodKey(period._1) shouldBe period._2
    }
  )

}
