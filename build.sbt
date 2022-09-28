import sbt.Provided

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "test-scala"
  )
libraryDependencies ++= Seq(
  "io.dropwizard.metrics" % "metrics-jvm" % "4.2.8",
  "nl.grons" %% "metrics4-scala-hdr" % "4.2.8",
  // https://mvnrepository.com/artifact/com.typesafe.akka/akka-stream
  "com.typesafe.akka" %% "akka-stream" % "2.6.14" exclude("com.typesafe.akka","akka-actor_2.13"),
  "com.typesafe.akka" %% "akka-actor-typed" % "2.6.14" exclude("com.typesafe.akka","akka-actor_2.13"),
  "com.typesafe.akka" %% "akka-actor" % "2.6.14",
  "com.typesafe.play" %% "play-ahc-ws-standalone" % "2.1.3" excludeAll(
    ExclusionRule(organization = "com.typesafe.akka", name = "akka-stream_2.13"))

)

