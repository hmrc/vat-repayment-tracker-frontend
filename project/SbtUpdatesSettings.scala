import com.timushev.sbt.updates.UpdatesKeys.dependencyUpdates
import com.timushev.sbt.updates.UpdatesPlugin.autoImport.{dependencyUpdatesFailBuild, dependencyUpdatesFilter, moduleFilterRemoveValue}
import sbt.Keys._
import sbt._

object SbtUpdatesSettings {

  lazy val sbtUpdatesSettings = Seq(
    dependencyUpdatesFailBuild := true,
    (Compile / compile) := ((Compile / compile) dependsOn dependencyUpdates).value,
    dependencyUpdatesFilter -= moduleFilter("org.scala-lang"),
    dependencyUpdatesFilter -= moduleFilter("org.playframework"),
    // locked by version of play
    dependencyUpdatesFilter -= moduleFilter("org.scalatestplus.play", "scalatestplus-play")

  )

}
