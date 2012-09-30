import com.loops101.UnitSpec
import de.learnscala.api.impl._
import scala.tools.nsc.interpreter.IR

class CompilerSpec extends UnitSpec {

    "compiler" >> {

        "interpret" >> {

            "right expression" >> {
                val (r, out) = Interpreter("val i = 42")

                r === IR.Success
                out must contain("i: Int = 42")
            }

            "wrong expression" >> {
                val (r, out) = Interpreter("val i = 42b")

                r === IR.Error
                out must contain ("Invalid literal number")
            }

            "incomplete expression" >> {
                val (r, out) = Interpreter("def func = {")

                r === IR.Incomplete
                out === ""
            }

            "class definition" >> {
                val (r, out) = Interpreter("class Car")

                r === IR.Success
                out must contain ("defined class Car")
            }

            "complex interaction" >> {
                val (r, out) = Interpreter("val i = 42; val i2 = i * 2; val f = i.toFloat")

                r === IR.Success
                out must contain ("f: Float = 42.0")
            }
        }

        "compile" >> {
            "class definition" >> {
                val (r, out) = Compiler("class Car")

                r === true
                out === ""
            }

            "duplicate class definition" >> {
                val (r, out) = Compiler("class Car\nclass Car")

                r === false
                out must contain ("Car is already defined")
            }

            "simple expression" >> {
                val (r, out) = Compiler("val i = 42")

                r === false
                out must contain ("expected class or object")
            }

            "incomplete expression" >> {
                val (r, out) = Compiler("def func = {")

                r === false
                out must contain ("expected class or object")
            }
        }

        "decode " >> {
            "invalid code" >> {
                val (r, out) = Decoder("class 2Car")

                r === null
                out must contain ("error")
            }

            "valid code" >> {
                val (r, out) = Decoder("class Car")

                r must contain("class Car extends scala.AnyRef")
                out === ""
            }
        }
    }
}
