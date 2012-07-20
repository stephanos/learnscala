import sbt._
import Keys._

trait MyBuild
    extends Build with Settings with Modules with Deps

trait Settings extends TaskStage with Env {

    val v: String
    val org: String

    lazy val buildSettings = Seq(
        organization := org,
        scalaVersion := "2.9.1",
        version := v
    )

    lazy val excludes =
        new sbt.FileFilter {
            def accept(f: File): Boolean =
                f.getName == "_repo" && f.getName == "_old"
        }

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
            //parallelExecution in Test := false
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
        AssetsCompiler("ueber", (_ ** "*.*"), ueberEntryPoints, {
            (name, min) => if (min) name.replace(".js", ".min.js") else name
        }, {
            (file, options) => Compiler4Ueber.compile(file)
        }, coffeescriptOptions)

    /*val lessComp =
        AssetsCompiler("less",
        (_ ** "*.less"), lessEntryPoints, {
            (name, min) => name.replace(".less", if (min) ".min.css" else ".css")
        }, {
            (lessFile, options) => Compiler4Less.compile(lessFile)
        }, lessOptions)*/

    lazy val myPlaySettings =
        buildSettings ++ defaultSettings ++ defaultScalaSettings ++ stageSettings ++ Seq(

            //scalacOptions ++= Seq("-Yresolve-term-conflict:object"),

            // add compiler settings
            ueberEntryPoints <<= baseDirectory(_ / "app" / "assets" / "javascripts" ** "script.js"),

            // customize assets compilation
            //resourceGenerators in Compile <<= lessComp(Seq(_)),
            resourceGenerators in Compile <<= LessCompiler(Seq(_)),
            resourceGenerators in Compile <+= ueberComp,

            // exclude old/repo resources
            excludeFilter in managedSources := excludes,
            excludeFilter in unmanagedSources := excludes,
            excludeFilter in managedResources := excludes,
            excludeFilter in unmanagedResources := excludes,

            mainClass := Some("play.core.server.NettyServer"),

            // overwrite default dependencies
            libraryDependencies := Seq(),

            // experimental feature: only compile changed files
            //incrementalAssetsCompilation := true,

            // only compile .less files in "root"
            lessEntryPoints <<= baseDirectory(_ / "app" / "assets" / "stylesheets" * "*.less")
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
        val Lift = "2.4"
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
        val Slf4j = "1.6.4"
        val Snappy = "1.0.4.1"
        val Specs2 = "1.11"
        val Spray = "1.0-M2"
        val Squeryl = "0.9.5"
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
    val metrics = "com.yammer.metrics" %% "metrics-scala" % V.Metrics
    val metricsGraphite = "com.yammer.metrics" % "metrics-graphite" % V.Metrics
    val memcache = "com.googlecode.xmemcached" % "xmemcached" % V.XMemcached
    val mongoDb = "org.mongodb" % "mongo-java-driver" % V.MongoDB
    val playWeb = ("play" %% "play" % V.Play) excludeAll(
        ExclusionRule(organization = "org.springframework"),
        ExclusionRule(organization = "net.sf.ehcache"),
        ExclusionRule(organization = "com.codahale"),
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

// ================================================================================================
// COMPILER: Ueber
// ================================================================================================

object Compiler4Ueber extends Env {

    import java.io._

    def compile(source: File): (String, Option[String], Seq[File]) = {

        //val start = new Date
        val srcDir = source.getParentFile
        println(" - compiling '" + srcDir.getName + "'")

        val viewDir = new File(srcDir, "views")
        val hasViews = viewDir.exists()

        val moduleDir = new File(srcDir, "modules")
        val hasModules = moduleDir.exists()

        // COMPILE: VIEWS
        val views =
            if (hasViews) Compiler4Handlebars.compile(viewDir, Seq()) + "\n"
            else "\n"

        // COMPILE: COFFEE
        val modules =
            if (hasModules) {
                val coffeeFiles = filesWithExt(moduleDir, ".coffee")
                Compiler4CoffeeScript.compile(coffeeFiles)
            }
            else "\n"

        // COMPILE: JS
        val script = new File(srcDir, "script.js")
        val js = Compiler4Closure.compile(script)._1 + views + modules
        val res = (js, if (isCloud) Some(Compiler4Closure.minify(js)) else Some(js), Seq(source))
        //println("time: " + (new Date().getTime - start.getTime) / 1000 + "s")
        res
    }
}

// ================================================================================================
// COMPILER: Handlebars
// ================================================================================================

object Compiler4Handlebars extends Env {

    import java.io._
    import sbt.IO
    import org.mozilla.javascript._
    import org.mozilla.javascript.tools.shell._
    import org.mozilla.javascript.JavaScriptException
    import play.core.coffeescript.CompilationException

    def compile(source: File, options: Seq[String]): String = {
        try {
            val views = filesWithExt(source, ".view")

            if (isCloud) {
                val templates = compressor(views)
                compiler(source, templates, options)
            } else
                try {
                    NodeJs.call(Seq("handlebars.cmd") ++ views.map(_.getAbsolutePath))
                        .replaceAllLiterally(".view']", "']")
                } catch {
                    case e: JavaScriptException =>
                        throw CompilationException(e.getValue.toString, source, None)
                }
        } catch {
            case e: JavaScriptException => {
                throw e
            }
        }
    }

    private lazy val compressor = {

        import com.googlecode.htmlcompressor.compressor._

        val compressor = new HtmlCompressor()
        compressor.setRemoveIntertagSpaces(true)

        (sources: Seq[File]) =>
            sources.map {
                f =>
                    val data = IO.read(f)
                    (f.getName, compressor.compress(data))
            }
    }

    private lazy val compiler = {
        val ctx = Context.enter
        val global = new Global;
        global.init(ctx)
        val scope = ctx.initStandardObjects(global)

        val wrappedHandlebarsCompiler = Context.javaToJS(this, scope)
        ScriptableObject.putProperty(scope, "HandlebarsCompiler", wrappedHandlebarsCompiler)

        val compilerJs = new File(new File("").getAbsolutePath, "project/handlebars.js")
        ctx.evaluateReader(scope, new InputStreamReader(compilerJs.toURI.toURL.openConnection().getInputStream), "handlebars.js", 1, null)

        val handlebars = scope.get("Handlebars", scope).asInstanceOf[NativeObject]
        val compilerFn = handlebars.get("precompile", scope).asInstanceOf[Function]

        Context.exit()

        (folder: File, sources: Seq[(String, String)], options: Seq[String]) => {
            val buffer = new StringBuffer()
            buffer.append("(function() { var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};\n");
            sources.foreach {
                s =>
                    val opts = ctx.newObject(scope)
                    //options.foreach(o => options.put("-k", options, o))
                    val compiled = Context.call(null, compilerFn, scope, scope, Array(s._2, opts)).asInstanceOf[String]
                    buffer.append("\ntemplates['" + s._1.substring(0, s._1.indexOf(".view")) + "'] = template(" + compiled + ");\n")
            }
            buffer.append("})();")
            buffer.toString
        }
    }
}

// ================================================================================================
// COMPILER: Coffeescript
// ================================================================================================

object Compiler4CoffeeScript extends Env {

    import play.core.coffeescript._
    import org.mozilla.javascript.JavaScriptException
    import play.core.coffeescript.CompilationException

    def compile(files: Array[File]): String =
        (files.map {
            f =>
                if (isCloud)
                    CoffeescriptCompiler.compile(f, Seq("bare"))
                else
                    extCompile(f)
        }).mkString("\n") + "\n"


    private def extCompile(file: File) =
        try {
            NodeJs.call(Seq("coffee.cmd", "--print", "--bare", file.getAbsolutePath))
        } catch {
            case e: JavaScriptException =>
                throw CompilationException(e.getValue.toString, file, None)
        }
}

// ================================================================================================
// COMPILER: JS
// ================================================================================================

object Compiler4Closure {

    import com.google.javascript.jscomp._
    import sbt.PlayExceptions.AssetCompilationException
    import java.io._

    /**
     * Compile a JS file with its dependencies
     * @return a triple containing the original source code, the minified source code, the list of dependencies (including the input file)
     */
    def compile(source: File, coptions: Seq[String] = Seq()): (String, Option[String], Seq[File]) = {
        import scala.util.control.Exception._

        val options = new CompilerOptions()
        options.prettyPrint = true // no obfuscation / minification
        options.closurePass = true // processes goog.provide() and goog.require() calls
        options.setManageClosureDependencies(true)

        val compiler = new Compiler()
        val extern = JSSourceFile.fromCode("externs.js", "function alert(x) {}")
        val all = allSiblings(source)
        val input = all.map(f => JSSourceFile.fromFile(f)).toArray

        catching(classOf[Exception]).either(compiler.compile(extern, input, options).success) match {
            case Right(true) =>
                val src = compiler.toSource
                (src, None, all)
            case Right(false) => {
                val error = compiler.getErrors.head
                val errorFile = all.find(f => f.getAbsolutePath == error.sourceName)
                throw AssetCompilationException(errorFile, error.description, error.lineNumber, 0)
            }
            case Left(exception) =>
                exception.printStackTrace()
                throw AssetCompilationException(Some(source), "Internal Closure Compiler error (see logs)", 0, 0)
        }
    }

    /**
     * Minify a Javascript string
     */
    def minify(source: String, name: Option[String] = None): String = {

        val compiler = new Compiler()
        val extern = JSSourceFile.fromCode("externs.js", "function alert(x) {}")

        val options = new CompilerOptions()
        CompilationLevel.SIMPLE_OPTIMIZATIONS.setOptionsForCompilationLevel(options)

        val input = JSSourceFile.fromCode(name.getOrElse("unknown"), source)

        compiler.compile(extern, input, options).success match {
            case true => compiler.toSource
            case false => {
                val error = compiler.getErrors.head
                throw AssetCompilationException(None, error.description, error.lineNumber, 0)
            }
        }
    }

    /**
     * Return all Javascript files in the same directory than the input file, or subdirectories
     */
    private def allSiblings(source: File): Seq[File] = allJsFilesIn(source.getParentFile)

    private def allJsFilesIn(dir: File): Seq[File] = {
        val jsFiles = dir.listFiles(new FileFilter {
            override def accept(f: File) = f.getName.endsWith(".js")
        })
        val directories = dir.listFiles(new FileFilter {
            override def accept(f: File) = f.isDirectory
        })
        val jsFilesChildren = directories.map(d => allJsFilesIn(d)).flatten
        jsFiles ++ jsFilesChildren
    }

    /**
     * Turns a filename into a JS identifier that is used for moduleNames in
     * rewritten code. Removes leading ./, replaces / with $, removes trailing .js
     * and replaces - with _. All moduleNames get a "module$" prefix.
     */
    private def toModuleName(filename: String) = {
        "module$" + filename.replaceAll("^\\./", "").replaceAll("/", "\\$").replaceAll("\\.js$", "").replaceAll("-", "_");
    }

}

// ================================================================================================
// COMPILER: LESS
// ================================================================================================

/*
object Compiler4Less extends Env {

    import play.core.less.LessCompiler
    import sbt.PlayExceptions.AssetCompilationException

    def compile(file: File): (String, Option[String], scala.Seq[File]) =
        if(isCloud)
            LessCompiler.compile(file)
        else
            extCompile(file)

    private def extCompile(file: File) =
        try {
            val r = NodeJs.call(Seq("lessc.cmd", file.getAbsolutePath))
            r
        } catch {
            case e: JavaScriptException =>
                throw AssetCompilationException(Some(file), e.getValue.toString, 0, 0)
        }
}
*/

/*
object Compiler4Less {

    import sbt.PlayExceptions.AssetCompilationException
    import org.mozilla.javascript._
    import org.mozilla.javascript.tools.shell._
    import scalax.file._
    import java.io._

    private def compiler(minify: Boolean) = {
        val ctx = Context.enter
        val global = new Global;
        global.init(ctx)
        val scope = ctx.initStandardObjects(global)

        val wrappedLessCompiler = Context.javaToJS(this, scope)
        ScriptableObject.putProperty(scope, "LessCompiler", wrappedLessCompiler)

        ctx.evaluateString(scope,
            """
                var timers = [],
                    window = {
                        document: {
                            getElementById: function(id) {
                                return [];
                            },
                            getElementsByTagName: function(tagName) {
                                return [];
                            }
                        },
                        location: {
                            protocol: 'file:',
                            hostname: 'localhost',
                            port: '80'
                        },
                        setInterval: function(fn, time) {
                            var num = timers.length;
                            timers[num] = fn.call(this, null);
                            return num;
                        }
                    },
                    document = window.document,
                    location = window.location,
                    setInterval = window.setInterval;

            """,
            "browser.js",
            1, null)
        val compilerJs = new File(new File("").getAbsolutePath, "project/less.js")
        val lib = new InputStreamReader(compilerJs.toURI.toURL.openConnection().getInputStream)
        ctx.evaluateReader(scope, lib, "less-1.3.0.js", 1, null)
        ctx.evaluateString(scope,
            """
 var compile = function(source) {

     var compiled;
     var dependencies = [source];

     window.less.Parser.importer = function(path, paths, fn, env) {
         var imported = LessCompiler.resolve(source, path);
         var input = String(LessCompiler.readContent(imported));
         dependencies.push(imported)
         new(window.less.Parser)({
             optimization:3,
             filename:path
         }).parse(input, function (e, root) {
             if(e instanceof Object) {
                 throw e;
             }
             fn(e, root, input);
         });
     }

     new(window.less.Parser)({optimization:3, filename:String(source.getCanonicalPath())}).parse(String(LessCompiler.readContent(source)), function (e,root) {
         if(e instanceof Object) {
             throw e;
         }
         compiled = root.toCSS({compress: """ + (if (minify) "true" else "false") + """})
                        })

                        return {css:compiled, dependencies:dependencies}
                    }
                """,
            "compiler.js",
            1, null)
        val compilerFunction = scope.get("compile", scope).asInstanceOf[Function]

        Context.exit()

        (source: File) => {
            val result = Context.call(null, compilerFunction, scope, scope, Array(source)).asInstanceOf[Scriptable]
            val css = ScriptableObject.getProperty(result, "css").asInstanceOf[String]
            val dependencies = ScriptableObject.getProperty(result, "dependencies").asInstanceOf[NativeArray]

            css -> (0 until dependencies.getLength.toInt).map(ScriptableObject.getProperty(dependencies, _) match {
                case f: File => f
                case o: NativeJavaObject => o.unwrap.asInstanceOf[File]
            })
        }
    }

    private lazy val debugCompiler = compiler(false)

    private lazy val minCompiler = compiler(true)

    def compile(source: File): (String, Option[String], Seq[File]) = {
        try {
            val debug = debugCompiler(source)
            val min = minCompiler(source)
            (debug._1, Some(min._1), debug._2)
        } catch {
            case e: JavaScriptException => {

                val error = e.getValue.asInstanceOf[Scriptable]
                val filename = ScriptableObject.getProperty(error, "filename").asInstanceOf[String]
                val file = if (filename == source.getAbsolutePath) source else resolve(source, filename)
                throw AssetCompilationException(Some(file),
                    ScriptableObject.getProperty(error, "message").asInstanceOf[String],
                    ScriptableObject.getProperty(error, "line").asInstanceOf[Double].intValue,
                    ScriptableObject.getProperty(error, "column").asInstanceOf[Double].intValue)
            }
        }
    }

    def readContent(file: File) = Path(file).slurpString.replace("\r", "")

    def resolve(originalSource: File, imported: String) = new File(originalSource.getParentFile, imported)
}
*/

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

    def call(cmd: Seq[String]) = {
        // I/O
        val stderr = new StringBuffer()
        val stdout = new StringBuffer()
        val logger = ProcessLogger(
            (out: String) => {
                stdout append (out + "\n")
            },
            (err: String) => {
                stdout append (err + "\n")
            })

        // exec
        val process = Process(cmd.mkString(" "))
        val exit = process ! logger
        val out = stdout.toString

        if (exit != 0)
            throw new JavaScriptException(out)
        out
    }
}