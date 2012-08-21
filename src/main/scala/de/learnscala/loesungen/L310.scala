package de.learnscala.loesungen

import de.learnscala.base.Loesung

object L310 extends Loesung {

    def add1 =
        (x: Int) => x + 1

    def sub1 =
        (x: Int) => x - 1

    def double =
        (x: Int) => x * 2


    /**
     * Erhöht alle Elemente der Liste um 1
     */
    def r1(l: List[Int]) =
        l map add1

    /**
     * Erhöht alle Elemente der Liste um 1 und verdoppelt sie anschließend
     */
    def r2(l: List[Int]) =
        l map add1 map double

    /**
     * Verdoppelt alle Elemente der Liste und addiert dann 1 dazu
     */
    def r3(l: List[Int]) =
        l map double map add1

    /**
     * Addiert 1 auf die Listenelemente, verdoppelt sie dann und zieht wieder 1 ab
     */
    def r4(l: List[Int]) =
        l map add1 map double map sub1

    /**
     * Addiert 1 auf das erste Element des Tuples und zieht 1 vom zweiten ab
     */
    def r5(l: List[(Int, Int)]) =
        l map (t => (add1(t._1), sub1(t._2)))
}