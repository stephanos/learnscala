import sbt._
import Keys._
import java.util.Date


trait MyBuild
  extends Build with Settings with Modules with Deps


// ================================================================================================
// SETTINGS
// ================================================================================================

trait Settings extends Env {

  val v: String
  val org: String
  val modBase: String
  val scalaV: String = "2.10.0"

  lazy val buildSettings =
    Seq(
      scalaVersion := scalaV,
      organization := org,
      version := v
    )

  lazy val excludes =
    new sbt.FileFilter {
      def accept(f: File): Boolean =
        f.getName == "_repo" && f.getName == "_old"
    }

  lazy val baseSettings =
    Defaults.defaultSettings ++ Seq(

      resolvers += DefaultMavenRepository,
      resolvers += "spray" at "http://repo.spray.cc",
      resolvers += "codahale" at "http://repo.codahale.com",
      resolvers += "typesafe" at "http://repo.typesafe.com/typesafe/releases/",

      scalacOptions ++=
        Seq("-encoding", "UTF-8", "-deprecation", "-unchecked", "-language", "_") ++ (
          if (isCloud) Seq("-optimize") else Seq("-g:vars")), // -Xcheckinit ? (crashes with lift)

      javacOptions ++=
        Seq("-source", "1.6", "-target", "1.6",
          "-Xlint:unchecked", "-Xlint:deprecation", "-encoding", "utf8"),
      //javacOptions := Seq("-g"),

      maxErrors := 5

      // disable parallel tests
      // parallelExecution in Test := false
    )

  lazy val dummySettings =
    Seq()

  lazy val moduleSettings =
    appSettings

  lazy val playModuleSettings =
    myPlaySettings

  lazy val testModuleSettings =
    moduleSettings ++ Seq(testOptions in Test := Seq(Tests.Filter(s => false)))

  lazy val appSettings =
    baseSettings ++ (mainClass := Some("boot.Main"))


  import play.Project._

  val assetoCompiler =
    (state, sourceDirectory in Compile, resourceManaged in Compile) map {
      (state, src, resources) =>

        import java.io._
        val assetDir = src / "assets" / "source"

        if (assetDir.exists()) {

          // find modified files
          val watch = (f: File) => {
            assetDir ** "**"
          }
          val wasModified =
            !watch(assetDir).get
              .map(f => FileInfo.lastModified(f).lastModified)
              .filter(lm => lm > NodeJs.lastCall)
              .isEmpty

          if (wasModified) {
            if (!isCloud) {
              val assetsPath = assetDir.getAbsolutePath
              println("[compiling " + assetsPath + "]")
              NodeJs.call(
                "/usr/local/share/npm/lib/node_modules/asseto/bin/asseto",
                "compile",
                assetsPath,
                assetsPath.replaceAllLiterally("app/assets/source", "public")
              )
            }
          }
        }

        Seq[File]()
    }

  lazy val myPlaySettings =
    buildSettings ++ defaultSettings ++ defaultScalaSettings ++ Seq(

      // add compiler settings
      //scalacOptions ++= Seq("-Yresolve-term-conflict:object"),

      // customize assets compilation
      resourceGenerators in Compile <<= assetoCompiler(Seq(_)),

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

  object MyModule {

    def apply(id: String, isDummy: Boolean = false): Project =
      MyProject.apply(id, file(modBase + id), isDummy)
        .settings(moduleSettings: _*)
  }

  object MyProject {

    def apply(id: String, base: File, isDummy: Boolean = false): Project =
      isDummy match {
        case true => dummy(id)
        case _ => Project(id, base)
      }

    private def dummy(name: String): Project =
      Project(name, file("dummy"))
        .settings(dummySettings: _*)
  }

}


// ================================================================================================
// MODULES
// ================================================================================================

trait Modules {

  self: Settings with Deps =>

  lazy val mod_play =
    MyModule("module-play")
      .settings(libraryDependencies ++= Seq(playWeb, playFilter)) // Test.playTest
      /*libraryDependencies <+= (sbtVersion) {
          sbtVersion => Defaults.sbtPluginExtra(module, sbtVersion, localScalaVersion)
      }*/
      .dependsOn(mod_config, mod_test_web % "test->test")

  lazy val mod_sql =
    MyModule("module-sql")
      .settings(libraryDependencies ++= Seq(slick, boneCP, Runtime.postgres))
      .dependsOn(mod_config, mod_test_unit % "test->test")

  lazy val mod_mongo =
    MyModule("module-mongo")
      .settings(libraryDependencies ++= Seq(slick, rogueCore, rogueField, rogueLift, mongoDb, liftMongo))
      .dependsOn(mod_config, mod_test_unit % "test->test")

  lazy val mod_redis =
    MyModule("module-redis")
      .settings(libraryDependencies ++= Seq(jedis))
      .dependsOn(mod_config, mod_test_unit % "test->test")

  lazy val mod_queue =
    MyModule("module-queue")
      .settings(libraryDependencies ++= Seq(akkaCamel, camel))
      .dependsOn(mod_config, mod_test_unit % "test->test")

  lazy val mod_cache =
    MyModule("module-cache")
      .settings(libraryDependencies ++= Seq(jta, ehCache, memcached))
      .dependsOn(mod_config, mod_test_unit % "test->test")

  lazy val mod_mail =
    MyModule("module-mail")
      .settings(libraryDependencies ++= Seq(javaMail))
      .dependsOn(mod_config, mod_test_unit % "test->test")

  lazy val mod_config =
    MyModule("module-config")
      .dependsOn(mod_util, mod_test_unit % "test->test")

  lazy val mod_util =
    MyModule("module-util")
      .settings(libraryDependencies ++= Seq(commonsLang, commonsCodec, commonsMath, commonsIO,
      jodaTime, jodaConvert, logback, slf4jJCL, janino, jBcrypt, snappy))
      .dependsOn(mod_test_unit % "test->test")

  // === TEST

  lazy val mod_test_unit =
    MyProject("module-test-unit", file(modBase + "module-test-unit"), isDummy = isCloud)
      .settings(libraryDependencies ++= Seq(Test.specs2, Test.scheck, Test.stest, Test.mockito))
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

    var Akka = "2.1.0"
    var AWS = "1.3.27"
    var BoneCP = "0.7.1.RELEASE"
    var Camel = "2.10.3"
    var Casbah = "3.0.0-M2"
    var CommonsIO = "2.3"
    var CommonsCodec = "1.6"
    var CommonsLang = "2.6"
    var CommonsMath = "2.2"
    var EhCache = "2.6.0"
    var H2 = "1.3.170"
    var HTTP = "4.2"
    var Janino = "2.5.10"
    var Jasypt = "1.9.0"
    var JavaMail = "1.4.4"
    var jBcrypt = "0.3m"
    var Jerkson = "0.5.0"
    var Jetty = "7.5.4.v20111024"
    var Jedis = "2.1.0"
    var JDBCP = "1.0.8.5"
    var JodaConvert = "1.2"
    var JodaTime = "2.1"
    var JSON = "20090211"
    var JTA = "1.1"
    var JUnit = "4.7"
    var Lift = "2.5-M4"
    var Logback = "1.0.3"
    var Memcached = "1.3.8"
    var Metrics = "2.1.2"
    var Mockito = "1.9.0"
    var MongoDB = "2.7.3"
    var Play = play.core.PlayVersion.current
    var Postgres = "9.1-901.jdbc4"
    var Rogue = "2.0.0-beta22"
    var ScalaCheck = "1.10.0"
    var ScalaTest = "1.9.1"
    var Selenium = "2.20.0"
    var Slf4j = "1.7.2"
    var Slick = "1.0.0-RC1"
    var Snappy = "1.0.4.1"
    var Smock = "3.0"
    var Specs2 = "1.13"
    var Spray = "1.0-M2.2"
  }


  // ==== Compile

  var akka = "com.typesafe.akka" %% "akka-actor" % V.Akka
  var akkaCamel = "com.typesafe.akka" %% "akka-camel" % V.Akka excludeAll (ExclusionRule(organization = "org.apache.camel"))
  var aws = "com.amazonaws" % "aws-java-sdk" % V.AWS
  var boneCP = "com.jolbox" % "bonecp" % V.BoneCP
  val camel = "org.apache.camel" % "camel-aws" % V.Camel excludeAll (ExclusionRule(organization = "com.amazonaws"))
  var commonsCodec = "commons-codec" % "commons-codec" % V.CommonsCodec
  var commonsLang = "commons-lang" % "commons-lang" % V.CommonsLang
  var commonsMath = "org.apache.commons" % "commons-math" % V.CommonsMath
  var commonsIO = "commons-io" % "commons-io" % V.CommonsIO
  var ehCache = "net.sf.ehcache" % "ehcache-core" % V.EhCache
  var http = "org.apache.httpcomponents" % "httpclient" % V.HTTP
  var janino = "janino" % "janino" % V.Janino
  var jasypt = "org.jasypt" % "jasypt" % V.Jasypt
  var javaMail = "javax.mail" % "mail" % V.JavaMail
  var jBcrypt = "org.mindrot" % "jbcrypt" % V.jBcrypt
  var jerkson = "com.codahale" %% "jerkson" % V.Jerkson
  var jetty = "org.eclipse.jetty" % "jetty-servlet" % V.Jetty
  var jedis = "redis.clients" % "jedis" % V.Jedis
  var jdbcPool = "org.apache.tomcat" % "jdbc-pool" % V.JDBCP
  var jodaConvert = "org.joda" % "joda-convert" % V.JodaConvert
  var jodaTime = "joda-time" % "joda-time" % V.JodaTime
  var json = "org.json" % "json" % V.JSON
  var jta = "javax.transaction" % "jta" % V.JTA
  var liftMongo = "net.liftweb" %% "lift-mongodb-record" % V.Lift excludeAll (ExclusionRule(organization = "org.mongodb"))
  var liftJson = "net.liftweb" %% "lift-json" % V.Lift
  var logback = "ch.qos.logback" % "logback-classic" % V.Logback
  var librato = "com.librato.metrics" % "metrics-librato" % "2.1.2.4"
  var metrics = "com.yammer.metrics" %% "metrics-scala" % V.Metrics
  var metricsGraphite = "com.yammer.metrics" % "metrics-graphite" % V.Metrics
  var memcached = "com.googlecode.xmemcached" % "xmemcached" % V.Memcached
  var mongoDb = "org.mongodb" % "mongo-java-driver" % V.MongoDB
  var playWeb = ("play" %% "play" % V.Play) excludeAll(ExclusionRule(organization = "net.sf.ehcache"), ExclusionRule(name = "oauth.signpost"))
  var playFilter = "play" %% "filters-helpers" % V.Play
  val rogueField = "com.foursquare" %% "rogue-field" % V.Rogue intransitive()
  val rogueCore = "com.foursquare" %% "rogue-core" % V.Rogue intransitive()
  val rogueLift = "com.foursquare" %% "rogue-lift" % V.Rogue intransitive()
  var slf4jApi = "org.slf4j" % "slf4j-api" % V.Slf4j
  var slf4jJCL = "org.slf4j" % "jcl-over-slf4j" % V.Slf4j
  var slick = "com.typesafe" %% "slick" % V.Slick
  var snappy = "org.xerial.snappy" % "snappy-java" % V.Snappy


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
    val junit = "junit" % "junit" % V.JUnit
    val mockito = "org.mockito" % "mockito-all" % V.Mockito % "test"

    val playTest = "play" %% "play-test" % V.Play % "test"

    val selenium = "org.seleniumhq.selenium" % "selenium-server" % V.Selenium % "test"
    val seleniumCH = "org.seleniumhq.selenium" % "selenium-chrome-driver" % V.Selenium % "test"
    val seleniumFF = "org.seleniumhq.selenium" % "selenium-firefox-driver" % V.Selenium % "test"
    val seleniumHU = "org.seleniumhq.selenium" % "selenium-htmlunit-driver" % V.Selenium % "test"

    val scheck = "org.scalacheck" %% "scalacheck" % V.ScalaCheck % "test"
    val smock = "org.scalamock" %% "scalamock-scalatest-support" % V.Smock % "test"
    val specs2 = "org.specs2" %% "specs2" % V.Specs2 % "test"
    var stest = "org.scalatest" %% "scalatest" % V.ScalaTest % "test"
  }

}


// ================================================================================================
// ENVIRONMENT
// ================================================================================================

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


// ================================================================================================
// NODE INTEGRATION
// ================================================================================================

object NodeJs {

  import scala.sys.process._

  var lastCall = 0L

  def call(cmd: String*) = {
    lastCall = (new Date).getTime
    //println("exec " + cmd.mkString(" "))

    // I/O
    val stderr = new StringBuffer()
    val stdout = new StringBuffer()
    val logger = ProcessLogger(
      (out: String) => {
        println(out)
        stdout append (" " + out + "\n")
      },
      (err: String) => {
        stderr append (" " + err + "\n")
      })

    // exec
    val process = Process(cmd.mkString(" "))
    val exit = process ! logger
    val err = stderr.toString

    if (exit != 0 && !err.isEmpty)
      throw new PlayExceptions.AssetCompilationException(None, err, None, None)

    stdout.toString
  }
}