import sbt._
import Keys._

object ProjectBuild extends MyBuild {

    val v = "0.1"
    val org = "com.learnscala"
    val modBase = "modules/commons/"

    lazy val learnscala =
        MyProject("learnscala", file("."))
            .settings(moduleSettings: _*)
            .aggregate(
            app_web, app_api, // apps
            mod_core, // modules
            mod_data_cache, mod_data_mongo, // data
            mod_test_unit, mod_util, mod_web_play // util
        )


    // ==== APPS

    lazy val devDeps: List[ClasspathDep[ProjectReference]] =
        if (isCloud)
            List()
        else
            List(app_api)

    lazy val app_web =
        MyProject("web", file("web"))
            .settings(myPlaySettings: _*)
            .dependsOn(mod_core, mod_web_play, mod_test_unit % "test->test")
            .dependsOn(devDeps: _*)

    lazy val app_api =
        MyProject("api", file("api"))
            .settings(appSettings: _*)
            .settings(libraryDependencies ++= (Seq(jetty) ++ apiKit))
            .dependsOn(mod_core, mod_test_unit % "test->test")


    // ==== MODULES

    lazy val mod_core =
        MyProject("core", file("modules/core"))
            .settings(moduleSettings: _*)
            .dependsOn(mod_test_unit % "test->test")


    // ==== SETTINGS

    override lazy val settings =
        super.settings ++ buildSettings


    // ==== DEPENDENCIES

    val apiKit =
        Seq(spray, sprayIo, akka2, akka2Slfj4)
}
            