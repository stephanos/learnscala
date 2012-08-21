package de.learnscala.uebungen

import de.learnscala.base.Uebung

object U221 extends Uebung {

    def enabled = false

    /**
     * Tauscht die Elemente des Tuples.
     */
    def swap(t: (Int, Int)) =
        TODO


    /**
     * Summiert die Elemente eines Tuples.
     * Das erste Element zeigt an, ob addiert (true) oder subtrahiert (false) werden soll.
     */
    def sum(t: (Boolean, Int, Int)) =
        TODO


    /**
     * Antwortet (print) ob das Auto gekauft werden soll - abhängig vom Argument.
     */
    def doYouBuyTheCar(b: Option[Boolean]) {
        TODO
    }


    /**
     * Konvertiert einen String in einen Integer (None bei Ausnahme).
     */
    def convertToInt(s: String) =
        TODO

}