package services.code

import scala.tools.nsc.Settings
import scala.tools.nsc.reporters._
import scala.tools.nsc.io.VirtualDirectory
import scala.tools.nsc.util._

// see https://github.com/vydra/gae-scala/tree/master/src
class Compiler {

    def compile(code: String, fname: String = "Code.scala") = {
        val cr = new scalac.Run
        cr.compileSources(List(new BatchSourceFile(fname, code.toCharArray)))
        println(reporter.infos)
    }

    //~ INTERNALS =====================================================================================================

    private val scalac =
        new scala.tools.nsc.Global(settings, reporter)

    private val virtualDirectory =
        new VirtualDirectory("mem", None)

    private val reporter =
        new StoreReporter()

    // settings
    private val settings = new Settings()
    settings.classpath.value_$eq("")
    settings.bootclasspath.value_$eq("")
    settings.outputDirs.setSingleOutput(virtualDirectory)
    settings.extdirs.value_$eq("")
    settings.verbose.value_$eq(false)
    settings.deprecation.value_$eq(true)
    settings.unchecked.value_$eq(true)

    /*
    private def getJarFromResource(resource: URL): String = {
        val path = resource.toString
        val indexOfFile = path.indexOf("file:")
        val indexOfSeparator = path.lastIndexOf('!')
        path.substring(indexOfFile, indexOfSeparator)
    }

    private val rtJarPath =
        getJarFromResource(getClass.getResource("/java/lang/Object.class"))

    private val scalaLibPath =
        getJarFromResource(getClass.getResource("/scala/Array.class"))

    private val codebasePath =
        rtJarPath + " " + scalaLibPath

    private val classpath: List[URL] = {
        val classpathPart = (ClassPath.expandPath(settings.classpath.value).map(s => new File(s).toURI.toURL))
        def parseURL(s: String): Option[URL] =
            try
                Some(new URL(s))
            catch {
                case _: MalformedURLException => None
            }
        val codebasePart = (settings.Xcodebase.value.split(" ")).toList.flatMap(parseURL)
        classpathPart //::: codebasePart
    }
    */
}

object Compiler {

    def apply(code: String) =
        new Compiler().compile(code)
}