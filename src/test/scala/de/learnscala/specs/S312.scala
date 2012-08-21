package de.learnscala.specs

import de.learnscala.uebungen.U312._
import de.learnscala.base.BaseTest

class S312 extends BaseTest {

    spec("U312", enabled) {

        "shout must print '#SCALA#'" >> {
            captureOutput(shout())._2.trim === "#SCALA#"
        }

        "google must print '[http://google.com]'" >> {
            captureOutput(google())._2.trim === "[http://google.com]"
        }
    }
}