package de.learnscala.specs

import de.learnscala.uebungen.U410._
import de.learnscala.base.BaseTest

class S410 extends BaseTest {

    spec("U410", enabled) {

        "+%" >> {
            "must increase value by percentage" >> {
                plus50p === 225
            }

        }

        "-%" >> {
            "must decrease value by percentage" >> {
                minus50p === 75
            }
        }
    }
}