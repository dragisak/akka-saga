organization := "com.github.dragisak"

name := "akka-saga"

val akkaVersion = "2.4.0"
val scalatestVersion = "2.2.4"
val projectVersion = "0.1.0"
val commonScalaVersion = "2.11.7"

version := projectVersion

scalaVersion := commonScalaVersion

libraryDependencies ++= Seq(
  "com.typesafe.akka"           %%  "akka-actor"             % akkaVersion,
  "com.typesafe.akka"           %%  "akka-cluster"           % akkaVersion,
  "com.typesafe.akka"           %%  "akka-persistence"       % akkaVersion,
  "com.typesafe.akka"           %%  "akka-slf4j"             % akkaVersion        % Test,
  "ch.qos.logback"              %   "logback-classic"        % "1.1.3"            % Test,
  "com.typesafe.akka"           %%  "akka-testkit"           % akkaVersion        % Test,
  "org.scalatest"               %%  "scalatest"              % scalatestVersion   % Test,
  "org.iq80.leveldb"            %   "leveldb"                % "0.7"              % Test,
  "org.fusesource.leveldbjni"   %   "leveldbjni-all"         % "1.8"              % Test
)

scalacOptions ++= Seq(
  "-language:postfixOps",
  "-deprecation",
  "-Xfatal-warnings",
  "-Xlint"
)


lazy val root = project.in(file("."))


import com.typesafe.sbt.SbtMultiJvm
import com.typesafe.sbt.SbtMultiJvm.MultiJvmKeys.MultiJvm

lazy val multiNodeTests = Project(
  id = "multiNodeTests",
  base = file("multi-node-tests"),
  settings = Project.defaultSettings ++ SbtMultiJvm.multiJvmSettings ++ Seq(
    name            := "akka-sample-multi-node-scala",
    version         := projectVersion,
    scalaVersion    := commonScalaVersion,
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-multi-node-testkit"  % akkaVersion        % Test,
      "org.scalatest"     %%  "scalatest"               % scalatestVersion   % Test
    ),
    // make sure that MultiJvm test are compiled by the default test compilation
    compile in MultiJvm <<= (compile in MultiJvm) triggeredBy (compile in Test),
    // disable parallel tests
    parallelExecution in Test := false,
    // make sure that MultiJvm tests are executed by the default test target,
    // and combine the results from ordinary test and multi-jvm tests
    executeTests in Test <<= (executeTests in Test, executeTests in MultiJvm) map {
      case (testResults, multiNodeResults) =>
        val overall = if (testResults.overall.id < multiNodeResults.overall.id)
            multiNodeResults.overall
          else
            testResults.overall
        Tests.Output(overall,
          testResults.events ++ multiNodeResults.events,
          testResults.summaries ++ multiNodeResults.summaries
        )
    }
  )
).dependsOn(root).configs(MultiJvm)
