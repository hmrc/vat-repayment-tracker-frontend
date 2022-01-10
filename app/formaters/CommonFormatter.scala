/*
 * Copyright 2022 HM Revenue & Customs
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

import java.text.DecimalFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object CommonFormatter {

  private val pattern1 = DateTimeFormatter.ofPattern("dd MMM yyyy")
  def formatDate(date: LocalDate): String = {
    date.format(pattern1)
  }

  private val df = new DecimalFormat("#,##0.00")
  def formatAmount(amount: BigDecimal): String = {

    df.format(amount.abs)
  }

}
