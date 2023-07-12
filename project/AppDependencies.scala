import play.core.PlayVersion.current
import sbt._

object AppDependencies {

  val bootstrapVersion = "7.19.0"

  val compile = Seq(
    "uk.gov.hmrc" %% "play-frontend-hmrc" % "7.14.0-play-28",
    "uk.gov.hmrc" %% "bootstrap-frontend-play-28" % bootstrapVersion,
    "com.beachape" %% "enumeratum"         % "1.6.0",
    "com.beachape" %% "enumeratum-play"    % "1.6.0"
  )

  val test = Seq(
    "org.scalatest" %% "scalatest" % "3.2.15" % Test,
    "org.jsoup" % "jsoup" % "1.10.2" % Test,
    "com.typesafe.play" %% "play-test" % current % Test,
    "org.pegdown" % "pegdown" % "1.6.0" % Test,
    "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test,
    "com.github.tomakehurst" % "wiremock-jre8" % "2.21.0" % Test,
    "com.vladsch.flexmark" % "flexmark-all" % "0.62.2" % Test
  )

}
