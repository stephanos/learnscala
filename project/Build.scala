import sbt._
import Keys._

object ProjectBuild extends MyBuild {

    val v = "0.1"
    val org = "de.learnscala"
    val modBase = "modules/"

    lazy val root =
        Project("learnscala", file("."))
            .aggregate(frontend, exercises)


    // ==== PROJECTS

    lazy val frontend =
        MyProject("frontend", file("frontend"))
            .settings(myPlaySettings: _*)
            //.settings(libraryDependencies ++= Seq(sun_tools))
            .dependsOn(mod_web_play, mod_data_mongo, mod_test_unit % "test->test")

    lazy val exercises = // cannot use RootProject ("AttributeKey ID collisions detected for: 'pgp-signer'")
        MyProject("exercises", file("exercises"), isCloud)
            .settings(libraryDependencies ++= Seq(http))
            .settings(libraryDependencies ++= Seq(Test.specs2, Test.mockito))
            .settings(libraryDependencies ++= Seq(squeryl, Test.h2).map(_.copy(configurations = Some("compile"))))


    // ==== SETTINGS

    override lazy val settings =
        super.settings ++ buildSettings


    // ==== DEPENDENCIES

    lazy val sun_tools =
        "com.sun" % "tools" % "1.6.0" from ("file:///" + jdkHome + fileSep + "lib" + fileSep + "tools.jar")
}