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

package pages

import org.openqa.selenium.WebDriver
import org.scalatest.Assertion

object InProgressCompleted extends CommonDetail {

  def containsBAC(result: Boolean)(implicit wd: WebDriver): Assertion = {
    containsText("For faster payment next time") shouldBe result
  }

  def checktabs(implicit wd: WebDriver): Assertion = {

    idPresent("completed-exist") shouldBe true
    idPresent("inprogress-exist") shouldBe true
    idPresent("completed-none") shouldBe false
    idPresent("inprogress-none") shouldBe false

  }

  def checktabsInPast(implicit wd: WebDriver): Assertion = {

    idPresent("completed-exist") shouldBe false
    idPresent("inprogress-exist") shouldBe true
    idPresent("completed-none") shouldBe true
    idPresent("inprogress-none") shouldBe false

  }

}
