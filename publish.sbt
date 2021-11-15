ThisBuild / organization := "group.scala"
ThisBuild / organizationName := "Karazin Scala Users Group"
ThisBuild / organizationHomepage := Some(url("https://github.com/KarazinScalaUsersGroup"))

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/KarazinScalaUsersGroup/mongo-literals"),
    "scm:git@github.com:KarazinScalaUsersGroup/mongo-literals.git"
  )
)
ThisBuild / developers := List(
  Developer(
    id    = "IgorWolkov",
    name  = "Igor Wolkov",
    email = "igor.wolkov@scala.group",
    url   = url("https://github.com/IgorWolkov")
  )
)

ThisBuild / description := "Mongo literals."
ThisBuild / licenses := List("Apache 2" -> new URL("http://www.apache.org/licenses/LICENSE-2.0.txt"))
ThisBuild / homepage := Some(url("https://github.com/KarazinScalaUsersGroup/mongo-literals"))

// Remove all additional repository other than Maven Central from POM
ThisBuild / pomIncludeRepository := { _ => false }
ThisBuild / publishTo := {
  val nexus = "https://s01.oss.sonatype.org/"
  if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "content/repositories/releases")
}
ThisBuild / publishMavenStyle := true
