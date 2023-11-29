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

package controllers

import org.jsoup.Jsoup
import play.api.test.FakeRequest
import play.api.test.Helpers._
import play.api.mvc.Session
import support.ItSpec

class TimeoutControllerSpec extends ItSpec {

  lazy val controller = injector.instanceOf[TimeoutController]

  "TimeoutController when" - {

    "keeping a session alive should" - {

      "return a 204 (NO CONTENT) response" in {
        val result = controller.keepAliveSession(FakeRequest())
        status(result) shouldBe NO_CONTENT
      }

    }

    "killing a session should" - {

      "clear the session and show the 'delete answers' page" in {
        val result = controller.killSession(FakeRequest().withSession("key" -> "value"))

        status(result) shouldBe OK
        session(result) shouldBe Session()

        val doc = Jsoup.parse(contentAsString(result))
        doc.select("h1").text() shouldBe "For your security, we signed you out"
        doc.select("p.govuk-body").text() shouldBe "We did not save your answers."

      }

    }

  }

}
