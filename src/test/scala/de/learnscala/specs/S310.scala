package de.learnscala.specs

import de.learnscala.uebungen.U310._
import de.learnscala.base.BaseTest

class S310 extends BaseTest {

    spec("U310", enabled) {

        "r1 must return (2,3,4) for (1,2,3)" >> {
            r1(List(1, 2, 3)) === List(2, 3, 4)
        }
        "r2 must return (4,6,8) for (1,2,3)" >> {
            r2(List(1, 2, 3)) === List(4, 6, 8)
        }
        "r3 must return () for (3,5,7)" >> {
            r3(List(1, 2, 3)) === List(3, 5, 7)
        }
        "r4 must return () for (1,2,3)" >> {
            r4(List(1, 2, 3)) === List(3, 5, 7)
        }
        "r5 must return () for ((1,3),(2,4),(3,5))" >> {
            r5(List((1, 3), (2, 4), (3, 5))) === List((2, 2), (3, 3), (4, 4))
        }
    }
}