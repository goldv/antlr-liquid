resolvers ++= Seq(
  "simplytyped" at "http://simplytyped.github.io/repo/releases",
  "spray repo" at "http://repo.spray.io"
)

addSbtPlugin("com.simplytyped" % "sbt-antlr4" % "0.7.4")

addSbtPlugin("com.typesafe.sbt" % "sbt-twirl" % "1.0.4")