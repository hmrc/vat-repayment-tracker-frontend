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

package formaters

import support.ItSpec

class DesFormatterSpec extends ItSpec {

  val desFormmatter = injector.instanceOf[DesFormatter]

  val period1 = Seq("A", "B", "C")
  val period2 = Seq("D", "E", "F")
  val period3 = Seq("G", "H", "I")
  val period4 = Seq("J", "K", "L")

  period1.foreach { month =>

    s"Period1 Month is ${month}" in {

      val period: String = s"18A${month}"

      desFormmatter.formatPeriodKey(period) shouldBe "1 January to 31 March 2018"

    }
  }

  period2.foreach { month =>

    s"Period2 Month is ${month}" in {

      val period: String = s"18A${month}"

      desFormmatter.formatPeriodKey(period) shouldBe "1 April to 30 June 2018"

    }
  }

  period3.foreach { month =>

    s"Period3 Month is ${month}" in {

      val period: String = s"18A${month}"

      desFormmatter.formatPeriodKey(period) shouldBe "1 July to 30 September 2018"

    }
  }

  period4.foreach { month =>

    s"Period4 Month is ${month}" in {

      val period: String = s"18A${month}"

      desFormmatter.formatPeriodKey(period) shouldBe "1 October to 31 December 2018"

    }
  }

}
