import sbt._

object Dependencies {
  
  object Version {
    val circe                   = "0.15.0-M1"
    val `circe-literal-extras`  = "0.3.0"
    val `scodec-bits`           = "1.1.34"
    val `scodec-cats`           = "1.1.0"
    val mongodb                 = "4.2.2"
    val `logback-classic`       = "1.2.3"
    val munit                   = "1.0.0-M6"
  }

  lazy val circe: Seq[ModuleID] = Seq(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-parser",
    "io.circe" %% "circe-numbers",
    "io.circe" %% "circe-jawn",
    "io.circe" %% "circe-pointer"
  ).map(_ % Version.circe)

  lazy val `circe-literal-extras`: Seq[ModuleID] = Seq(
    "group.scala.karazin" %% "circe-literal-extras"
  ).map(_ % Version.`circe-literal-extras`)

  lazy val `scodec-bits`: Seq[ModuleID] = Seq(
    "org.scodec" %% "scodec-bits"
  ).map(_ % Version.`scodec-bits`)

  lazy val `scodec-cats`: Seq[ModuleID] = Seq(
    "org.scodec" %% "scodec-cats"
  ).map(_ % Version.`scodec-cats`)

  lazy val mongodb: Seq[ModuleID] = Seq(
    "org.mongodb.scala" % "mongo-scala-driver_2.13"
  ).map(_ % Version.mongodb)

  lazy val `logback-classic`: Seq[ModuleID] = Seq(
    "ch.qos.logback" % "logback-classic"
  ).map(_ % Version.`logback-classic`)
  
  lazy val munit: Seq[ModuleID] = Seq(
    "org.scalameta" %% "munit",
    "org.scalameta" %% "munit-scalacheck"
  ).map(_ % Version.munit % "test")

}
