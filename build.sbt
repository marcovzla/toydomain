name := "toydomain"

version := "1.0"

scalaVersion := "2.11.5"

libraryDependencies ++= Seq(
  "edu.arizona.sista" %% "processors" % "5.2-SNAPSHOT",
  "edu.arizona.sista" %% "processors" % "5.2-SNAPSHOT" classifier "models"
)