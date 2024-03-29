import play.core.PlayVersion.current
import sbt._

object AppDependencies {

  val bootstrapVersion = "8.4.0"

  val compile = Seq(
    // FORMAT: OFF
    "uk.gov.hmrc"   %% "play-frontend-hmrc-play-30" % "8.5.0",
    "uk.gov.hmrc"   %% "bootstrap-frontend-play-30" % bootstrapVersion,
    "com.beachape"  %% "enumeratum"                 % "1.7.3",
    "com.beachape"  %% "enumeratum-play"            % "1.8.0",
    "org.typelevel" %% "cats-core"                  % "2.10.0"
    // FORMAT: ON
  )

  val test = Seq(
    // FORMAT: OFF
    "uk.gov.hmrc"            %% "bootstrap-test-play-30" % bootstrapVersion,
    "org.scalatest"          %% "scalatest"              % "3.2.18",
    "org.jsoup"              % "jsoup"                   % "1.17.2",
    "org.playframework"      %% "play-test"              % current,
    "org.scalatestplus.play" %% "scalatestplus-play"     % "7.0.1",
    "org.wiremock"           % "wiremock-standalone"     % "3.4.2"
    // FORMAT: ON
  ).map(_ % Test)

}
