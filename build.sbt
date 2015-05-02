import _root_.sbt.Keys._

name := "antlr-liquid"

version := "1.0"

scalaVersion := "2.11.5"

libraryDependencies ++= Seq(
  "commons-io" % "commons-io" % "2.4",
  "org.specs2"      %% "specs2-core"       % "2.4.17"   % "test"
)

antlr4Settings

antlr4PackageName in Antlr4 := Some("com.goldv.antlr")

antlr4GenVisitor in Antlr4 := true
