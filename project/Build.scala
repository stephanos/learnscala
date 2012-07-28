import sbt._
import Keys._
import org.sbtidea._
import PlayProject._

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
            .settings(libraryDependencies ++= Seq(sun_tools))
            .settings(templatesImport += "views.html.comp._")
            .dependsOn(mod_web_play, mod_data_mongo, mod_test_unit % "test->test")

    lazy val exercises = // cannot use RootProject ("AttributeKey ID collisions detected for: 'pgp-signer'")
        MyProject("exercises", file("exercises"), isCloud)
            .settings(moduleSettings: _*)
            .settings(libraryDependencies ++= Seq(http, jodaTime, jodaConvert))
            .settings(libraryDependencies ++= Seq(Test.specs2, Test.junit, Test.mockito, Test.scheck))
            .settings(libraryDependencies ++= Seq(squeryl, Test.h2).map(_.copy(configurations = Some("compile"))))


    // ==== SETTINGS

    override lazy val settings =
        super.settings ++ SbtIdeaPlugin.ideaSettings ++ buildSettings


    // ==== DEPENDENCIES

    val sun_tools_file = file(jdk6Home + fileSep + "lib" + fileSep + "tools.jar").getCanonicalPath
    lazy val sun_tools = "com.sun" % "tools" % "1.6.0" from ("file:///" + sun_tools_file)
    println("TOOLS.jar: " + sun_tools_file)

    lazy val jdk6Home = {
        val jdk = Option(System.getenv("JDK6_HOME"))
        val jre = Option(System.getProperty("java.home", System.getProperty("java_home", System.getenv("JAVA_HOME"))))
        val path = jdk.getOrElse(jre.map(_ + fileSep + "..").getOrElse("/usr/lib/jdk"))
        new File(path).getAbsolutePath
    }
}