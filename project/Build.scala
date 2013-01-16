import sbt._
import Keys._
import play.Project._

object ProjectBuild extends MyBuild {

    val v = "0.2"
    val org = "de.learnscala"
    val modBase = "modules/commons/"


    lazy val root =
        Project("learnscala", file("."))
            .aggregate(app_frontend, app_api, mod_api)


    // ==== PROJECTS

    lazy val app_frontend =
        MyProject("app-frontend", file("frontend"))
            .settings(myPlaySettings: _*)
            .settings(templatesImport += "views.html.comp._")
            .dependsOn(mod_api, mod_mail, mod_test_unit % "test->test")

    lazy val app_api =
        MyProject("app-api", file("api"))
            .settings(myPlaySettings: _*)
            .dependsOn(mod_api, mod_test_unit % "test->test")

    lazy val mod_api =
        MyProject("module-api", file("modules/module-api"))
            .settings(libraryDependencies ++= Seq(playWeb, scalaP, scalaR, scalaC, sun_tools))
            .dependsOn(mod_play)

    /*
    // use RootProject ("AttributeKey ID collisions detected for: 'pgp-signer'")
    lazy val exercises =
        MyProject("exercises", file("exercises"), isCloud)
            .settings(moduleSettings: _*)
            .settings(libraryDependencies ++= Seq(http, jodaTime, jodaConvert))
            .settings(libraryDependencies ++= Seq(Test.specs2, Test.junit, Test.mockito, Test.scheck))
            .settings(libraryDependencies ++= Seq(squeryl, Test.h2).map(_.copy(configurations = Some("compile"))))
    */


    // ==== SETTINGS

    override lazy val settings =
        super.settings ++ buildSettings ++ Seq(
            javaHome := Some(file(jdk6Home))
        )

    println("JRE HOME: " + System.getProperty("java.home"))
    println("JDK HOME: " + jdk6Home)


    // ==== DEPENDENCIES

    val sun_tools_file =
        if(System.getProperty("os.name").contains("Mac"))
            "Classes" + fileSep + "classes.jar"
        else
            "lib" + fileSep + "tools.jar"
    val sun_tools_path = file(jdk6Home + fileSep + ".." + fileSep + sun_tools_file).getCanonicalPath
    lazy val sun_tools = "com.sun" % "tools" % "1.6.0" from ("file:///" + sun_tools_path)

    println("TOOLS.jar: " + sun_tools_path)

    val scalaP = "org.scala-lang" % "scalap" % scalaV
    val scalaR = "org.scala-lang" % "scala-reflect" % scalaV
    val scalaC = "org.scala-lang" % "scala-compiler" % scalaV


    // ==== OTHER

    lazy val jdk6Home = {
        val jdk = Option(System.getProperty("JDK6_HOME", System.getenv("JDK6_HOME")))
        val jre = Option(System.getProperty("java.home", System.getProperty("java_home", System.getenv("JAVA_HOME"))))
        val path = jdk.getOrElse(jre.map(_ + fileSep + "..").getOrElse("/usr/lib/jdk"))
        new File(path).getCanonicalPath
    }
}