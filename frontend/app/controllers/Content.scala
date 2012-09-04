package controllers

import controllers.base.MyController

object Content extends MyController {

    case class Slide(id: String, label: String, chpt: String, deps: Seq[Slide] = Seq(), status: String = "planned")

    // ================================================================================================================
    // LEVEL I ========================================================================================================

    // === ALLGEMEIN

    val einfuehrung = Slide("B10", "Einführung", "basic", status = "")
    val geschichte = Slide("B11", "Geschichte", "basic", status = "")

    // === OBJEKTE


    // === FUNKTIONEN


    // === ERWEITERT

    val tools1 = Slide("E10", "Werkzeuge I", "ext", status = "")
    val tools2 = Slide("E11", "Werkzeuge II", "ext", Seq(tools1), status = "")


    // ================================================================================================================
    // LEVEL II =======================================================================================================

    // === ALLGEMEIN

    val recursion = Slide("B25", "Rekursion", "basic", status = "incomplete")


    // === OBJEKTE

    val hierarchy = Slide("O20", "Hierarchie", "oop", status = "")


    // === FUNKTIONEN


    // === ERWEITERT

    val xml = Slide("E20", "XML", "ext", status = "incomplete")


    // ================================================================================================================
    // LEVEL III ======================================================================================================

    // === ALLGEMEIN


    // === OBJEKTE


    // === FUNKTIONEN


    // === ERWEITERT


    // ================================================================================================================
    // LEVEL IV =======================================================================================================

    // === ALLGEMEIN


    // === OBJEKTE


    // === FUNKTIONEN


    // === ERWEITERT
}