import play.core.PlayVersion.current
import sbt._

object AppDependencies {

  val compile = Seq(
    "uk.gov.hmrc" %% "govuk-template"      % "5.69.0-play-28",
    "uk.gov.hmrc" %% "play-ui"             % "9.6.0-play-28",
    "uk.gov.hmrc" %% "bootstrap-frontend-play-28"   % "5.6.0",
    "com.beachape" %% "enumeratum"         % "1.6.0",
    "com.beachape" %% "enumeratum-play"    % "1.6.0"
  )

  val test = Seq(
    "org.scalatest" %% "scalatest" % "3.0.8" % Test,
    "org.jsoup" % "jsoup" % "1.10.2" % Test,
    "com.typesafe.play" %% "play-test" % current % Test,
    "org.pegdown" % "pegdown" % "1.6.0" % Test,
    "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,
    "com.github.tomakehurst" % "wiremock-jre8" % "2.21.0" % Test
  )

}
