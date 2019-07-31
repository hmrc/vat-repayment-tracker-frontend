import com.typesafe.sbt.SbtScalariform.ScalariformKeys
import scalariform.formatter.preferences._
import uk.gov.hmrc.DefaultBuildSettings.{defaultSettings, integrationTestSettings, scalaSettings}
import uk.gov.hmrc.SbtArtifactory
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin.publishingSettings
import wartremover.{Wart, wartremoverErrors, wartremoverExcluded, wartremoverWarnings}

val appName = "vat-repayment-tracker-frontend"

lazy val scalariformSettings = {
  // description of options found here -> https://github.com/scala-ide/scalariform
  ScalariformKeys.preferences := ScalariformKeys.preferences.value
    .setPreference(AlignArguments, true)
    .setPreference(AlignParameters, true)
    .setPreference(AlignSingleLineCaseStatements, true)
    .setPreference(AllowParamGroupsOnNewlines, true)
    .setPreference(CompactControlReadability, false)
    .setPreference(CompactStringConcatenation, false)
    .setPreference(DanglingCloseParenthesis, Preserve)
    .setPreference(DoubleIndentConstructorArguments, true)
    .setPreference(DoubleIndentMethodDeclaration, true)
    .setPreference(FirstArgumentOnNewline, Preserve)
    .setPreference(FirstParameterOnNewline, Preserve)
    .setPreference(FormatXml, true)
    .setPreference(IndentLocalDefs, true)
    .setPreference(IndentPackageBlocks, true)
    .setPreference(IndentSpaces, 2)
    .setPreference(IndentWithTabs, false)
    .setPreference(MultilineScaladocCommentsStartOnFirstLine, false)
    .setPreference(NewlineAtEndOfFile, true)
    .setPreference(PlaceScaladocAsterisksBeneathSecondAsterisk, false)
    .setPreference(PreserveSpaceBeforeArguments, true)
    .setPreference(RewriteArrowSymbols, false)
    .setPreference(SpaceBeforeColon, false)
    .setPreference(SpaceBeforeContextColon, false)
    .setPreference(SpaceInsideBrackets, false)
    .setPreference(SpaceInsideParentheses, false)
    .setPreference(SpacesAroundMultiImports, false)
    .setPreference(SpacesWithinPatternBinders, true)
}
lazy val wartRemoverWarning = {
  val warningWarts = Seq(
    Wart.JavaSerializable,
    Wart.StringPlusAny,
    Wart.AsInstanceOf,
    Wart.IsInstanceOf,
    Wart.Any
  )
  wartremoverWarnings in(Compile, compile) ++= warningWarts
}
lazy val wartRemoverError = {
  // Error
  val errorWarts = Seq(
    Wart.ArrayEquals,
    Wart.AnyVal,
    Wart.EitherProjectionPartial,
    Wart.Enumeration,
    Wart.ExplicitImplicitTypes,
    Wart.FinalVal,
    Wart.JavaConversions,
    Wart.JavaSerializable,
    Wart.LeakingSealed,
    Wart.MutableDataStructures,
    Wart.Null,
    Wart.OptionPartial,
    Wart.Recursion,
    Wart.Return,
    Wart.TraversableOps,
    Wart.TryPartial,
    Wart.Var,
    Wart.While)

  wartremoverErrors in(Compile, compile) ++= errorWarts
}
lazy val scoverageSettings = {
  import scoverage.ScoverageKeys
  Seq(
    // Semicolon-separated list of regexs matching classes to exclude
    ScoverageKeys.coverageExcludedPackages := "<empty>;.*BuildInfo.*;Reverse.*;app.Routes.*;prod.*;testOnlyDoNotUseInAppConf.*;forms.*;config.*",
    ScoverageKeys.coverageExcludedFiles := ".*microserviceGlobal.*;.*microserviceWiring.*",
    ScoverageKeys.coverageMinimum := 10,
    ScoverageKeys.coverageFailOnMinimum := false,
    ScoverageKeys.coverageHighlighting := true
  )
}

lazy val microservice = Project(appName, file("."))
  .enablePlugins(play.sbt.PlayScala, SbtAutoBuildPlugin, SbtGitVersioning, SbtDistributablesPlugin, SbtArtifactory)
  .settings(
    scalaVersion                                  :=  "2.11.11",
    resolvers                                     ++= Seq(Resolver.bintrayRepo("hmrc", "releases"), Resolver.jcenterRepo),
    libraryDependencies                           ++= AppDependencies.compile ++ AppDependencies.test,
    retrieveManaged                               :=  true,
    routesGenerator                               :=  InjectedRoutesGenerator,
    evictionWarningOptions     in update          :=  EvictionWarningOptions.default.withWarnScalaVersionEviction(false)
  )
  .settings(majorVersion := 0)
  .settings(scalariformSettings: _*)
  .settings(wartRemoverError)
  .settings(wartRemoverWarning)
  .settings(wartremoverErrors in(Test, compile) --= Seq(Wart.Any, Wart.Equals, Wart.Null, Wart.NonUnitStatements, Wart.PublicInference))
  .settings(wartremoverExcluded ++=
    routes.in(Compile).value ++
      (baseDirectory.value / "it").get ++
      (baseDirectory.value / "test").get ++
      Seq(sourceManaged.value / "main" / "sbt-buildinfo" / "BuildInfo.scala"))
  .settings(scoverageSettings: _*)
  .settings(publishingSettings: _*)
  .settings(PlayKeys.playDefaultPort := 9863)
  .settings(scalaSettings: _*)
  .settings(defaultSettings(): _*)
  .settings(integrationTestSettings())
  .configs(IntegrationTest)
