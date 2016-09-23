name := "ShapelessJson"

version := "1.0"

scalaVersion := "2.11.8"

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots")
)

libraryDependencies ++= Seq(
  "com.chuusai" %% "shapeless" % "2.3.2",
  "com.typesafe.play" %% "play-json" % "2.3.4",
  "org.scalatest" %% "scalatest" % "3.0.0" % "test"
)
