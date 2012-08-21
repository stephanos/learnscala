package de.learnscala.uebungen

import de.learnscala.base.Uebung

object U222 extends Uebung {

    def enabled = false

    /**
     * Liste aus den deutschen Tagen der Woche, unterteilt in Wochentage und Wochenendtage.
     */
    def weekDays = TODO
    def weekendDays = TODO

    def days = TODO


    /**
     * Ermittelt das letzte Element einer Liste (None falls nicht vorhanden).
     */
    def last(l: List[Int]) =
        TODO


    /**
     * Ermittelt das dritte Element einer Liste (None falls nicht vorhanden).
     */
    def third(l: List[Int], count: Int = 1) =
        TODO


    /**
     * Ermittelt ob eine Liste ein Palindrom ist (Zeichenkette, die von vorn und von hinten gelesen gleich bleibt).
     *
     * Tipp: l.reverse
     */
    def palindrome(s: List[Int]) =
        TODO

}