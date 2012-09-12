package controllers

import controllers.base.MyController

object Content extends MyController {

    val fp = "fp"
    val oop = "oop"
    val ext = "ext"
    val basic = "basic"

    case class Slide(id: String, label: String, chpt: String, deps: Seq[Slide] = Seq(), status: String = "planned")

    // ================================================================================================================
    // LEVEL I ========================================================================================================

    // === ALLGEMEIN

    lazy val einfuehrung = Slide("B10", "Einführung", basic, status = "")
    lazy val geschichte = Slide("B11", "Geschichte", basic, Seq(einfuehrung), status = "")
    lazy val ersteSchritte = Slide("B12", "Erste Schritte", basic, Seq(werkzeuge1), status = "")
    lazy val kontrollstrukturen = Slide("B13", "Kontrollstrukturen", basic, Seq(werkzeuge2), status = "")

    // === OBJEKTE
    lazy val klassen1 = Slide("O10", "Klassen I", oop, Seq(), status = "")
    lazy val vererbung1 = Slide("O11", "Vererbung I", oop, Seq(klassen1), status = "")
    lazy val objekte = Slide("O12", "Objekte", oop, Seq(vererbung1), status = "")
    lazy val sichtbarkeit = Slide("O13", "Sichtbarkeit", oop, Seq(objekte), status = "")
    lazy val pakete = Slide("O14", "Pakete", oop, Seq(sichtbarkeit), status = "")

    // === FUNKTIONEN


    // === ERWEITERT

    lazy val werkzeuge1 = Slide("E10", "Werkzeuge I", ext, Seq(geschichte), status = "")
    lazy val werkzeuge2 = Slide("E11", "Werkzeuge II", ext, Seq(ersteSchritte), status = "")
    lazy val uebungen = Slide("E12", "Übungen", ext, Seq(werkzeuge2), status = "")


    // ================================================================================================================
    // LEVEL II =======================================================================================================

    // === ALLGEMEIN

    lazy val annotationen = Slide("B21", "Annotationen", basic, status = "")
    lazy val recursion = Slide("B25", "Rekursion", basic, status = "")


    // === OBJEKTE

    lazy val hierarchy = Slide("O20", "Typhierarchie", oop, status = "")
    lazy val traits = Slide("O21", "Traits", oop, status = "")
    lazy val klassen2 = Slide("O22", "Klassen II", oop, Seq(), status = "")
    lazy val vererbung2 = Slide("O23", "Vererbung II", oop, Seq(), status = "")
    lazy val generics1 = Slide("O24", "Typ-Parameter I", oop, Seq(), status = "")


    // === FUNKTIONEN
    lazy val kollektionen1 = Slide("F024", "Kollektionen I", fp)


    // === ERWEITERT

    lazy val xml = Slide("E20", "XML", ext, status = "incomplete")


    // ================================================================================================================
    // LEVEL III ======================================================================================================

    // === ALLGEMEIN


    // === OBJEKTE

    lazy val generics2 = Slide("O30", "Typ-Parameter II", oop, Seq(generics1))


    // === FUNKTIONEN

    lazy val kollektionen2 = Slide("F030", "Kollektionen II", fp, Seq(kollektionen1))


    // === ERWEITERT

}