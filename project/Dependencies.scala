import sbt._

object Dependencies {
  
  object Version {
    val circe                   = "0.14.0-M6"
    val `scodec-bits`           = "1.1.26"
    val `scodec-cats`           = "1.1.0-RC3"
    val mongodb                 = "4.2.2"
    val `logback-classic`       = "1.2.3"
    val munit                   = "0.7.25"
    val `circe-literal-extras`  = "0.1.0-SNAPSHOT"
  }

  lazy val circe: Seq[ModuleID] = Seq(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-parser",
    "io.circe" %% "circe-numbers",
    "io.circe" %% "circe-jawn",
    "io.circe" %% "circe-pointer"
  ).map(_ % Version.circe withSources() withJavadoc())

  lazy val `circe-literal-extras`: Seq[ModuleID] = Seq(
    "group.scala.karazin" %% "circe-literal-extras"
  ).map(_ % Version.`circe-literal-extras` withSources() withJavadoc())

  lazy val `scodec-bits`: Seq[ModuleID] = Seq(
    "org.scodec" %% "scodec-bits"
  ).map(_ % Version.`scodec-bits` withSources() withJavadoc())

  lazy val `scodec-cats`: Seq[ModuleID] = Seq(
    "org.scodec" %% "scodec-cats"
  ).map(_ % Version.`scodec-cats` withSources() withJavadoc())

  lazy val mongodb: Seq[ModuleID] = Seq(
    "org.mongodb.scala" % "mongo-scala-driver_2.13"
  ).map(_ % Version.mongodb withSources() withJavadoc())

  lazy val `logback-classic`: Seq[ModuleID] = Seq(
    "ch.qos.logback" % "logback-classic"
  ).map(_ % Version.`logback-classic` withSources() withJavadoc())
  
  lazy val munit: Seq[ModuleID] = Seq(
    "org.scalameta" %% "munit",
    "org.scalameta" %% "munit-scalacheck"
  ).map(_ % Version.munit % "test" withSources() withJavadoc())

}
