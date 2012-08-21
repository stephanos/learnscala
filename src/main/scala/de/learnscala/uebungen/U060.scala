package de.learnscala.uebungen

import de.learnscala.base.Uebung
import java.io._

object U060 extends Uebung {

    def enabled = false


    /**
     * Berechnet die Hälfte einer Zahl,
     * wirft jedoch eine Exception wenn die Zahl nicht ganzzahlig teilbar ist.
     */
    def half(n: Int) =
        TODO


    /**
     * Liest das erste Zeichen einer Datei aus.
     *
     * Bei 'IOException' sollte 'cannot read' geloggt werden.
     * Bei 'RuntimeException' sollte 'not found' geloggt werden.
     * Bei 'FileNotFoundException' sollte 'not found' geloggt werden.
     *
     * PS: Nicht vergessen den Reader zu schließen!
     *
     * @param f der File Reader
     * @return erste Zeichen der Datei - oder '0' bei einem Fehler
     */
    def readCharFromFile(f: FileReader) = {
        TODO
    }

}