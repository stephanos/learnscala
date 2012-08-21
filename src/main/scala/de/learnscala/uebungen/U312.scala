package de.learnscala.uebungen

import de.learnscala.base.Uebung

object U312 extends Uebung {

    def enabled = false

    /**
     * Eine Markdown-ähnliche Sprache wird erstellt.
     *
     * Bestimmte Formatierungen wie Links und Fett werden häufiger gebraucht,
     * daher werden die speziellen Funktionen 'printLink' und 'printBold' gebraucht.
     */

    def printEnclosed(left: String, text: String, right: String) {
        println(left + text + right)
    }



    def shout() =
        TODO // printBold("SCALA")

    def google() =
        TODO // printLink("http://google.com")
}