package de.learnscala.loesungen

import de.learnscala.base.Loesung
import java.io._

object L060 extends Loesung {

    /**
     * Berechnet die Hälfte einer Zahl,
     * wirft jedoch eine Exception wenn die Zahl nicht ganzzahlig teilbar ist.
     */
    def half(n: Int) =
        if (n % 2 == 0)
            n / 2
        else
            throw new RuntimeException("n must be even")


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
    def readCharFromFile(f: FileReader): Char = {
        try {
            f.read().toChar
        } catch {
            case e: FileNotFoundException =>
                println("not found")
                0
            case e: IOException =>
                println("cannot read")
                0
            case e: RuntimeException =>
                println("unknown error")
                0
        } finally {
            f.close()
        }
    }

}