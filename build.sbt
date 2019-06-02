name := "hatena-intern-pre-scala"

version := "0.1"

scalaVersion := "2.12.8"

scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked", "-Xlint")

libraryDependencies ++= Seq(
  "joda-time" % "joda-time" % "2.9.2",
  "org.joda" % "joda-convert" % "1.8" // http://www.joda.org/joda-convert/
)