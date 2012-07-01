import com.loops101.UnitSpec
import services.code._
import scala.tools.nsc.interpreter.IR

class CompilerSpec extends UnitSpec {

    "compiler" >> {

        "interpret" >> {

            "right expression" >> {
                val r = Interpreter("val i = 42")
                println(r._2)
                r._1 === IR.Success
            }

            "wrong expression" >> {
                val r = Interpreter("val i = 42b")
                println(r._2)
                r._1 === IR.Error
            }

            "incomplete expression" >> {
                val r = Interpreter("def func = {")
                println(r._2)
                r._1 === IR.Incomplete
            }

            "class definition" >> {
                val r1 = Interpreter("class Car")
                println(r1._2)
                r1._1 === IR.Success

                val r2 = Interpreter("class Car")
                println(r2._2)
                r2._1 === IR.Success
            }

            "complex interaction" >> {
                val r1 = Interpreter("val i = 42; val i2 = i * 2; val f = i.toFloat")
                println(r1._2)
                r1._1 === IR.Success
            }
        }

        "compile" >> {
            "class definition" >> {
                val r = Compiler("class Car")
                println(r._2)
                r._1 === true
            }

            "duplicate class definition" >> {
                val r = Compiler("class Car\nclass Car")
                println(r._2)
                r._1 === false
            }

            "simple expression" >> {
                val r = Compiler("val i = 42")
                println(r._2)
                r._1 === false
            }

            "incomplete expression" >> {
                val r = Compiler("def func = {")
                println(r._2)
                r._1 === false
            }
        }
    }
}
