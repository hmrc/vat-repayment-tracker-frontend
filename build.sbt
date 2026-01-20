import uk.gov.hmrc.DefaultBuildSettings.{defaultSettings, scalaSettings}

val appName = "vat-repayment-tracker-frontend"

lazy val microservice = Project(appName, file("."))
  .enablePlugins(play.sbt.PlayScala, SbtDistributablesPlugin)
  .disablePlugins(JUnitXmlReportPlugin)
  .settings(
    scalaVersion := "3.5.2",
    libraryDependencies ++= AppDependencies.compile ++ AppDependencies.test,
    libraryDependencySchemes += "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always,
    retrieveManaged := false,
    routesGenerator := InjectedRoutesGenerator,
    (Compile / doc / sources) := Seq.empty
  )
  .settings(majorVersion := 1)
  .settings(scalafmtOnCompile := true)
  .settings(ScoverageSettings())
  .settings(SbtUpdatesSettings.sbtUpdatesSettings)
  .settings(
    WartRemoverSettings.wartRemoverError,
    WartRemoverSettings.wartRemoverWarning,
    (Test/ compile / wartremoverErrors) --= Seq(Wart.Any, Wart.Equals, Wart.Null, Wart.NonUnitStatements, Wart.PublicInference),
    wartremoverExcluded ++=
      (Compile / routes).value ++
        (baseDirectory.value / "test").get ++
        Seq(sourceManaged.value / "main" / "sbt-buildinfo" / "BuildInfo.scala")
  )
  .settings(PlayKeys.playDefaultPort := 9863)
  .settings(scalaSettings *)
  .settings(defaultSettings() *)
  .settings(
    routesImport ++= Seq(
      "model._",
      "model.des._"
    ))
  .settings(
    scalacOptions ++= ScalaCompilerFlags.scalaCompilerOptions
  )
  .settings(
    TwirlKeys.templateImports ++= Seq(
      "uk.gov.hmrc.govukfrontend.views.html.components._",
      "uk.gov.hmrc.hmrcfrontend.views.html.components._",
      "uk.gov.hmrc.hmrcfrontend.views.html.helpers._"
    )
  )
