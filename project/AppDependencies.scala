import sbt.*

object AppDependencies {

  val bootstrapVersion = "10.6.0"
  val enumeratumVersion = "1.9.5"

  val compile: Seq[ModuleID] = Seq(
    // FORMAT: OFF
    "uk.gov.hmrc"   %% "play-frontend-hmrc-play-30" % "12.31.0",
    "uk.gov.hmrc"   %% "bootstrap-frontend-play-30" % bootstrapVersion,
    "com.beachape"  %% "enumeratum"                 % enumeratumVersion,
    "com.beachape"  %% "enumeratum-play"            % enumeratumVersion,
    "org.typelevel" %% "cats-core"                  % "2.13.0"
    // FORMAT: ON
  )

  val test: Seq[ModuleID] = Seq(
    // FORMAT: OFF
    "uk.gov.hmrc"            %% "bootstrap-test-play-30" % bootstrapVersion,
    "org.scalatest"          %% "scalatest"              % "3.2.19",
    "org.jsoup"              % "jsoup"                   % "1.22.1",
    "org.scalatestplus.play" %% "scalatestplus-play"     % "7.0.2",
    "org.wiremock"           % "wiremock-standalone"     % "3.13.2"
    // FORMAT: ON
  ).map(_ % Test)

}
