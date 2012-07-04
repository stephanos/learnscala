package services.code

import java.io.PrintStream
import scala.actors.Futures._
import scala.annotation.tailrec
import scala.tools.nsc.interpreter._
import scala.tools.nsc.interpreter.Results._

// see https://github.com/vydra/gae-scala/tree/master/src
class Scala {

    import CodeUtil._

    def compile(code: String) = {
        val (compiler, out) = encoder()
        (compiler.compileString(code), asString(out))
    }

    def decode(code: String) = {
        val scala = decoder()
        scala.exec(code)
    }

    def interpret(code: String) = {
        future {
            val (compiler, out) = encoder()
            val r = Console.withOut(new PrintStream(out, true)) {
                @tailrec
                def eval(lines: List[String], buffer: List[String] = List(), state: Result = IR.Success): Result =
                    buffer match {
                        case List() =>
                            lines match {
                                case Nil => state // stop
                                case x :: xs => eval(xs, List(x))
                            }
                        case xs =>
                            val code = xs.mkString("\n")
                            val log = com.loops101.util.Logger("controllers")
                            log.info(code)
                            compiler.interpret(code) match {
                                case ir @ IR.Success =>
                                    lines match {
                                        case Nil => state
                                        case y :: ys =>  eval(lines.tail, state = ir)
                                    }
                                case ir @ IR.Incomplete =>
                                    lines match {
                                        case Nil => state
                                        case y :: ys => eval(lines.tail, buffer ::: List(lines.head), state = ir)
                                    }
                                case ir => ir // stop
                            }
                }
                eval(code.split('\n').toList)
            }
            val s = asString(out)
            (r, s)
        }()
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

    import CodeUtil._

    def apply(code: String, session: Option[String] = None) =
        new Scala().interpret(withSession(code, session))
}