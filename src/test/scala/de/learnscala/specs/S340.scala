package de.learnscala.specs

import de.learnscala.uebungen.U340._
import de.learnscala.base.BaseTest

class S340 extends BaseTest {

    spec("U340", enabled) {

        "incr must be 10" >> {
            incr === 10
        }
    }
}