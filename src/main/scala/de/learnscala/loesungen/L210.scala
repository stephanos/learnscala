package de.learnscala.loesungen

import de.learnscala.base.Loesung

object L210 extends Loesung {

    /**
     * Klasse 'MyPair' ist eine konkrete, typisierte Implementierung von 'Pair'.
     */
    class MyPair[T,S](val t: T, val s: S) extends Pair {

        def swap =
            new MyPair(s, t)

        def bothHaveSameType =
            t.getClass == s.getClass
    }

    trait Pair {

        def t: Any
        def s: Any
        def swap: Pair
        def bothHaveSameType: Boolean
    }


    def p1 =
        new MyPair(1, 1)

    def p2 =
        new MyPair(true, false)

    def p3 =
        new MyPair("Bob", "Babe")

    def p4 =
        new MyPair("male", true)
}