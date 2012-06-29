import sbt._

object ProjectBuild extends MyBuild {

    val v = "0.1"
    val org = "com.learnscala"
    val modBase = "modules/"


    lazy val app_web =
        MyProject("learnscala", file("."))
            .settings(myPlaySettings: _*)
            .dependsOn(mod_web_play, mod_data_mongo, mod_test_unit % "test->test")


    // ==== SETTINGS

    override lazy val settings =
        super.settings ++ buildSettings

}
            