import play.core.PlayVersion.current
import sbt._

object AppDependencies {

  val compile = Seq(

    "uk.gov.hmrc" %% "govuk-template" % "5.36.0-play-26",
    "uk.gov.hmrc" %% "play-ui" % "8.3.0-play-26",
    "uk.gov.hmrc" %% "bootstrap-play-26" % "1.0.0",
    "uk.gov.hmrc" %% "auth-client" % "2.31.0-play-26",
    "com.beachape" %% "enumeratum" % "1.5.13"
  )

  val test = Seq(
    "uk.gov.hmrc" %% "bootstrap-play-26" % "0.42.0" % Test,
    "org.scalatest" %% "scalatest" % "3.0.8" % Test,
    "org.jsoup" % "jsoup" % "1.10.2" % Test,
    "com.typesafe.play" %% "play-test" % current % Test,
    "org.pegdown" % "pegdown" % "1.6.0" % Test,
    "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,
    "com.github.tomakehurst" % "wiremock-jre8" % "2.21.0" % Test
  )

}
