package services.code

import java.io.PrintStream
import scala.actors.Futures._

// see https://github.com/vydra/gae-scala/tree/master/src
class Scala {

    import CodeUtil._

    def compile(code: String) = {
        val scala = encoder()
        (scala._1.compileString(code), asString(scala._2))
    }

    def decode(code: String) = {
        val scala = decoder()
        scala.exec(code)
    }

    def interpret(code: String) = {
        future {
            val (compiler, out) = encoder()
            val r = Console.withOut(new PrintStream(out, true)) {
                compiler.interpret(code)
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