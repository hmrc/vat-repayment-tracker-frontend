import sbt.*

object AppDependencies {

  val bootstrapVersion = "9.13.0"

  val compile: Seq[ModuleID] = Seq(
    // FORMAT: OFF
    "uk.gov.hmrc"   %% "play-frontend-hmrc-play-30" % "12.6.0",
    "uk.gov.hmrc"   %% "bootstrap-frontend-play-30" % bootstrapVersion,
    "com.beachape"  %% "enumeratum"                 % "1.9.0",
    "com.beachape"  %% "enumeratum-play"            % "1.9.0",
    "org.typelevel" %% "cats-core"                  % "2.13.0"
    // FORMAT: ON
  )

  val test: Seq[ModuleID] = Seq(
    // FORMAT: OFF
    "uk.gov.hmrc"            %% "bootstrap-test-play-30" % bootstrapVersion,
    "org.scalatest"          %% "scalatest"              % "3.2.19",
    "org.jsoup"              % "jsoup"                   % "1.21.1",
    "org.scalatestplus.play" %% "scalatestplus-play"     % "7.0.1",
    "org.wiremock"           % "wiremock-standalone"     % "3.13.1"
    // FORMAT: ON
  ).map(_ % Test)

}
