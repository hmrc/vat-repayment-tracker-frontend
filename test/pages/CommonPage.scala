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

package pages

import java.io.{FileInputStream, FileOutputStream}
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import org.openqa.selenium.{By, OutputType, TakesScreenshot, WebDriver}
import org.scalatest.Assertion
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatestplus.selenium.WebBrowser
import play.api.Logger
import support.RichMatchers

import scala.util.Random

trait CommonPage extends WebBrowser with RichMatchers:

  private val logger = Logger(this.getClass)

  given PatienceConfig = PatienceConfig(scaled(Span(1, Seconds)), scaled(Span(200, Millis)))

  def assertTechnicalErrorDisplayed(path: String)(using WebDriver): Assertion = probing { _ =>
    currentPath shouldBe path
    pageTitle shouldBe "Sorry, we are experiencing technical difficulties - 500"
  }

  def currentPath(using webDriver: WebDriver): String =
    val url = new java.net.URL(webDriver.getCurrentUrl)
    url.getPath

  def clickFinish()(using WebDriver): Unit = probing(_.findElement(By.id("finish")).click())

  def getPageHeader(using WebDriver): String = probing(_.findElement(By.className("heading-large")).getText)

  def clickContinue()(using WebDriver): Unit = probing(_.findElement(By.id("next")).click())

  def clickOnEnglishLink()(using WebDriver): Unit = probing(
    _.findElement(By.partialLinkText("English")).click()
  )

  def clickOnWelshLink()(using WebDriver): Unit = probing(
    _.findElement(By.partialLinkText("Cymraeg")).click()
  )

  def clickViewProgress()(using WebDriver): Unit =
    probing(
      _.findElement(By.xpath(s"/html/body/div/main/div/article/div[3]/div/div/section/table/tbody/tr[1]/td[4]/a"))
        .click()
    )

  /** Probing tries to run `probingF` until it succeeds. If it doesn't it: reports what was the page source and dumps
    * page screenshot and fails assertion
    */
  def probing[A](probingF: WebDriver => A)(using webDriver: WebDriver): A = eventually(probingF(webDriver)).withClue {
    val maybeDumpedFile = takeADump()
    s"""
       |${maybeDumpedFile.map(uri => s"Screenshot recorded in $uri").getOrElse("Sorry, no screenshot recorded")}
       |page source was:
       |${webDriver.getPageSource}
       |""".stripMargin
  }

  /** If can it will dump PNG image showing current page in browser.
    *
    * @return
    *   some uri of the dumped file or none
    */
  def takeADump()(using webDriver: WebDriver): Option[String] =
    // original `capture to` relies on side effecting `targetDir`
    // this is safer implementation
    val targetDir = "target/ittests-screenshots"
    val fileName  =
      val addon = List.fill(5)(Random.nextPrintableChar()).mkString
      s"${this.getClass.getSimpleName}-$addon.png"
    webDriver match
      case takesScreenshot: TakesScreenshot =>
        val tmpFile = takesScreenshot.getScreenshotAs(OutputType.FILE)
        val outFile = new java.io.File(targetDir, fileName)
        new FileOutputStream(outFile).getChannel
          .transferFrom(
            new FileInputStream(tmpFile).getChannel,
            0,
            Long.MaxValue
          )
        Some(outFile.toURI.toString)
      case _                                =>
        logger.warn(s"Could not take screen shot: $fileName")
        None

  def clickBack()(using WebDriver): Unit = probing(_.findElement(By.className("link-back")).click())

  def readBackButtonUrl()(using WebDriver): String = probing(
    _.findElement(By.className("govuk-back-link"))
      .getDomAttribute("href")
  )

  def readMainMessage(using WebDriver): String = probing(_.findElement(By.id("main-message")).getText)

  def readWarning(using WebDriver): String = probing(
    _.findElement(By.className("govuk-warning-text__text")).getText
  )

  def readAccName(using WebDriver): String = probing(_.findElement(By.id("acc-name")).getText)

  def readAccSortCode(using WebDriver): String = probing(_.findElement(By.id("acc-sort-code")).getText)

  def readAccNumber(using WebDriver): String = probing(_.findElement(By.id("acc-number")).getText)

  def readBuildingSocietyNumber(using WebDriver): String = probing(
    _.findElement(By.id("building-society-number")).getText
  )

  def readTitle(using webDriver: WebDriver): String = webDriver.getTitle

  def assertErrorSummaryIsShown()(using WebDriver): Assertion =
    assert(globalErrors.isDefined)

  def globalErrors(using WebDriver): Option[Element] = id("error-summary-display").findElement

  def getByStringIdOption(id: String)(using webDriver: WebDriver): Option[String] = try
    Some(webDriver.findElement(By.id(id)).getText)
  catch {
    case _: org.openqa.selenium.NoSuchElementException => None
  }

  def getTextByCss(css: String)(using webDriver: WebDriver): Option[String] = try
    Some(webDriver.findElement(By.cssSelector(css)).getText)
  catch {
    case _: org.openqa.selenium.NoSuchElementException => None
  }

  def containsText(text: String)(using WebDriver): Boolean =
    probing(_.getPageSource.contains(text))

  def cssCount(css: String)(using WebDriver): Int =
    probing(_.findElements(By.cssSelector(css))).size()

  def readMain()(using WebDriver): String = xpath("""//*[@id="content"]""").element.text

  def assertContentMatchesExpectedLines(expectedLines: List[String])(using WebDriver): Unit =
    val content = readMain().stripSpaces().replaceAll("\n", " ")
    expectedLines.foreach: expectedLine =>
      withClue(s"\nThe page content should include '$expectedLine'") {
        content should include(expectedLine)
      }

  def hasTextHyperLinkedTo(text: String, link: String)(using WebDriver): Assertion =
    probing(
      _.findElement(By.partialLinkText(text))
        .getAttribute("href")
    ) shouldBe link

  def assertBackButtonRedirectsTo(url: String)(using WebDriver): Assertion =
    readBackButtonUrl() shouldBe url

  def idPresent(id: String)(using webDriver: WebDriver): Boolean = try {
    webDriver.findElement(By.id(id))
    true
  } catch {
    case _: Throwable => false
  }

  def cssPresent(css: String)(using webDriver: WebDriver): Boolean = try {
    webDriver.findElement(By.cssSelector(css))
    true
  } catch {
    case _: Throwable => false
  }

  def formatDate(date: LocalDate): String =
    val pattern1 = DateTimeFormatter.ofPattern("dd MMM yyyy")
    date.format(pattern1)

  extension (s: String)
    /** Transforms string so it's easier it to compare. It also replaces `unchecked`
      */
    def stripSpaces(): String = s
      .replaceAll(
        "unchecked",
        ""
      )                                 // when you run tests from intellij webdriver.getText adds extra 'unchecked' around selection
      .replaceAll("[^\\S\\r\\n]+", " ") // replace many consecutive white-spaces (but not new lines) with one space
      .replaceAll("[\r\n]+", "\n")      // replace many consecutive new lines with one new line
      .split("\n")
      .map(_.trim)                      // trim each line
      .filterNot(_ == "")               // remove any empty lines
      .mkString("\n")
