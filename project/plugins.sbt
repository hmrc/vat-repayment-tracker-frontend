resolvers += "hmrc-releases" at "https://artefacts.tax.service.gov.uk/artifactory/hmrc-releases/"
resolvers += "Typesafe Releases" at "https://repo.typesafe.com/typesafe/releases/"
resolvers += MavenRepository("HMRC-open-artefacts-maven2", "https://open.artefacts.tax.service.gov.uk/maven2")
resolvers += Resolver.url("HMRC-open-artefacts-ivy2", url("https://open.artefacts.tax.service.gov.uk/ivy2"))(Resolver.ivyStylePatterns)

ThisBuild / libraryDependencySchemes += "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always

// FORMAT: OFF
addSbtPlugin("org.playframework"  % "sbt-plugin"             % "3.0.8")
addSbtPlugin("uk.gov.hmrc"        % "sbt-distributables"     % "2.6.0")
addSbtPlugin("org.scalastyle"     %% "scalastyle-sbt-plugin" % "1.0.0" exclude("org.scala-lang.modules", "scala-xml_2.12"))
addSbtPlugin("org.scalariform"    % "sbt-scalariform"        % "1.8.3" exclude("org.scala-lang.modules", "scala-xml_2.12"))
addSbtPlugin("uk.gov.hmrc"        % "sbt-auto-build"         % "3.24.0")
addSbtPlugin("org.scoverage"      % "sbt-scoverage"          % "2.3.0")
addSbtPlugin("org.wartremover"    % "sbt-wartremover"        % "3.3.1")
addSbtPlugin("io.github.irundaia" % "sbt-sassify"            % "1.5.2")
addSbtPlugin("com.timushev.sbt"   % "sbt-updates"            % "0.6.4")
// FORMAT: ON

addDependencyTreePlugin
