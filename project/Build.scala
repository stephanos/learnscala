import sbt._

object ProjectBuild extends MyBuild {

    val v = "0.1"
    val org = "com.learnscala"
    val modBase = "modules/"


    lazy val app_web =
        MyProject("learnscala", file("."))
            .settings(myPlaySettings: _*)
            .dependsOn(mod_web_play, mod_data_mongo, mod_test_unit % "test->test")

    //lazy val exercises =
    //    MyProject("exercises", file("exercises"), isCloud)


    // ==== SETTINGS

    override lazy val settings =
        super.settings ++ buildSettings

}

//[search path for class files: D:\Applications\Java\jre7\lib\resources.jar;D:\Applications\Java\jre7\lib\rt.jar;D:\Applications\Java\jre7\lib\jsse.jar;D:\Applications\Java\jre7\lib\jce.jar;D:\Applications\Java\jre7\lib\charsets.jar;D:\Applications\Java\jre7\lib\ext\dnsns.jar;D:\Applications\Java\jre7\lib\ext\localedata.jar;D:\Applications\Java\jre7\lib\ext\sunec.jar;D:\Applications\Java\jre7\lib\ext\sunjce_provider.jar;D:\Applications\Java\jre7\lib\ext\sunmscapi.jar;D:\Applications\Java\jre7\lib\ext\zipfs.jar;D:\work\dev\tools\sbt\sbt-launch.jar;.]