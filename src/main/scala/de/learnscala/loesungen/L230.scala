package de.learnscala.loesungen

import de.learnscala.base.Loesung

object L230 extends Loesung {

    /**
     * Für die folgenden Aufgaben sollte die offizielle Scala API-Dokumentation verwendet werden:
     * http://www.scala-lang.org/api/current/index.html
     */


    /**
     * Konvertiert Elemente von Traversable in einen String der Form "[1,2,3]".
     */
    def toString(t: Traversable[Int]) =
        t.mkString("[", ",", "]")


    /**
     * Hängt Argument 2 an Argument 1.
     */
    def join(t1: Traversable[Int], t2: Traversable[Int]) =
        t1 ++ t2


    /**
     * Liefert die ersten beiden Elemente des Traversable.
     */
    def first2(t: Traversable[Int]) =
        t.take(2)


    /**
     * Summiert die Elemente des Traversable.
     */
    def sumOf(t: Traversable[Int]) =
        t.sum


    /**
     * Ermittelt die Länge des Traversable.
     */
    def length(t: Traversable[Int]) =
        t.size


    /**
     * Entfernt das erste Element des Traversable.
     */
    def withoutFirst(t: Traversable[Int]) =
        t.drop(1)


    /**
     * Ermittelt das größte Elemente desTraversable.
     */
    def biggest(t: Traversable[Int]) =
        t.max


    /////////////////////////////////////////////////////////
    // List

    /**
     * Erstellt eine Liste aus den 3 Elementen
     */
    def makeList(e1: Int, e2: Int, e3: Int) =
        List(e1, e2, e3)

    /**
     * Erstellt aus Parameter eine Liste ohne Duplikate.
     */
    def unique(l: List[Int]) =
        l.distinct


    /**
     * Kehrt die Reihenfolge der Liste um
     */
    def backwards(l: List[Int]) =
        l.reverse


    /////////////////////////////////////////////////////////
    // Set

    /**
     * Ermittelt ob das Set die Elemente 42 und 101 enthält.
     */
    def has42and101(s: Set[Int]) =
        Set(42, 101) subsetOf s


    /**
     * Kombiniert Argument 1 und Argument 2
     */
    def mergeSet(s1: Set[Int], s2: Set[Int]) =
        s1 union s2


    /////////////////////////////////////////////////////////
    // Map

    /**
     * Entfernt Schlüssel '42' aus der Map.
     */
    def withoutKey42(m: Map[Int, Boolean]) =
        m - 42

    /**
     * Ermittelt ob Map den Schlüssel '42' enthält.
     */
    def hasKey42(m: Map[Int, Boolean]) =
        m contains 42

}