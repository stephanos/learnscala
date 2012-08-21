package de.learnscala.loesungen

import de.learnscala.base.Loesung

object L030 extends Loesung {

    /**
     * Bestätigt DIE Antwort '42' als richtig.
     */
    def isTheAnswerToLifeTheUniverseAndEverything(n: Int): Boolean =
        if(n == 42) true else false


    /**
     * Signum einer positiven Zahl ist 1, einer negativen -1 und von Null 0.
     */
    def signum(n: Int): Int =
        if(n == 0) 0
        else {
            if(n > 0) 1 else -1
        }


    /**
     * Es wird die Zahl von 2 Argumenten gesucht, die absolut den größten Wert hat.
     */
    def absMax(n1: Int, n2: Int): Int =
        if(math.abs(n1) > math.abs(n2)) n1 else n2
}