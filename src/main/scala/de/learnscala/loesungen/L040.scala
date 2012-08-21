package de.learnscala.loesungen

import de.learnscala.base.Loesung

object L040 extends Loesung {

    /**
     * Zählt von 1 bis 10, gibt Zahlen durch Leerzeichen getrennt aus.
     */
    def countingOut() {
        for (i <- 1 to 10)
            print(i + " ")
    }


    /**
     * Zählt von 10 bis 100, gibt Zahlen durch Leerzeichen getrennt aus.
     */
    def count10s() {
        for (i <- 10 to 100 by 10)
            print(i + " ")
    }


    /**
     * Zählt Zahlen von 1 bis 10;
     * gibt Zahlen, die ganzzahlig durch Parameter teilbar sind, durch Leerzeichen getrennt aus.
     */
    def countMod(mod: Int) {
        for (i <- 1 to 10)
            if(i % mod == 0) print(i + " ")
    }
}