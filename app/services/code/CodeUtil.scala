package services.code

import scala.tools.nsc.Settings
import scala.tools.nsc.interpreter.IMain
import java.io._

object CodeUtil {

    def instance(verbose: Boolean = false): (IMain,ByteArrayOutputStream) =
    {
        val s = new Settings()
        s.verbose.value_$eq(verbose)
        s.usejavacp.value_$eq(true)
        //s.deprecation.value_$eq(true)
        //s.unchecked.value_$eq(true)
        s

        val out = new ByteArrayOutputStream()
        (new IMain(s, new PrintWriter(new OutputStreamWriter(out))), out)
    }

    //~ INTERNALS =====================================================================================================

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
    */

    /*
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
