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

import java.io.{FileInputStream, FileOutputStream}

import org.openqa.selenium.{By, OutputType, TakesScreenshot, WebDriver}
import org.scalatest.Assertion
import org.scalatest.concurrent.PatienceConfiguration
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatestplus.selenium.WebBrowser
import play.api.Logger
import support.RichMatchers

import scala.collection.immutable.List
import scala.util.Random

trait CommonPage
  extends WebBrowser
    with PatienceConfiguration
    with RichMatchers {

  override implicit val patienceConfig: PatienceConfig = PatienceConfig(scaled(Span(10, Seconds)), scaled(Span(200, Millis)))

  def assertTechnicalErrorDisplayed(path: String)(implicit webDriver: WebDriver): Assertion = probing { _ =>
    currentPath shouldBe path
    pageTitle shouldBe "Sorry, we are experiencing technical difficulties - 500"
  }

  def currentPath(implicit driver: WebDriver): String = {
    val url = new java.net.URL(driver.getCurrentUrl)
    url.getPath
  }

  def clickFinish()(implicit driver: WebDriver): Unit = probing(_.findElement(By.id("finish")).click())

  def getPageHeader(implicit driver: WebDriver): String = probing(_.findElement(By.className("heading-large")).getText)

  def clickContinue()(implicit driver: WebDriver): Unit = probing(_.findElement(By.id("next")).click())

  def clickBack()(implicit driver: WebDriver): Unit = probing(_.findElement(By.className("link-back")).click())

  /**
    * Probing tries to run `probingF` until until it succeeds. If it doesn't it:
    * reports what was the page source
    * and dumps page screenshot
    * and fails assertion
    */
  def probing[A](probingF: WebDriver => A)(implicit driver: WebDriver): A = eventually(probingF(driver)).withClue {
    val maybeDumpedFile = takeADump()
    s"""
       |${maybeDumpedFile.map(uri => s"Screenshot recorded in $uri").getOrElse("Sorry, no screenshot recorded")}
       |page source was:
       |${driver.getPageSource}
       |""".stripMargin
  }

  /**
    * If can it will dump PNG image showing current page in browser.
    *
    * @return some uri of the dumped file or none
    */
  def takeADump()(implicit driver: WebDriver): Option[String] = {
    //original `capture to` relies on side effecting `targetDir`
    //this is safer implementation
    val targetDir = "target/ittests-screenshots"
    val fileName = {
      val addon = List.fill(5)(Random.nextPrintableChar()).mkString
      s"${this.getClass.getSimpleName}-$addon.png"
    }
    driver match {
      case takesScreenshot: TakesScreenshot =>
        val tmpFile = takesScreenshot.getScreenshotAs(OutputType.FILE)
        val outFile = new java.io.File(targetDir, fileName)
        new FileOutputStream(outFile)
          .getChannel
          .transferFrom(
            new FileInputStream(tmpFile).getChannel, 0, Long.MaxValue
          )
        Some(outFile.toURI.toString)
      case _ =>
        Logger.warn(s"Could not take screen shot: $fileName")
        None
    }
  }

  def assertErrorSummaryIsShown()(implicit webDriver: WebDriver): Assertion = {
    globalErrors shouldBe defined
  }

  def globalErrors(implicit driver: WebDriver): Option[Element] = id("error-summary-display").findElement

  def getByStringIdOption(id: String)(implicit driver: WebDriver) = try {
    Some(driver.findElement(By.id(id)).getText)
  } catch {
    case ex: org.openqa.selenium.NoSuchElementException => None
  }

  def containsText(text: String)(implicit driver: WebDriver): Boolean = {
    probing(_.getPageSource.contains(text))
  }

}
