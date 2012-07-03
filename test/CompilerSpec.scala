import com.loops101.UnitSpec
import services.code._
import scala.tools.nsc.interpreter.IR

class CompilerSpec extends UnitSpec {

    "compiler" >> {

        "interpret" >> {

            "right expression" >> {
                val r = Interpreter("val i = 42")

                r._1 === IR.Success
                r._2 must contain("i: Int = 42")
            }

            "wrong expression" >> {
                val r = Interpreter("val i = 42b")

                r._1 === IR.Error
                r._2 must contain ("Invalid literal number")
            }

            "incomplete expression" >> {
                val r = Interpreter("def func = {")

                r._1 === IR.Incomplete
                r._2 === ""
            }

            "class definition" >> {
                val r = Interpreter("class Car")

                r._1 === IR.Success
                r._2 must contain ("defined class Car")
            }

            "complex interaction" >> {
                val r = Interpreter("val i = 42; val i2 = i * 2; val f = i.toFloat")

                r._1 === IR.Success
                r._2 must contain ("f: Float = 42.0")
            }
        }

        "compile" >> {
            "class definition" >> {
                val r = Compiler("class Car")

                r._1 === true
                r._2 === ""
            }

            "duplicate class definition" >> {
                val r = Compiler("class Car\nclass Car")

                r._1 === false
                r._2 must contain ("Car is already defined")
            }

            "simple expression" >> {
                val r = Compiler("val i = 42")

                r._1 === false
                r._2 must contain ("expected class or object")
            }

            "incomplete expression" >> {
                val r = Compiler("def func = {")

                r._1 === false
                r._2 must contain ("expected class or object")
            }
        }

        "decode " >> {
            "invalid code" >> {
                val r = Decoder("class 2Car")

                r._1 === null
                r._2 must contain ("error")
            }

            "valid code" >> {
                val r = Decoder("class Car")

                r._1 must contain("class Car extends scala.AnyRef")
                r._2 === ""
            }
        }
    }
}
