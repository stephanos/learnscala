package de.learnscala.specs

import de.learnscala.uebungen.U110._
import de.learnscala.base.BaseTest

class S110 extends BaseTest {

    spec("U110", enabled) {

        "Point" >> {
            "must create instance for coordinates (5,5)" >> {
                checkType[Point](p1, "p1", enabled) {
                    p =>
                        p.x === 5
                        p.y === 5
                }
            }

            "must create instance with coordinates (5,5) for parameter '5'" >> {
                checkType[Point](p2, "p2", enabled) {
                    p =>
                        p.x === 5
                        p.y === 5
                }
            }
            "must throw exception for 2 negative coordinates" >> {
                p3 must throwA[Throwable]
            }
            "must throw exception for 1 negative coordinate" >> {
                p4 must throwA[Throwable]
            }
            "object must return count '2'" >> {
                Point.getCount === 2
            }
        }
    }
}