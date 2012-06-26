import sbt._
import Keys._

object ProjectBuild extends MyBuild {

    val v = "0.1"
    val org = "com.learnscala"
    val modBase = "modules/commons/"

    lazy val crashnote =
        MyProject("learnscala", file("."))
            .settings(moduleSettings: _*)
            .aggregate(app_web)


    // ==== APPS

    lazy val app_web =
        MyProject("web", file("web"))
            .settings(myPlaySettings: _*)
            .settings(libraryDependencies += "javax.mail" % "mail" % "1.4.5")
            .dependsOn(mod_web_play, mod_test_unit % "test->test")


    // ==== SETTINGS

    override lazy val settings =
        super.settings ++ buildSettings

}
            