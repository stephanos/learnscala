package de.learnscala.loesungen

import de.learnscala.base.Loesung

object L110 extends Loesung {

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
        def apply(x: Int): Point = {
            apply(x, x)
        }

        def apply(x: Int, y: Int): Point = {
            require(x >= 0)
            require(y >= 0)
            count += 1
            new Point(x, y)
        }

        def getCount = count

        private var count = 0
    }


    /**
     * Erstellt einen Punkt mit den Koordinaten (5,5)
     */
    def p1 = Point(5, 5)

    /**
     * Erstellt einen Punkt mit den Koordinaten (5,5)
     */
    def p2 = Point(5)

    /**
     * Versucht einen Punkt mit negativen Koordiaten zu erstellen.
     */
    def p3 = Point(-5)

    /**
     * Versucht einen Punkt mit einer negativen Koordiate zu erstellen.
     */
    def p4 = Point(-5, 5)
}