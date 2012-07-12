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
        new ScalaDecoder()

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

    class JavaDecoder {
        def exec(code: String): (String, String) = {
            //val t = sun.tools.javap.Main
            null
        }
    }

    class ScalaDecoder {
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

        private def printFile(file: AbstractFile, out: OutputStream) {
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