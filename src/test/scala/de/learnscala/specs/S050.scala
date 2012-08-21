package de.learnscala.specs

import de.learnscala.uebungen.U050._
import de.learnscala.base.BaseTest

class S050 extends BaseTest {

    spec("U050", enabled) {

        "nameOfSymbol" >> {
            "must be 'Unbekannt' for value '?'" >> {
                nameOfSymbol('?') === "Unbekannt"
            }
            "must be 'Kreiszahl' for value 'Π'" >> {
                nameOfSymbol('Π') === "Kreiszahl"
            }
            "must be 'Wurzel' for value '√'" >> {
                nameOfSymbol('√') === "Wurzel"
            }
            "must be 'Fakultät' for value '!'" >> {
                nameOfSymbol('!') === "Fakultät"
            }
            "must be 'Summe' for value '∑'" >> {
                nameOfSymbol('∑') === "Summe"
            }
        }

        "isWorkingDay" >> {
            "must be 'true' for value 'Sunday'" >> {
                isWorkingDay("Sunday") === true
            }
            "must be 'true' for value 'Saturay'" >> {
                isWorkingDay("Saturay") === true
            }
            "must be 'false' for value 'Monday'" >> {
                isWorkingDay("Monday") === false
            }
            "must be 'false' for value 'Tuesday'" >> {
                isWorkingDay("Tuesday") === false
            }
            "must be 'false' for value 'Wednesday'" >> {
                isWorkingDay("Wednesday") === false
            }
            "must be 'false' for value 'Thursday'" >> {
                isWorkingDay("Thursday") === false
            }
            "must be 'false' for value 'Friday'" >> {
                isWorkingDay("Friday") === false
            }
        }

        "nameOfType" >> {
            "must be 'Boolean' for value true" >> {
                nameOfType(true) === "Boolean"
            }
            "must be 'Char' for value 'c'" >> {
                nameOfType('c') === "Char"
            }
            "must be 'Int' for value 42" >> {
                nameOfType(42) === "Int"
            }
            "must be 'Float' for value 3.14f" >> {
                nameOfType(3.14f) === "Float"
            }
            "must be 'String' for value \"String\"" >> {
                nameOfType("String") === "String"
            }
            "must be 'Unbekannt' for value 1000L" >> {
                nameOfType(1000L) === "Unbekannt"
            }
            "must be 'Unbekannt' for value 1000.0" >> {
                nameOfType(1000.0) === "Unbekannt"
            }
        }
    }
}