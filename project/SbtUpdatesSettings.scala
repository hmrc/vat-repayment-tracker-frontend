import com.timushev.sbt.updates.UpdatesKeys.dependencyUpdates
import com.timushev.sbt.updates.UpdatesPlugin.autoImport.{dependencyUpdatesFailBuild, dependencyUpdatesFilter, moduleFilterRemoveValue}
import sbt.Keys._
import sbt._

object SbtUpdatesSettings {

  lazy val sbtUpdatesSettings = Seq(
    dependencyUpdatesFailBuild := true,
    (Compile / compile) := ((Compile / compile) dependsOn dependencyUpdates).value,
    dependencyUpdatesFilter -= moduleFilter("org.scala-lang"),
    dependencyUpdatesFilter -= moduleFilter("com.typesafe.play"),
    // locked by version of play
    dependencyUpdatesFilter -= moduleFilter("org.scalatestplus.play", "scalatestplus-play"),
    // enumeratum-play 1.8.0 incompatible with enumeratum 1.7.3
    dependencyUpdatesFilter -= moduleFilter("com.beachape", "enumeratum-play")

  )

}
