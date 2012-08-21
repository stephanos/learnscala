package de.learnscala.specs

import de.learnscala.uebungen.U230
import de.learnscala.uebungen.U230._
import de.learnscala.base.BaseTest

class S230 extends BaseTest {

    spec("U230", enabled) {

        "toString must return '[1,2,3]' for (1,2,3)" >> {
            U230.toString(List(1, 2, 3)) === "[1,2,3]"
        }

        "join must return '(1,2,3,4)' for (1,2) and (3,4)" >> {
            join(List(1, 2), List(3, 4)) === List(1, 2, 3, 4)
        }

        "first2 must return '(1,2)' for (1,2,3,4)" >> {
            first2(List(1, 2, 3, 4)) === List(1, 2)
        }

        "sumOf must return '5' for (1,2,3)" >> {
            sumOf(List(1, 2, 3)) === 6
        }

        "withoutFirst must return '(2, 3)' for (1,2,3)" >> {
            withoutFirst(List(1, 2, 3)) === List(2, 3)
        }

        "biggest must return '3' for (1,2,3)" >> {
            biggest(List(1, 2, 3)) === 3
        }


        /////////////////////////////////////////////////////////
        // List

        "makeList must return (1,2,3) for 1, 2 and 3" >> {
            makeList(1, 2, 3) === List(1, 2, 3)
        }

        "unique must return (1,2,3) for (1,2,2,3,3)" >> {
            unique(List(1, 2, 2, 3, 3)) === List(1, 2, 3)
        }

        "backwards must return (3,2,1) for (1,2,3)" >> {
            backwards(List(1, 2, 3)) === List(3, 2, 1)
        }


        /////////////////////////////////////////////////////////
        // Set

        "has42and101" >> {
            "must return 'true' for (1,42,101)" >> {
                has42and101(Set(1, 42, 101)) === true
            }
            "must return 'false' for (1,42)" >> {
                has42and101(Set(1, 42)) === false
            }
            "must return 'false' for (1,101)" >> {
                has42and101(Set(1, 101)) === false
            }
        }

        "mergeSet must return (1,2,3,4) for (1,2) and (3,4)" >> {
            mergeSet(Set(1, 2), Set(3, 4)) === Set(1, 2, 3, 4)
        }


        /////////////////////////////////////////////////////////
        // Map

        "withoutKey42 must return '(101 -> false)' for (42 -> true, 101 -> false)" >> {
            withoutKey42(Map(42 -> true, 101 -> false)) === Map(101 -> false)
        }

        "hasKey42" >> {
            "must return 'true' for (42 -> true, 101 -> false)" >> {
                hasKey42(Map(42 -> true, 101 -> false)) === true
            }
            "must return 'false' for (43 -> true, 101 -> false)" >> {
                hasKey42(Map(43 -> true, 101 -> false)) === false
            }
        }
    }
}