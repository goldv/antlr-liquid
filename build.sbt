import _root_.sbt.Keys._

name := "antlr-liquid"

version := "1.0"

scalaVersion := "2.11.5"

libraryDependencies ++= Seq(
  "commons-io" % "commons-io" % "2.4",
  "org.specs2" %% "specs2-core" % "2.4.17" % "test",
  "io.spray" %% "spray-can" % "1.3.1",
  "io.spray" %% "spray-routing" % "1.3.1",
  "io.spray" %% "spray-json" % "1.3.1",
  "com.typesafe.akka"   %%  "akka-actor" % "2.3.9"
)

antlr4Settings

antlr4PackageName in Antlr4 := Some("com.goldv.antlr")

antlr4GenVisitor in Antlr4 := true

lazy val root = (project in file(".")).enablePlugins(SbtTwirl)