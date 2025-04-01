import sbt._

object AppDependencies {

  val bootstrapVersion = "9.11.0"

  val compile = Seq(
    // FORMAT: OFF
    "uk.gov.hmrc"   %% "play-frontend-hmrc-play-30" % "12.0.0",
    "uk.gov.hmrc"   %% "bootstrap-frontend-play-30" % bootstrapVersion,
    "com.beachape"  %% "enumeratum"                 % "1.7.6",
    "com.beachape"  %% "enumeratum-play"            % "1.8.2",
    "org.typelevel" %% "cats-core"                  % "2.13.0"
    // FORMAT: ON
  )

  val test = Seq(
    // FORMAT: OFF
    "uk.gov.hmrc"            %% "bootstrap-test-play-30" % bootstrapVersion,
    "org.scalatest"          %% "scalatest"              % "3.2.19",
    "org.jsoup"              % "jsoup"                   % "1.19.1",
    "org.scalatestplus.play" %% "scalatestplus-play"     % "7.0.1",
    "org.wiremock"           % "wiremock-standalone"     % "3.12.1"
    // FORMAT: ON
  ).map(_ % Test)

}
