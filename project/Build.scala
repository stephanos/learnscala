import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName = "learnscala"
    val appVersion = "0.1"

    val main =
        PlayProject(appName, appVersion)
            .settings(defaultScalaSettings: _*)
            .settings(scalaVersion := "2.9.1")
            .settings(libraryDependencies += "javax.mail" % "mail" % "1.4.5")
            .settings(lessEntryPoints <<= baseDirectory(_ / "app" / "assets" / "stylesheets" * "*.less"))
}
            