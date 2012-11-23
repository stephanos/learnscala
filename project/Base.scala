import sbt._
import Keys._
import org.sbtidea._

trait MyBuild
    extends Build with Settings with Modules with Deps

trait Settings extends TaskStage with Env {

    val v: String
    val org: String

    lazy val buildSettings =
        ideaSettings ++ Seq(
            organization := org,
            scalaVersion := "2.9.1",
            version := v
        )

    lazy val excludes =
        new sbt.FileFilter {
            def accept(f: File): Boolean =
                f.getName == "_repo" && f.getName == "_old"
        }

    lazy val ideaSettings =
        SbtIdeaPlugin.ideaSettings ++
            Seq(
                SbtIdeaPlugin.commandName := "idea",
                SbtIdeaPlugin.includeScalaFacet := true,
                SbtIdeaPlugin.addGeneratedClasses := true,
                SbtIdeaPlugin.defaultClassifierPolicy := false
            )

    lazy val baseSettings =
        Defaults.defaultSettings ++ Seq(

            // repositories
            resolvers += DefaultMavenRepository,
            resolvers += "spray" at "http://repo.spray.cc",
            resolvers += "codahale" at "http://repo.codahale.com",
            resolvers += "typesafe" at "http://repo.typesafe.com/typesafe/releases/",

            // compile options
            scalacOptions ++=
                Seq("-encoding", "UTF-8", "-deprecation", "-unchecked") ++ (
                    if (isCloud) Seq("-optimize")
                    else Seq("-g:vars") // -Xcheckinit ? (crashes with lift)
                    ),
            javacOptions ++= Seq("-Xlint:unchecked", "-Xlint:deprecation")

            // disable parallel tests
            // parallelExecution in Test := false
        )

    lazy val stageSettings =
        Seq(stage <<= stageTask) ++ Seq(packageProjects <<= packageProjectsTask)

    lazy val dummySettings =
        Seq(stage <<= stageDummyTask)

    lazy val moduleSettings =
        appSettings ++ Seq(stage := Unit) // skip "stage"

    lazy val testModuleSettings =
        moduleSettings ++ Seq(testOptions in Test := Seq(Tests.Filter(s => false)))

    lazy val appSettings =
        baseSettings ++ stageSettings ++ (mainClass := Some("boot.Main"))

    import PlayProject._

    val ueberEntryPoints = SettingKey[PathFinder]("play-ueber-entry-points")
    val ueberComp =
        AssetsCompiler("ueber", _ / "assets" ** "*" , ueberEntryPoints,
        {
            (name, min) => if (min) name.replace(".js", ".min.js") else name
        }, {
            (file, options) => {
                if(!isCloud) {
                    val folder = file.getParentFile
                    println(" - compiling " + folder.getName)
                    val input = folder.getAbsolutePath
                    NodeJs.call("/usr/local/share/npm/lib/node_modules/asseto/bin/asseto",
                        input, input.replaceAllLiterally("app/assets", "public"))
                }
                ("", None, Seq(file))
            }
        }, coffeescriptOptions)

    lazy val myPlaySettings =
        buildSettings ++ defaultSettings ++ defaultScalaSettings ++ stageSettings ++ Seq(

            // add compiler settings
            //scalacOptions ++= Seq("-Yresolve-term-conflict:object"),

            // define asset locations
            ueberEntryPoints <<= baseDirectory(_ / "app" / "assets" ** "build.json"),

            // customize assets compilation
            resourceGenerators in Compile <<= ueberComp(Seq(_)),

            // exclude old/repo resources
            excludeFilter in managedSources := excludes,
            excludeFilter in unmanagedSources := excludes,
            excludeFilter in managedResources := excludes,
            excludeFilter in unmanagedResources := excludes,

            // runnable main class
            mainClass := Some("play.core.server.NettyServer"),

            // reset default dependencies
            libraryDependencies := Seq()

            // experimental feature: only compile changed files
            //incrementalAssetsCompilation := true,
        )

    object MyProject {

        def apply(id: String, base: File, isDummy: Boolean = false): Project =
            isDummy match {
                case true => dummy(id)
                case _ => Project(id, base)
            }

        def dummy(name: String): Project =
            Project(name, file("dummy"))
                .settings(dummySettings: _*)
    }

}

// ================================================================================================
// MODULES
// ================================================================================================

trait Modules {

    self: Settings with Deps =>

    val modBase: String

    // === WEB

    lazy val mod_web_play =
        MyProject("module-web-play", file(modBase + "module-web-play"))
            .settings(libraryDependencies ++= Seq(playWeb)) // Test.playTest
            .settings(moduleSettings: _*)
            /*libraryDependencies <+= (sbtVersion) {
                sbtVersion => Defaults.sbtPluginExtra(module, sbtVersion, localScalaVersion)
            }*/
            .dependsOn(mod_util, mod_data_cache, mod_test_web % "test->test")

    // === DATA

    lazy val mod_data_sql =
        MyProject("module-data-sql", file(modBase + "module-data-sql"))
            .settings(libraryDependencies ++= Seq(squeryl, boneCP, Runtime.postgres))
            .settings(moduleSettings: _*)
            .dependsOn(mod_util, mod_test_unit % "test->test")

    lazy val mod_data_mongo =
        MyProject("module-data-mongo", file(modBase + "module-data-mongo"))
            .settings(libraryDependencies ++= Seq(rogue, mongoDb, liftMongo))
            .settings(moduleSettings: _*)
            .dependsOn(mod_util, mod_test_unit % "test->test")

    lazy val mod_data_redis =
        MyProject("module-data-redis", file(modBase + "module-data-redis"))
            .settings(libraryDependencies ++= Seq(jedis))
            .settings(moduleSettings: _*)
            .dependsOn(mod_util, mod_test_unit % "test->test")

    lazy val mod_data_cache =
        MyProject("module-data-cache", file(modBase + "module-data-cache"))
            .settings(libraryDependencies ++= Seq(memcache))
            .settings(moduleSettings: _*)
            .dependsOn(mod_util, mod_test_unit % "test->test")

    // === UTIL

    lazy val mod_util =
        MyProject("module-util", file(modBase + "module-util"))
            .settings(libraryDependencies ++= Seq(commonsLang, commonsCodec, commonsMath, commonsIO,
            jodaTime, jodaConvert, logback, slf4jJCL, janino, liftJson, javaMail, jBcrypt, snappy)) // ehCache
            .settings(moduleSettings: _*)
            .dependsOn(mod_test_unit % "test->test")

    // === TEST

    lazy val mod_test_unit =
        MyProject("module-test-unit", file(modBase + "module-test-unit"), isDummy = isCloud)
            .settings(libraryDependencies ++= Seq(Test.specs2, Test.mockito))
            .settings(testModuleSettings: _*)

    lazy val mod_test_net =
        MyProject("module-test-net", file(modBase + "module-test-net"), isDummy = isCloud)
            .dependsOn(mod_test_unit % "test->test")
            .settings(testModuleSettings: _*)

    lazy val mod_test_web =
        MyProject("module-test-web", file(modBase + "module-test-web"), isDummy = isCloud)
            .dependsOn(mod_test_unit % "test->test")
            .settings(testModuleSettings: _*)
}

// ================================================================================================
// DEPENDENCIES
// ================================================================================================

trait Deps {

    self: Settings =>

    // ==== Versions

    object V {

        val Akka2 = "2.0.2"
        val AWS = "1.3.10"
        val BoneCP = "0.7.1.RELEASE"
        val Casbah = "3.0.0-M2"
        val CommonsIO = "2.3"
        val CommonsCodec = "1.6"
        val CommonsLang = "2.6"
        val CommonsMath = "2.2"
        val EhCache = "2.5.2"
        val H2 = "1.3.166"
        val HTTP = "4.2"
        val Janino = "2.5.10"
        val Jasypt = "1.9.0"
        val JavaMail = "1.4.4"
        val jBcrypt = "0.3m"
        val Jerkson = "0.5.0"
        val Jetty = "7.5.4.v20111024"
        val Jedis = "2.1.0"
        val JDBCP = "1.0.8.5"
        val JodaConvert = "1.2"
        val JodaTime = "2.1"
        val JSON = "20090211"
        val Lift = "2.5-M2"
        val Logback = "1.0.3"
        val XMemcached = "1.3.6"
        val Metrics = "2.1.2"
        val Mockito = "1.9.0"
        val MongoDB = "2.7.3"
        val Play = play.core.PlayVersion.current
        val Postgres = "9.1-901.jdbc4"
        val Rogue = "1.1.8"
        val Selenium = "2.20.0"
        val SJSON = "0.15"
        val Slf4j = "1.7.2"
        val Snappy = "1.0.4.1"
        val Specs2 = "1.12.1"
        val Spray = "1.0-M2.2"
        val Squeryl = "0.9.5"
        val Unfiltered = "0.6.3"
    }

    // ==== Compile

    val akka2 = "com.typesafe.akka" % "akka-actor" % V.Akka2
    val akka2Slfj4 = "com.typesafe.akka" % "akka-slf4j" % V.Akka2
    val aws = "com.amazonaws" % "aws-java-sdk" % V.AWS
    val boneCP = "com.jolbox" % "bonecp" % V.BoneCP
    val casbah = "org.mongodb" % "casbah_2.9.1" % V.Casbah
    val commonsCodec = "commons-codec" % "commons-codec" % V.CommonsCodec
    val commonsLang = "commons-lang" % "commons-lang" % V.CommonsLang
    val commonsMath = "org.apache.commons" % "commons-math" % V.CommonsMath
    val commonsIO = "commons-io" % "commons-io" % V.CommonsIO
    val ehCache = "net.sf.ehcache" % "ehcache-core" % V.EhCache
    val http = "org.apache.httpcomponents" % "httpclient" % V.HTTP
    val janino = "janino" % "janino" % V.Janino
    val jasypt = "org.jasypt" % "jasypt" % V.Jasypt
    val javaMail = "javax.mail" % "mail" % V.JavaMail
    val jBcrypt = "org.mindrot" % "jbcrypt" % V.jBcrypt
    val jerkson = "com.codahale" %% "jerkson" % V.Jerkson
    val jetty = "org.eclipse.jetty" % "jetty-servlet" % V.Jetty
    val jedis = "redis.clients" % "jedis" % V.Jedis
    val jdbcPool = "org.apache.tomcat" % "jdbc-pool" % V.JDBCP
    val jodaConvert = "org.joda" % "joda-convert" % V.JodaConvert
    val jodaTime = "joda-time" % "joda-time" % V.JodaTime
    val json = "org.json" % "json" % V.JSON
    val liftMongo = "net.liftweb" %% "lift-mongodb-record" % V.Lift excludeAll (
        ExclusionRule(organization = "org.mongodb"))
    val liftJson = "net.liftweb" %% "lift-json" % V.Lift
    val logback = "ch.qos.logback" % "logback-classic" % V.Logback
    val librato = "com.librato.metrics" % "metrics-librato" % "2.1.2.4"
    val metrics = "com.yammer.metrics" %% "metrics-scala" % V.Metrics
    val metricsGraphite = "com.yammer.metrics" % "metrics-graphite" % V.Metrics
    val memcache = "com.googlecode.xmemcached" % "xmemcached" % V.XMemcached
    val mongoDb = "org.mongodb" % "mongo-java-driver" % V.MongoDB
    val playWeb = ("play" %% "play" % V.Play) excludeAll(
        ExclusionRule(organization = "org.springframework"),
        ExclusionRule(organization = "net.sf.ehcache"),
        ExclusionRule(organization = "com.codahale"),
        ExclusionRule(name = "closure-compiler"),
        ExclusionRule(name = "bonecp"),
        ExclusionRule(name = "ebean"),
        ExclusionRule(name = "anorm"),
        ExclusionRule(name = "h2"))
    val rogue = "com.foursquare" %% "rogue" % V.Rogue intransitive()
    val servlet30 = "org.glassfish" % "javax.servlet" % "3.0"
    val slf4jApi = "org.slf4j" % "slf4j-api" % V.Slf4j
    val slf4jJCL = "org.slf4j" % "jcl-over-slf4j" % V.Slf4j
    val sjson = "net.debasishg" %% "sjson" % V.SJSON
    val snappy = "org.xerial.snappy" % "snappy-java" % V.Snappy
    val spray = "cc.spray" % "spray-server" % V.Spray
    val sprayIo = "cc.spray" % "spray-io" % V.Spray
    val sprayCan = "cc.spray" % "spray-can" % V.Spray
    val squeryl = "org.squeryl" %% "squeryl" % V.Squeryl

    object unfiltered {

        val filter = "net.databinder" %% "unfiltered-filter" % V.Unfiltered
    }

    // ==== Provided

    object Provided {
    }

    // ==== Runtime

    object Runtime {

        val postgres = "postgresql" % "postgresql" % V.Postgres // % "runtime"
    }

    // ==== Test

    object Test {

        val h2 = "com.h2database" % "h2" % V.H2 % "test"
        val playTest = "play" %% "play-test" % V.Play % "test"
        val mockito = "org.mockito" % "mockito-all" % V.Mockito % "test"
        val selenium = "org.seleniumhq.selenium" % "selenium-server" % V.Selenium % "test"
        val seleniumCH = "org.seleniumhq.selenium" % "selenium-chrome-driver" % V.Selenium % "test"
        val seleniumFF = "org.seleniumhq.selenium" % "selenium-firefox-driver" % V.Selenium % "test"
        val seleniumHU = "org.seleniumhq.selenium" % "selenium-htmlunit-driver" % V.Selenium % "test"
        val scheck = "org.scalacheck" %% "scalacheck" % "1.10.0" % "test"
        val specs2 = "org.specs2" %% "specs2" % V.Specs2 % "test"
        val junit = "junit" % "junit" % "4.7"
    }

}

// ================================================================================================
// TASK: Stage
// ================================================================================================

trait TaskStage extends Env {

    lazy val stage = TaskKey[Unit]("stage")

    lazy val libFilter =
        (fname: String) => true

    //fname.contains("-compiler") || projName.contains("worker") // compiler is required in 'worker'

    def stageDummyTask = (streams, update, packageProjects, baseDirectory, mainClass) map {
        (s, updateReport, packaged, root, mainClass) =>
            () // do nothing
    }

    def stageTask = (streams, dependencyClasspath in Runtime, packageProjects, baseDirectory, mainClass) map {
        (s, dependencies, packaged, root, mainClass) =>

            val projName = root.getName
            println("staging '" + projName + "'")

            // COPY JARs

            val target = root / "target"
            val destPath = target / "staged"
            IO.delete(destPath)
            IO.createDirectory(destPath)

            val libs = dependencies.filter(_.data.ext == "jar").map(_.data) ++ packaged
            libs foreach {
                jar =>
                    val fname = jar.getName
                    if (libFilter(fname))
                        IO.copyFile(jar, new File(destPath, fname))
            }

            // WRITE SCRIPT

            val start = target / "start"
            val script = (
                """|#!/usr/bin/env sh
                  |java $@ -cp "`dirname $0`/staged/*" """ + mainClass.get + """ `dirname $0`/..""").stripMargin
            println(script)

            IO.write(start, script)
            if (isCloud) "chmod a+x %s".format(start.getAbsolutePath) !

            s.log.info("")
            s.log.info("Your application is ready to be run in place: target/start")
            s.log.info("")

            ()
    }

    val packageProjects = TaskKey[Seq[File]]("package-projects")
    val packageProjectsTask = (state, thisProjectRef) flatMap {
        (s, r) => PlayProject.inAllDependencies(r, (packageBin in Compile).task, Project structure s).join
    }
}

trait Env {

    lazy val isCloud =
        System.getProperty("os.name").contains("nux")

    val fileSep =
        java.io.File.separator

    def filesWithExt(base: File, ext: String) =
        IO.listFiles(base, new FileFilter() {
            def accept(path: File) = path.getName.endsWith(ext)
        }).sortWith(_.compareTo(_) < 0)

    def time[A](cmt: String)(a: => A) = {
        val now = System.nanoTime
        val result = a
        val micros = (System.nanoTime - now) / 1000
        println("[%s] %d ms".format(cmt, micros))
        result
    }
}

object NodeJs {

    import scala.sys.process._
    import org.mozilla.javascript._

    def call(cmd: String*) = {
        //println("exec " + cmd.mkString(" "))

        // I/O
        val stderr = new StringBuffer()
        val stdout = new StringBuffer()
        val logger = ProcessLogger(
            (out: String) => {
                //println(out)
                stdout append (out + "\n")
            },
            (err: String) => {
                stderr append (err + "\n")
            })

        // exec
        val process = Process(cmd.mkString(" "))
        val exit = process ! logger
        val err = stderr.toString

        if (exit != 0 && !err.isEmpty)
            throw new JavaScriptException(stderr.toString)
        stdout.toString
    }
}