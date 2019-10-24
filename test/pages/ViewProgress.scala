package pages

import org.openqa.selenium.{By, WebDriver}
import org.scalatest.Assertion

object ViewProgress extends CommonPage {

    def checkAmount(amount: String)(implicit webDriver: WebDriver): Assertion = probing(_.findElement(By.id(s"amount")).getText) shouldBe amount

}
