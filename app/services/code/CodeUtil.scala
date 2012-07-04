package services.code

import java.io._
import scala.tools.util._
import scala.tools.nsc._
import scala.tools.nsc.io.{PlainFile, AbstractFile}
import scala.tools.nsc.util._
import scala.tools.nsc.interpreter.{IMain => Encoder, IR}
import scala.tools.nsc.reporters.Reporter
import scala.tools.scalap._
import com.loops101.util.EnvUtil
import scala.actors.Futures._
import scala.Some

object CodeUtil {

    def decoder() =
        new Decoder()

    def encoder(verbose: Boolean = false, unchecked: Boolean = false, deprecation: Boolean = false,
                out: ByteArrayOutputStream = new ByteArrayOutputStream): (Encoder, ByteArrayOutputStream) = {

        val s = new Settings()
        s.verbose.value = verbose
        s.optimise.value = false
        s.Yreplsync.value = true
        s.bootclasspath.value = scalaCP.map(_.toString).mkString(File.pathSeparator)
        s.deprecation.value = deprecation
        s.unchecked.value = unchecked

        (new Encoder(s, new PrintWriter(new OutputStreamWriter(out), true)) {
            // customize compiler initialization: remove SBT from classpath
            override protected def newCompiler(settings: Settings, reporter: Reporter) = {
                if (EnvUtil.isLocal) {
                    settings.outputDirs setSingleOutput virtualDirectory
                    settings.exposeEmptyPackage.value = true
                    new Global(settings, reporter) {
                        override lazy val classPath: ClassPath[_] = {
                            val paths = new PathResolver(settings) {
                                override def containers =
                                    super.containers.filterNot(_.asClasspathString contains "sbt")
                            }
                            val default = paths.result
                            val scala = scalaCP.map(p => new PlainFile(p)).map(new DirectoryClassPath(_, default.context))
                            new MergedClassPath[AbstractFile](default :: scala, default.context)
                        }
                    }
                } else
                    super.newCompiler(settings, reporter)
            }
        }, out)
    }

    def withSession(code: String, session: Option[String]) =
        session match {
            case Some(s) =>
                code // TODO
            case _ =>
                code
        }

    def asString(out: ByteArrayOutputStream) = {
        out.flush()
        new String(out.toByteArray, "UTF-8")
    }


    //~ INTERNALS =====================================================================================================

    class Decoder {
        def exec(code: String): (String, String) = {
            val scala = encoder()
            val compiler = scala._1
            if (compiler.compileString(code)) {
                val out = new ByteArrayOutputStream()
                printFile(compiler.virtualDirectory, out)
                (asString(out), asString(scala._2))
            } else
                (null, asString(scala._2))
        }

        private def printFile(file: AbstractFile, out: OutputStream): Unit = {
            if (file.isDirectory)
                for (f <- file.iterator)
                    printFile(f, out)
            else if (!file.name.contains("$repl")) {
                val reader = new ByteArrayReader(file.toByteArray)
                val clazz = new Classfile(reader)
                val writer = new OutputStreamWriter(out)
                new JavaWriter(clazz, writer).printClass()
                writer.flush()
            }
        }
    }

    lazy val scalaCP =
        List(ClassPath.info[Global], ClassPath.info[Dynamic]).map(_.locationFile)
}


/*
[init] [search path for source files: ]
[init] [search path for class files: D:\Applications\Java\jre7\lib\resources.jar;D:\Applications\Java\jre7\lib\rt.jar;D:\Applications\Java\jre7\lib\jsse.jar;D:\Applications\Java\jre7\lib\jce.jar;D:\Applications\Java\jre7\lib\charsets.jar;D:\Applications\Java\jre7\lib\ext\dnsns.jar;D:\Applications\Java\jre7\lib\ext\localedata.jar;D:\Applications\Java\jre7\lib\ext\sunec.jar;D:\Applications\Java\jre7\lib\ext\sunjce_provider.jar;D:\Applications\Java\jre7\lib\ext\sunmscapi.jar;D:\Applications\Java\jre7\lib\ext\zipfs.jar;D:\work\dev\tools\sbt\sbt-launch.jar;.;D:\work\.sbt\boot\scala-2.9.1\lib\scala-library.jar;D:\work\.sbt\boot\scala-2.9.1\lib\scala-compiler.jar]
[init] [loaded package loader resources.jar in 82ms]
[init] [loaded package loader scala in 1ms]
Exception in thread "Thread-5" java.lang.Error: typeConstructor inapplicable for <none>
	at scala.tools.nsc.symtab.SymbolTable.abort(SymbolTable.scala:34)
	at scala.tools.nsc.symtab.Symbols$Symbol.typeConstructor(Symbols.scala:877)
	at scala.tools.nsc.symtab.Definitions$definitions$.scala$tools$nsc$symtab$Definitions$definitions$$booltype(Definitions.scala:157)
	at scala.tools.nsc.symtab.Definitions$definitions$.init(Definitions.scala:814)
	at scala.tools.nsc.Global$Run.<init>(Global.scala:697)
*/


/*
                            val defaults =
                                default.asURLs
                                    .map(url => new PlainFile(Path(url.getFile))).map(new DirectoryClassPath(_, default.context))
                            */

/*val java =
List(javaBootClassPath).map(p => new DirectoryClassPath(new PlainFile(p), default.context))*/

/*
val scalaCP = (ClassPath expandDir "D:\\work\\dev\\tools\\scala\\lib").mkString(";")
val additional = List(javaBootClassPath, scalaCP)
    .map(p => new DirectoryClassPath(new PlainFile(p), default.context))
*/