package de.learnscala.api.impl

import java.io.PrintStream
import scala.annotation.tailrec
import scala.tools.nsc.interpreter.IR
import scala.tools.nsc.interpreter.Results._

import scala.concurrent._
import Future._
import duration._
import Duration._
import Await._
import ExecutionContext.Implicits.global

// see https://github.com/vydra/gae-scala/tree/master/src
class Scala {

    import CodeUtil._

    def compile(code: String) = {
        val (compiler, out) = encoder()
        (compiler.compileString(code), asString(out))
    }

    def decode(code: String) = {
        val scala = new ScalaDecoder()
        scala.exec(code)
    }

    def interpret(code: (String, String)): (Result, String) =
        runWithTimeout[(Result, String)](5000, (IR.Error, "timeout")) {
            val (compiler, out) = encoder()
            val r = Console.withOut(new PrintStream(out, true)) {
                @tailrec
                def eval(lines: List[String], buffer: List[String] = List(), state: Result = IR.Success): Result =
                    buffer match {
                        case List() =>
                            lines match {
                                case Nil => state // stop
                                case x :: xs => eval(xs, List(x), state)
                            }
                        case xs =>
                            val code = xs.mkString("\n")
                            //val log = com.loops101.util.Logger("controllers")
                            //log.info(code)
                            (if(ignoreCodeLine(code)) state else compiler.interpret(code)) match {
                                case ir@IR.Success =>
                                    //log.info("{}", ir)
                                    lines match {
                                        case Nil => ir // stop
                                        case y :: ys => eval(ys, List(y), ir)
                                    }
                                case ir@IR.Incomplete =>
                                    //log.info("{}", ir)
                                    lines match {
                                        case Nil => ir // // stop
                                        case y :: ys => eval(ys, buffer ::: List(y), ir)
                                    }
                                case ir =>
                                    ir // stop
                            }
                    }
                eval(List(code._1))
                eval(code._2.split('\n').toList)
            }
            val s = asString(out)
            (r, s)
        }

    private def ignoreCodeLine(line: String) = {
        val tline = line.trim
        tline.isEmpty || tline.startsWith("//") || tline.startsWith("/*")
    }

    private def runWithTimeout[T](timeoutMs: Long)(f: => T): T = {
        val fut = Future[T](f)
        val tout: Duration = timeoutMs.seconds
        Await.result[T](fut, tout)
    }

    private def runWithTimeout[T](timeoutMs: Long, default: T)(f: => T): T = {
        runWithTimeout(timeoutMs)(f)
    }
}

object Compiler {

    def apply(code: String) =
        new Scala().compile(code)
}

object Decoder {

    def apply(code: String) =
        new Scala().decode(code)
}

object Interpreter {

    def apply(code: String) =
        new Scala().interpret(("", code))

    def apply(code: (String, String)) =
        new Scala().interpret(code)
}