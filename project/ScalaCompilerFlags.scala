object ScalaCompilerFlags {
  val scalaCompilerOptions: Seq[String] = Seq(
    "-Xfatal-warnings",
    "-Wunused:explicits",
    "-Wunused:implicits",
    "-Wunused:imports",
    "-Wunused:locals",
    "-Wunused:params",
    "-Wunused:privates",
    "-deprecation",
    "-feature",
    "-unchecked",
    "-language:implicitConversions",
    "-language:strictEquality",
    "-language:reflectiveCalls",
    // required in place of silencer plugin
    "-Wconf:msg=unused import&src=html/.*:s",
    "-Wconf:msg=unused import&src=views/.*:s",
    "-Wconf:src=routes/.*:s"
  )
}
