package de.learnscala.loesungen

import de.learnscala.base.Loesung

object L221 extends Loesung {

    /**
     * Tauscht die Elemente des Tuples.
     */
    def swap(t: (Int, Int)): (Int, Int) =
        (t._2, t._1)


    /**
     * Summiert die Elemente eines Tuples.
     * Das erste Element zeigt an, ob addiert (true) oder subtrahiert (false) werden soll.
     */
    def sum(t: (Boolean, Int, Int)) =
        t match {
            case (true, a1, a2) => a1 + a2
            case (false, a1, a2) => a1 - a2
        }


    /**
     * Antwortet ob das Auto gekauft werden soll - abhängig vom Argument.
     */
    def doYouBuyTheCar(b: Option[Boolean]) {
        print(b match {
            case Some(true) => "yes"
            case Some(false) => "no"
            case _ => "not sure"
        })
    }


    /**
     * Konvertiert einen String in einen Integer.
     */
    def convertToInt(s: String): Option[Int] =
        try {
            Some(s.toInt)
        } catch {
            case _: Throwable => None
        }

}