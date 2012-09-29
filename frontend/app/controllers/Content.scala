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
    lazy val ersteSchritte = Slide("B12", "Erste Schritte", basic, Seq(werkzeuge1))
    lazy val kontrollstrukturen = Slide("B13", "Kontrollstrukturen", basic, Seq(werkzeuge2))

    // === OBJEKTE
    lazy val klassen1 = Slide("O10", "Klassen I", oop, Seq(ersteSchritte))
    lazy val vererbung1 = Slide("O11", "Vererbung I", oop, Seq(klassen1))
    lazy val objekte = Slide("O12", "Objekte", oop, Seq(klassen1), status = "complete")
    lazy val pakete = Slide("O13", "Pakete", oop, Seq(klassen1))

    // === FUNKTIONEN


    // === ERWEITERT

    lazy val werkzeuge1 = Slide("E10", "Werkzeuge I", ext, Seq(geschichte), status = "complete")
    lazy val werkzeuge2 = Slide("E11", "Werkzeuge II", ext, Seq(ersteSchritte), status = "complete")
    lazy val uebungen = Slide("E12", "Übungen", ext, Seq(werkzeuge2))


    // ================================================================================================================
    // LEVEL II =======================================================================================================

    // === ALLGEMEIN

    lazy val annotationen = Slide("B21", "Annotationen", basic, Seq())
    lazy val implicit1 = Slide("B24", "Implicits I", basic, Seq(), status="complete")
    lazy val recursion = Slide("B25", "Rekursion", basic, Seq())


    // === OBJEKTE

    lazy val typen1 = Slide("O20", "Typen I", oop, Seq(), status = "incomplete")
    lazy val traits = Slide("O21", "Traits", oop, Seq(klassen1, vererbung1))
    lazy val klassen2 = Slide("O22", "Klassen II", oop, Seq(klassen1))
    lazy val vererbung2 = Slide("O23", "Vererbung II", oop, Seq(vererbung1))
    lazy val generics1 = Slide("O24", "Typ-Parameter I", oop, Seq())


    // === FUNKTIONEN

    lazy val kollektionen1 = Slide("F20", "Kollektionen I", fp, Seq(generics1))
    lazy val forexpr = Slide("F21", "for-Ausdruck", fp, Seq(kollektionen1))


    // === ERWEITERT

    lazy val xml = Slide("E20", "XML", ext, Seq(forexpr), status = "complete")
    lazy val jsond = Slide("E21", "JSON", ext, Seq())
    lazy val tests = Slide("E22", "Testen", ext, Seq())
    lazy val swing = Slide("E23", "Swing", ext, Seq())
    lazy val interop = Slide("E24", "Interoperabilität", ext, Seq(implicit1, kollektionen1, traits), status = "complete")


    // ================================================================================================================
    // LEVEL III ======================================================================================================

    // === ALLGEMEIN

    lazy val dynamic = Slide("B30", "Dynamic", basic)
    lazy val reflection = Slide("B31", "Reflection", basic)
    lazy val macros = Slide("B32", "Makros", basic)
    lazy val implicit2 = Slide("B34", "Implicits II", basic, Seq())

    // === OBJEKTE

    lazy val generics2 = Slide("O30", "Typ-Parameter II", oop, Seq(generics1))
    lazy val typen2 = Slide("O31", "Typen II", oop, Seq(typen1, implicit1))


    // === FUNKTIONEN

    lazy val kollektionen2 = Slide("F30", "Kollektionen II", fp, Seq(kollektionen1))
    lazy val kontrollabstraktion = Slide("F32", "Kontrollabstraktion", fp, Seq())


    // === ERWEITERT

    lazy val aktoren = Slide("E30", "Aktoren", ext, Seq())
    lazy val dbase = Slide("E31", "Datenbanken", ext, Seq())
    lazy val web = Slide("E32", "Web", ext, Seq())
    lazy val scalaz = Slide("E34", "Scalaz", ext, Seq())
}