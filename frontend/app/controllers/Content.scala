package controllers

import controllers.base.MyController

object Content extends MyController {

    case class Slide(id: String, label: String, chpt: String, deps: Seq[Slide] = Seq(), status: String = "planned")

    // ================================================================================================================
    // LEVEL I ========================================================================================================

    // === ALLGEMEIN

    lazy val einfuehrung = Slide("B10", "Einführung", "basic", status = "")
    lazy val geschichte = Slide("B11", "Geschichte", "basic", Seq(einfuehrung), status = "")
    lazy val ersteSchritte = Slide("B12", "Erste Schritte", "basic", Seq(tools1), status = "")
    lazy val kontrollstrukturen = Slide("B13", "Kontrollstrukturen", "basic", Seq(tools2), status = "")

    // === OBJEKTE


    // === FUNKTIONEN


    // === ERWEITERT

    lazy val tools1 = Slide("E10", "Werkzeuge I", "ext", Seq(geschichte), status = "")
    lazy val tools2 = Slide("E11", "Werkzeuge II", "ext", Seq(ersteSchritte), status = "")


    // ================================================================================================================
    // LEVEL II =======================================================================================================

    // === ALLGEMEIN

    lazy val recursion = Slide("B25", "Rekursion", "basic", status = "incomplete")


    // === OBJEKTE

    lazy val hierarchy = Slide("O20", "Hierarchie", "oop", status = "")


    // === FUNKTIONEN


    // === ERWEITERT

    lazy val xml = Slide("E20", "XML", "ext", status = "incomplete")


    // ================================================================================================================
    // LEVEL III ======================================================================================================

    // === ALLGEMEIN


    // === OBJEKTE


    // === FUNKTIONEN


    // === ERWEITERT

}