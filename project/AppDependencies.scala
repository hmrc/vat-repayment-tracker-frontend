import play.core.PlayVersion.current
import play.sbt.PlayImport._
import sbt.Keys.libraryDependencies
import sbt._

object AppDependencies {

  val compile = Seq(

    "uk.gov.hmrc"             %% "govuk-template"           % "5.36.0-play-26",
    "uk.gov.hmrc"             %% "play-ui"                  % "7.40.0-play-26",
    "uk.gov.hmrc"             %% "bootstrap-play-26"        % "0.42.0"
  )

  val test = Seq(
    "uk.gov.hmrc"             %% "bootstrap-play-26"        % "0.42.0",
    "org.scalatest"           %% "scalatest"                % "3.0.8",
    "org.jsoup"               %  "jsoup"                    % "1.10.2",
    "com.typesafe.play"       %% "play-test"                % current,        
    "org.pegdown"             %  "pegdown"                  % "1.6.0",            
    "org.scalatestplus.play"  %% "scalatestplus-play"       % "3.1.2",
    "com.github.tomakehurst" % "wiremock-jre8" % "2.21.0"
  )

}
