import play.core.PlayVersion.current
import sbt._

object AppDependencies {

  val bootstrapVersion = "7.20.0"

  val compile = Seq(
    // FORMAT: OFF
    "uk.gov.hmrc"  %% "play-frontend-hmrc"         % "7.15.0-play-28",
    "uk.gov.hmrc"  %% "bootstrap-frontend-play-28" % bootstrapVersion,
    "com.beachape" %% "enumeratum"                 % "1.7.3",
    "com.beachape" %% "enumeratum-play"            % "1.7.3"
    // FORMAT: ON
  )

  val test = Seq(
    // FORMAT: OFF
    "uk.gov.hmrc"            %% "bootstrap-test-play-28" % bootstrapVersion,
    "org.scalatest"          %% "scalatest"              % "3.2.16",
    "org.jsoup"              % "jsoup"                   % "1.16.1",
    "com.typesafe.play"      %% "play-test"              % current,
    "org.pegdown"            % "pegdown"                 % "1.6.0" ,
    "org.scalatestplus.play" %% "scalatestplus-play"     % "5.1.0",
    "com.github.tomakehurst" % "wiremock-standalone"     % "2.27.2",
    "com.vladsch.flexmark"   % "flexmark-all"            % "0.64.8"
    // FORMAT: ON
  ).map(_ % Test)

}
