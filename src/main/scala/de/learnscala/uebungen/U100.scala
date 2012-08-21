package de.learnscala.uebungen

import de.learnscala.base.Uebung

object U100 extends Uebung {

    def enabled = false


    /**
     * Klasse 'Time' erwartet 2 Parameter: Stunden und Minuten.
     *
     * Sie besitzt die Methoden
     * - addMin(Int): fügt Minuten hinzu
     * - addHours(Int): fügt Stunden hinzu
     * - +(Time): addiert eine andere Time-Instanz
     * - print: gibt die Zeit aus (z.B. 00:30)
     **/


    /**
     * Zeit um 13:37.
     */
    def t1 = TODO // new Time(13, 37)

    /**
     * Zeit um 20:00.
     */
    def t2 = TODO // new Time(20)

    /**
     * Zeit um 0:00.
     */
    def t3 = TODO // new Time()

    /**
     * Zeit um 12:00 plus 2 Stunden
     */
    def t4 = TODO // new Time(12, 00) addHours 2

    /**
     * Zeit um 23:59 plus 2 Minuten
     */
    def t5 = TODO // new Time(23, 59) addMin 2

    /**
     * Zeit um 12:23 plus 13:37
     */
    def t6 = TODO // new Time(12, 23) + new Time(13, 37)
}