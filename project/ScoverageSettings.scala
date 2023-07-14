import sbt.Keys.parallelExecution
import sbt._
import scoverage.ScoverageKeys

object ScoverageSettings {
  def apply() = Seq( // Semicolon-separated list of regexes matching classes to exclude
    ScoverageKeys.coverageExcludedPackages := "<empty>;.*BuildInfo.*;Reverse.*;app.Routes.*;prod.*;testOnlyDoNotUseInAppConf.*;forms.*;config.*",
    ScoverageKeys.coverageExcludedFiles := Seq(
      "" +
        "<empty>",
      ".*microserviceGlobal.*",
      ".*microserviceWiring.*",
      ".*Link.*",
      ".*Language.*",
      ".*LanuageSwitchController.*",
      ".*language_selection.*"
    ).mkString(";"),
    ScoverageKeys.coverageMinimumStmtTotal := 80,  //should be a lot higher but we are where we are
    ScoverageKeys.coverageFailOnMinimum := true,
    ScoverageKeys.coverageHighlighting := true,
    (Test/ parallelExecution) := false
  )
}
