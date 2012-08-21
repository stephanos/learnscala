package de.learnscala.uebungen

import de.learnscala.base.Uebung

object U230 extends Uebung {

    def enabled = false

    /**
     * Für die folgenden Aufgaben sollte die offizielle Scala API-Dokumentation verwendet werden:
     * http://www.scala-lang.org/api/current/index.html
     */


    /**
     * Konvertiert Elemente von Traversable in einen String der Form "[1,2,3]".
     */
    def toString(t: Traversable[Int]) =
        TODO


    /**
     * Hängt Argument 2 an Argument 1.
     */
    def join(t1: Traversable[Int], t2: Traversable[Int]) =
        TODO


    /**
     * Liefert die ersten beiden Elemente des Traversable.
     */
    def first2(t: Traversable[Int]) =
        TODO


    /**
     * Summiert die Elemente des Traversable.
     */
    def sumOf(t: Traversable[Int]) =
        TODO


    /**
     * Ermittelt die Länge des Traversable.
     */
    def length(t: Traversable[Int]) =
        TODO


    /**
     * Entfernt das erste Element des Traversable.
     */
    def withoutFirst(t: Traversable[Int]) =
        TODO


    /**
     * Ermittelt das größte Elemente desTraversable.
     */
    def biggest(t: Traversable[Int]) =
        TODO


    /////////////////////////////////////////////////////////
    // List

    /**
     * Erstellt eine Liste aus den 3 Elementen
     */
    def makeList(e1: Int, e2: Int, e3: Int) =
        TODO

    /**
     * Erstellt aus Parameter eine Liste ohne Duplikate.
     */
    def unique(l: List[Int]) =
        TODO


    /**
     * Kehrt die Reihenfolge der Liste um
     */
    def backwards(l: List[Int]) =
        TODO


    /////////////////////////////////////////////////////////
    // Set

    /**
     * Ermittelt ob das Set die Elemente 42 und 101 enthält.
     */
    def has42and101(s: Set[Int]) =
        TODO


    /**
     * Kombiniert Argument 1 und Argument 2
     */
    def mergeSet(s1: Set[Int], s2: Set[Int]) =
        TODO


    /////////////////////////////////////////////////////////
    // Map

    /**
     * Entfernt Schlüssel '42' aus der Map.
     */
    def withoutKey42(m: Map[Int, Boolean]) =
        TODO

    /**
     * Ermittelt ob Map den Schlüssel '42' enthält.
     */
    def hasKey42(m: Map[Int, Boolean]) =
        TODO

}