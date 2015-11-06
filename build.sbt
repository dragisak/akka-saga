organization := "com.github.dragisak"

name := "akka-saga"

version := "0.1.0"

scalaVersion := "2.11.7"

val akkaVersion = "2.4.0"

libraryDependencies ++= Seq(
        "com.typesafe.akka" %%  "akka-actor"       % akkaVersion,
        "com.typesafe.akka" %%  "akka-slf4j"       % akkaVersion    % "test",
        "ch.qos.logback"    %   "logback-classic"  % "1.1.3"        % "test",
        "com.typesafe.akka" %%  "akka-testkit"     % akkaVersion    % "test",
        "org.scalatest"     %%  "scalatest"        % "2.2.4"        % "test"
)

scalacOptions ++= Seq(
        "-language:postfixOps",
        "-deprecation",
        "-Xfatal-warnings",
        "-Xlint"
)
