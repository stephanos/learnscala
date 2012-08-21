package de.learnscala.loesungen

import de.learnscala.base.Loesung

object L050 extends Loesung {

    /**
     * Gibt den Namen der mathematischen Symbole zurück: Π, √, ! und ∑
     */
    def nameOfSymbol(symbol: Char): String =
        symbol match {
            case 'Π' => "Kreiszahl"
            case '√' => "Wurzel"
            case '!' => "Fakultät"
            case '∑' => "Summe"
            case _ => "Unbekannt"
        }


    /**
     * Ermittelt ob ein Wochentag ein Arbeitstag ist.
     */
    def isWorkingDay(day: String): Boolean =
        day match {
            case "Sunday" | "Saturay" => true
            case _ => false
        }


    /**
     * Gibt vom übergebenen Wert den Namen des Typs zurück.
     */
    def nameOfType(value: Any): String =
        value match {
            case _: Boolean => "Boolean"
            case _: Char => "Char"
            case _: Int => "Int"
            case _: Float => "Float"
            case _: String => "String"
            case _ => "Unbekannt"
        }
}