package de.learnscala.specs

import de.learnscala.uebungen.U121._
import de.learnscala.base.BaseTest
import de.learnscala.uebungen._

class S121 extends BaseTest {

    spec("U121", enabled) {

        "convert" >> {
            "'m1' must be 1 Minute" >> {
                checkType[Minutes](m1, "m1", enabled) {
                    _.v === 1
                }
            }

            "'s120' must be 120 Seconds" >> {
                checkType[Seconds](s120, "s120", enabled) {
                    _.v === 120
                }
            }
        }

        1 === 1
    }
}

/*
"'m1' must be 1 Minute" >> {
    if (m1 == null) {
        m1 must haveClass[Minutes]
        m1.asInstanceOf[Minutes].v === 1
    } else {
        skipped("m1 is null")
    }
}

"'s120' must be 120 Seconds" >> {
    if (s120 != null) {
        s120 must haveClass[Minutes]
        s120.asInstanceOf[Minutes].v === 120
    } else {
        skipped("s120 is null")
    }
}
*/