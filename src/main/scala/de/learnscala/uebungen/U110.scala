package de.learnscala.uebungen

import de.learnscala.base.Uebung

object U110 extends Uebung {

    def enabled = false


    /**
     * Klasse 'Point' definiert einen 2-dimensionalen Punkt.
     *
     * Objekt 'Point' stellt 2 Factory-Methoden zur Verfügung,
     * eine für x/y-Koordinaten und die andere mit einem Parameter für den Sonderfall x = y.
     *
     * Ist einer der beiden Koordinaten negativ, sollte eine Ausnahme geworfen werden.
     *
     * Zusätzlich zählt es die Anzahl der bisher erstellten Punkte
     * und liefert diese via 'getCount'.
     */

    class Point(val x: Int, val y: Int)


    object Point {

        def getCount = 0
    }


    /**
     * Erstellt einen Punkt mit den Koordinaten (5,5)
     */
    def p1 = TODO // Point(5, 5)

    /**
     * Erstellt einen Punkt mit den Koordinaten (5,5)
     */
    def p2 = TODO //Point(5)

    /**
     * Versucht einen Punkt mit negativen Koordiaten zu erstellen.
     */
    def p3 = TODO //Point(-5)

    /**
     * Versucht einen Punkt mit einer negativen Koordiate zu erstellen.
     */
    def p4 = TODO //Point(-5, 5)
}