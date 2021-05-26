import Dependencies._

lazy val root = project
  .in(file("."))
  .settings(name := "mongo4s")
  .settings(commonSettings: _*)
  .settings(libraryDependencies ++= dependencies)
  .settings(testFrameworks += new TestFramework("munit.Framework"))
  .settings(Test / parallelExecution := true)

lazy val dependencies =
    circe ++
    `circe-literal-extras` ++
    `scodec-bits` ++
    `scodec-cats` ++
    mongodb ++
    `logback-classic` ++
    munit

testFrameworks += new TestFramework("munit.Framework")

lazy val commonSettings = Seq(
    scalaVersion     := "3.0.0-RC3",
    organization     := "group.scala.karazin",
    organizationName := "Karazin Scala Users Group",
    version          := "0.1.0-SNAPSHOT",
    scalacOptions ++= Seq(
        "-deprecation",
        "-unchecked",
        "-Xmax-inlines", "32",
        "-Xfatal-warnings",
        "-Xprint-suspension",
        "-language:postfixOps",
        "-language:implicitConversions",
        "-language:higherKinds"
    ),
    javacOptions ++= Seq(
        "-source", "11",
        "-target", "11"
    )
)
