package de.learnscala.base

import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import org.specs2.specification._
import org.specs2.mock.Mockito
import org.specs2.ScalaCheck

@RunWith(classOf[JUnitRunner])
abstract class BaseTest
    extends SpecificationWithJUnit
    with Mockito with ScalaCheck
    with Reflect with Push with Capture {

    // execute sequentially
    sequential

    // stop execution on first fail / skip
    stopOnFail
    stopOnSkip


    protected def spec(name: String, enabled: Boolean)(fn: => Fragment): Fragments =
        name should {
            if (enabled)
                fn
            else
                skipped
        }

    protected def checkType[T](v: Any, n: String, enabled: Boolean)(fn1: (T) => Fragment)(implicit t: Manifest[T]): Fragment = {
        // is null ?
        if(enabled) {
            println(enabled)
            "'" + n + "' must not be null" >> {
                v aka n must not beNull
            }

            // is right type?
            /*
            "'" + n + "' must have type " + t.erasure.getName >> {
                v aka n must haveClass[T]
            }
            */

            try {
                fn1(v.asInstanceOf[T])
            } catch {
                case e: Throwable =>
                    1 === 1
            }
        } else
            skipped
    }

    protected def add(f: => Fragment) =
        addFragments(f)
}