package de.learnscala.specs

import de.learnscala.uebungen.U030._
import de.learnscala.base.BaseTest

class S030 extends BaseTest {

    spec("U030", enabled) {

        "isTheAnswerToLifeTheUniverseAndEverything" >> {

            "must be 'true' for value '42'" >> {
                isTheAnswerToLifeTheUniverseAndEverything(42) === true
            }
            "must be 'false' for values NOT '42'" >> {
                isTheAnswerToLifeTheUniverseAndEverything(0) === false
                isTheAnswerToLifeTheUniverseAndEverything(100) === false
                isTheAnswerToLifeTheUniverseAndEverything(-100) === false
            }
        }

        "signum" >> {

            "must be '-1' for negative value" >> {
                signum(-100) === -1
                signum(-1) === -1
            }
            "must be '1' for positive value" >> {
                signum(100) === 1
                signum(1) === 1
            }
            "must be '0' for '0'" >> {
                signum(0) === 0
            }
        }

        "absMax" >> {

            "must be '100' for '200' and '200'" >> {
                absMax(100, 200) === 200
            }
            "must be '200' for '100' and '200'" >> {
                absMax(200, 100) === 200
            }
            "must be '100' for '100' and '100'" >> {
                absMax(100, 100) === 100
            }
            "must be '-100' for '-100' and '-100'" >> {
                absMax(-100, -100) === -100
            }
            "must be '0' for '0' and '0'" >> {
                absMax(0, 0) === 0
            }
            "must be '-100' for '200' and '200'" >> {
                absMax(-100, 200) === 200
            }
            "must be '200' for '-100' and '200'" >> {
                absMax(200, -100) === 200
            }
            "must be '-200' for '100' and '-200'" >> {
                absMax(-200, 100) === -200
            }
        }
    }
}