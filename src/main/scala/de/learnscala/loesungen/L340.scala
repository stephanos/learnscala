package de.learnscala.loesungen

import de.learnscala.base.Loesung

object L340 extends Loesung {

    def until(condition: => Boolean)(block: => Unit) {
        if (!condition) {
            block
            until(condition)(block)
        }
    }

    /**
     * Gesucht wird eine Funktion 'until' die bewirkt, dass der Rückgabewert '10' ist.
     */
    def incr = {
        var i = 0
        until(i == 10) {
            i += 1
        }
        i
    }
}