package de.learnscala.specs

import de.learnscala.uebungen.U040._
import de.learnscala.base.BaseTest

class S040 extends BaseTest {

    spec("U040", enabled) {

        "countingOut" >> {
            "must print numbers from 1 to 10" >> {
                captureOutput(countingOut())._2.trim === "1 2 3 4 5 6 7 8 9 10"
            }
        }

        "count10s" >> {
            "must print multiples of 10 between 10 and 100" >> {
                captureOutput(count10s())._2.trim === "10 20 30 40 50 60 70 80 90 100"
            }
        }

        "countMod" >> {
            "must print only even numbers for argument '2'" >> {
                captureOutput(countMod(2))._2.trim === "2 4 6 8 10"
            }
            "must print only numbers divisible by 3 for argument '3'" >> {
                captureOutput(countMod(3))._2.trim === "3 6 9"
            }
        }
    }
}