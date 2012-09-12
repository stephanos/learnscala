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
    lazy val klassen = Slide("O10", "Klassen", oop, Seq(), status = "")
    lazy val vererbung1 = Slide("O11", "Vererbung I", oop, Seq(klassen), status = "")
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

    lazy val recursion = Slide("B25", "Rekursion", basic, status = "incomplete")


    // === OBJEKTE

    lazy val hierarchy = Slide("O20", "Hierarchie", oop, status = "")
    lazy val traits = Slide("O21", "Traits", oop, status = "")


    // === FUNKTIONEN


    // === ERWEITERT

    lazy val xml = Slide("E20", "XML", "ext", status = "incomplete")


    // ================================================================================================================
    // LEVEL III ======================================================================================================

    // === ALLGEMEIN


    // === OBJEKTE


    // === FUNKTIONEN

    lazy val kollektionen2 = Slide("F030", "Kollektionen II", "fp", status = "incomplete")


    // === ERWEITERT

}