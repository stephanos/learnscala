package services.code

// see https://github.com/vydra/gae-scala/tree/master/src
class Scala {

    import CodeUtil._

    def compile(code: String, fname: String = "Code.scala") = {
        val scala = instance()
        (scala._1.compileString(code), new String(scala._2.toByteArray, "UTF-8"))
    }

    def interpret(code: String) = {
        val scala = instance()
        (scala._1.interpret(code), new String(scala._2.toByteArray, "UTF-8"))
    }
}

object Compiler {

    def apply(code: String) =
        new Scala().compile(code)
}

object Interpreter {

    def apply(code: String) =
        new Scala().interpret(code)
}