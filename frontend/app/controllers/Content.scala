package controllers

import controllers.base.MyController

object Content extends MyController {

    val fp = "fp"
    val oop = "oop"
    val ext = "ext"
    val basic = "basic"

    case class Slide(id: String, label: String, chpt: String, deps: Seq[Slide] = Seq(), status: String = "todo")


    // ================================================================================================================
    // LEVEL I ========================================================================================================

    // === ALLGEMEIN

    lazy val einfuehrung = Slide("B10", "Einführung", basic, status = "complete")
    lazy val geschichte = Slide("B11", "Geschichte", basic, Seq(einfuehrung), status = "complete")
    lazy val ersteSchritte = Slide("B12", "Erste Schritte", basic, Seq(werkzeuge1), status = "complete")
    lazy val methoden = Slide("B13", "Methoden", basic, Seq(ersteSchritte), status = "complete")
    lazy val kontrollstrukturen = Slide("B14", "Kontrollstrukturen", basic, Seq(werkzeuge2, uebungen), status = "complete")

    // === OBJEKTE

    lazy val klassen1 = Slide("O10", "Klassen I", oop, Seq(methoden, werkzeuge2, uebungen), status = "complete")
    lazy val objekte = Slide("O12", "Objekte", oop, Seq(klassen1), status = "complete")
    lazy val vererbung1 = Slide("O11", "Vererbung", oop, Seq(objekte), status = "complete")
    lazy val pakete = Slide("O13", "Pakete", oop, Seq(vererbung1), status = "complete")

    // === FUNKTIONEN

    lazy val funcproc = Slide("F10", "FP", fp, Seq(geschichte), status = "complete")
    lazy val funktionen1 = Slide("F11", "Funktionen I", fp, Seq(funcproc), status = "complete")
    lazy val funktionen2 = Slide("F12", "Funktionen II", fp, Seq(funktionen1), status = "complete")

    // === ERWEITERT

    lazy val werkzeuge1 = Slide("E10", "Werkzeuge I", ext, Seq(geschichte), status = "complete")
    lazy val werkzeuge2 = Slide("E11", "Werkzeuge II", ext, Seq(methoden), status = "complete")
    lazy val uebungen = Slide("E12", "Übungen", ext, Seq(werkzeuge2), status = "complete")


    // ================================================================================================================
    // LEVEL II =======================================================================================================

    // === ALLGEMEIN

    lazy val annotationen = Slide("B21", "Annotationen", basic, Seq())
    lazy val operatoren = Slide("B22", "Operatoren", basic, Seq(methoden, klassen1), status = "complete")
    lazy val regexp = Slide("B23", "Reguläre Ausdrücke", basic, Seq())
    lazy val recursion = Slide("B25", "Rekursion", basic, Seq(kontrollstrukturen, annotationen), status = "complete")
    lazy val implicit1 = Slide("B23", "Implicits", basic, Seq(pakete), status = "complete")

    // === OBJEKTE

    lazy val typen1 = Slide("O20", "Typen", oop, Seq(klassen1), status = "complete")
    lazy val traits = Slide("O21", "Traits", oop, Seq(klassen1, vererbung1), status = "complete")
    lazy val klassen2 = Slide("O22", "Klassen II", oop, Seq(vererbung1), status = "complete")
    lazy val generics1 = Slide("O24", "Typ-Parameter I", oop, Seq(traits, klassen2), status = "complete")

    // === FUNKTIONEN

    lazy val datatypes1 = Slide("F20", "Datenstrukturen I", fp, Seq(generics1))
    lazy val datatypes2 = Slide("F21", "Datenstrukturen II", fp, Seq(datatypes1), status = "complete")
    lazy val listen = Slide("F22", "Listenverarbeitung", fp, Seq(datatypes2), status = "complete")
    lazy val funktionen3 = Slide("F23", "Funktionen III", fp, Seq(datatypes2, funktionen2))
    lazy val forexpr = Slide("F24", "for-Ausdruck", fp, Seq(funktionen3), status = "complete")

    // === ERWEITERT

    lazy val xml = Slide("E20", "XML", ext, Seq(forexpr), status = "complete")
    lazy val jsond = Slide("E21", "JSON", ext, Seq())
    lazy val tests = Slide("E22", "Testen", ext, Seq(traits))
    lazy val swing = Slide("E23", "Swing", ext, Seq())
    lazy val interop = Slide("E24", "Interoperabilität", ext, Seq(implicit1, annotationen, datatypes2, traits), status = "complete")


    // ================================================================================================================
    // LEVEL III ======================================================================================================

    // === ALLGEMEIN

    lazy val dynamic = Slide("B30", "Dynamic", basic)
    lazy val reflection = Slide("B31", "Reflection", basic)
    lazy val macros = Slide("B32", "Makros", basic)
    lazy val implicit2 = Slide("B34", "Implicits II", basic, Seq())
    lazy val extraktoren = Slide("B35", "Extraktoren", basic, Seq())
    lazy val parser = Slide("B36", "Parser", basic, Seq())

    // === OBJEKTE

    lazy val vererbung2 = Slide("O30", "Vererbung II", oop, Seq(vererbung1))
    lazy val typen2 = Slide("O31", "Typen II", oop, Seq(typen1, implicit1))
    lazy val generics2 = Slide("O32", "Typ-Parameter II", oop, Seq(generics1))
    lazy val typeclass = Slide("O33", "Type Classes", oop, Seq())

    // === FUNKTIONEN

    lazy val datatypes3 = Slide("F30", "Datenstrukturen III", fp, Seq(datatypes2))
    lazy val currying = Slide("F31", "Currying", fp, Seq(funktionen2))
    lazy val byname = Slide("F32", "By-Name Parameter", fp, Seq(funktionen2), status = "complete")
    lazy val funktionen4 = Slide("F33", "Funktionen IV", fp, Seq(currying, funktionen3))
    lazy val kontrollabstraktion = Slide("F34", "Kontrollabstraktion", fp, Seq(currying, byname), status = "complete")

    // === ERWEITERT

    lazy val aktoren = Slide("E30", "Aktoren", ext, Seq(funktionen3, forexpr))
    lazy val dbase = Slide("E31", "Datenbanken", ext, Seq(funktionen2))
    lazy val web = Slide("E32", "Web-Entwicklung", ext, Seq(funktionen2))
    lazy val utilities = Slide("E33", "Utilities", ext, Seq())
    lazy val scalaz = Slide("E34", "Scalaz", ext, Seq(typeclass))


    // ================================================================================================================
    // SONSTIGE =======================================================================================================

    lazy val ausblick = Slide("ausblick", "Ausblick", "", Seq(), status = "complete")
}