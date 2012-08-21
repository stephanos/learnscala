package de.learnscala.specs

import de.learnscala.uebungen.U100._
import de.learnscala.base.BaseTest

class S100 extends BaseTest {

    spec("U100", enabled) {

        "t1 must be '13:37'" >> {
            t1 must not (beNull)
            t1.toString === "13:37"
        }
        "t2 must be '20:00'" >> {
            t2 must not (beNull)
            t2.toString === "20:00"
        }
        "t3 must be '0:00'" >> {
            t3 must not (beNull)
            t3.toString === "0:00"
        }
        "t4 must be '14:00'" >> {
            t4 must not (beNull)
            t4.toString === "14:00"
        }
        "t5 must be '0:01'" >> {
            t5 must not (beNull)
            t5.toString === "0:01"
        }
        "t6 must be '2:00'" >> {
            t6 must not (beNull)
            t6.toString === "2:00"
        }
    }
}