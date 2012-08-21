package de.learnscala.specs

import de.learnscala.base.BaseTest
import de.learnscala.uebungen.U221._

class S221 extends BaseTest {

    spec("U221", enabled) {

        "swap" >> {
            "must return (5,1) for (1,5)" >> {
                swap(5, 1) ===(1, 5)
            }
            "must return (1,1) for (1,1)" >> {
                swap(1, 1) ===(1, 1)
            }
        }

        "sum" >> {
            "must return 5 for (true,2,3)" >> {
                sum((true, 2, 3)) === 5
            }
            "must return 2 for (true,1,1)" >> {
                sum((true, 1, 1)) === 2
            }
            "must return -1 for (true,2,3)" >> {
                sum((false, 2, 3)) === -1
            }
            "must return 1 for (true,9,8)" >> {
                sum((false, 9, 8)) === 1
            }
        }

        "doYouBuyTheCar" >> {
            "must print 'yes' for Some(true)" >> {
                captureOutput(doYouBuyTheCar(Some(true)))._2.trim === "yes"
            }
            "must print 'no' for Some(false)" >> {
                captureOutput(doYouBuyTheCar(Some(false)))._2.trim === "no"
            }
            "must print 'not sure' for None" >> {
                captureOutput(doYouBuyTheCar(None))._2.trim === "not sure"
            }
        }

        "convertToInt" >> {
            "must return 'Some(5)' for '5'" >> {
                convertToInt("5") === Some(5)
            }
            "must return 'Some(-5)' for '-5'" >> {
                convertToInt("-5") === Some(-5)
            }
            "must return 'None' for 'a'" >> {
                convertToInt("a") === None
            }
        }
    }
}