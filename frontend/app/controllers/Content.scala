package controllers

import controllers.base.MyController

object Content extends MyController {

  val fp = "fp"
  val oop = "oop"
  val ext = "ext"
  val basic = "basic"

  case class Slide(id: String, code: String, label: String, chpt: String, deps: Seq[Slide] = Seq(),
                   status: String = "complete")


  // ================================================================================================================
  // LEVEL I ========================================================================================================

  // === ALLGEMEIN

  lazy val einfuehrung = Slide("B10", "intro", "Einführung", basic)
  lazy val ersteSchritte = Slide("B12", "basic", "Erste Schritte", basic, Seq(werkzeuge1))
  lazy val methoden = Slide("B13", "meth", "Methoden", basic, Seq(ersteSchritte))
  lazy val control = Slide("B14", "control", "Kontrollstrukturen", basic, Seq(methoden, uebungen))

  // === OBJEKTE

  lazy val klassen1 = Slide("O10", "class", "Klassen", oop, Seq(control, uebungen))
  lazy val objekte = Slide("O11", "object", "Objekte", oop, Seq(klassen1))
  lazy val vererbung1 = Slide("O12", "inherit", "Vererbung", oop, Seq(objekte))
  lazy val pakete = Slide("O13", "pckg", "Pakete", oop, Seq(vererbung1))

  // === FUNKTIONEN

  lazy val funcproc = Slide("F10", "fp", "FP", fp, Seq(control))
  lazy val funktionen1 = Slide("F11", "func1", "Funktionen I", fp, Seq(objekte, funcproc))
  lazy val funktionen2 = Slide("F12", "func2", "Funktionen II", fp, Seq(pakete, funktionen1))

  // === ERWEITERT

  lazy val werkzeuge1 = Slide("E10", "tools1", "Werkzeuge I", ext, Seq(einfuehrung))
  lazy val werkzeuge2 = Slide("E11", "tools2", "Werkzeuge II", ext, Seq(methoden))
  lazy val uebungen = Slide("E12", "excs", "Übungen", ext, Seq(werkzeuge2))


  // ================================================================================================================
  // LEVEL II =======================================================================================================

  // === ALLGEMEIN

  lazy val operatoren = Slide("B20", "operator", "Operatoren", basic, Seq(pakete, vererbung1))
  // depends on 'override'
  lazy val recursion = Slide("B21", "recursion", "Rekursion", basic, Seq(pakete, listen))
  lazy val implicit1 = Slide("B22", "implicit", "Implicits", basic, Seq(operatoren, klassen2, pakete, listen))
  lazy val interpolation = Slide("B23", "interpol", "String Interpolation", basic, Seq(implicit1, funktionen3))
  lazy val annotationen = Slide("B24", "anno", "Annotationen", basic, Seq()) // ERROR: USE JAVA !

  // === OBJEKTE

  lazy val typen1 = Slide("O20", "typ", "Typen", oop, Seq(control, klassen1))
  lazy val traits = Slide("O21", "trait", "Traits", oop, Seq(klassen1, vererbung1, pakete))
  lazy val generics1 = Slide("O22", "typparam", "Typparameter", oop, Seq(traits))
  lazy val klassen2 = Slide("O23", "class2", "Klassen II", oop, Seq(datatypes1, generics1))

  // === FUNKTIONEN

  lazy val datatypes1 = Slide("F20", "data", "Datenstrukturen I", fp, Seq(generics1))
  lazy val datatypes2 = Slide("F21", "data2", "Datenstrukturen II", fp, Seq(operatoren, datatypes1))
  lazy val listen = Slide("F22", "list", "Listenverarbeitung", fp, Seq(datatypes2))
  lazy val funktionen3 = Slide("F23", "func3", "Funktionen III", fp, Seq(datatypes2, funktionen2))

  // === ERWEITERT

  lazy val interop = Slide("E20", "interop", "Interoperabilität", ext, Seq(implicit1, datatypes2, traits))
  lazy val xml = Slide("E21", "xml", "XML", ext, Seq(forexpr))
  lazy val jsond = Slide("E22", "json", "JSON", ext, Seq())
  lazy val swing = Slide("E23", "swing", "Swing", ext, Seq())


  // ================================================================================================================
  // LEVEL III ======================================================================================================

  // === ALLGEMEIN

  lazy val implicit2 = Slide("B30", "impl2", "Implicits II", basic, Seq())
  lazy val dynamic = Slide("B31", "dyn", "Dynamic", basic)
  lazy val reflection = Slide("B32", "refl", "Reflection", basic)
  lazy val macros = Slide("B33", "macro", "Makros", basic)

  // === OBJEKTE

  lazy val klassen3 = Slide("O30", "class3", "Klassen III", oop, Seq(klassen2))
  lazy val typen2 = Slide("O31", "typ2", "Typen II", oop, Seq(typen1, implicit1))
  lazy val generics2 = Slide("O32", "typparam2", "Typparameter II", oop, Seq(generics1))

  // === FUNKTIONEN
  lazy val funktionen4 = Slide("F30", "func4", "Funktionen IV", fp, Seq(funktionen3))
  lazy val forexpr = Slide("F31", "for", "for-Ausdruck", fp, Seq(funktionen3))
  lazy val kontrollabstraktion = Slide("F32", "control2", "Kontrollabstraktion", fp, Seq(funktionen3, implicit1))
  lazy val errors = Slide("F32", "", "Fehlerbehandlung", fp, Seq(funktionen4))

  lazy val dsl = Slide("F33", "DSL", "dsl", fp, Seq())
  lazy val datatypes3 = Slide("F34", "data3", "Datenstrukturen III", fp, Seq(datatypes2))
  lazy val funktionen5 = Slide("F37", "func5", "Funktionen IV", fp, Seq(funktionen4))

  // === ERWEITERT

  lazy val tests1 = Slide("E30", "test", "Testen", ext, Seq(traits, operatoren, implicit1))
  lazy val tests2 = Slide("E31", "test2", "Testen II", ext, Seq(tests1))

  lazy val aktoren = Slide("E32", "actor", "Aktoren", ext, Seq(funktionen4, forexpr))
  lazy val dbase = Slide("E33", "db", "Datenbank", ext, Seq(funktionen4))
  lazy val web = Slide("E34", "web", "Web-Entwicklung", ext, Seq(funktionen4))


  // ================================================================================================================
  // SONSTIGE =======================================================================================================

  lazy val ausblick = Slide("ausblick", "end", "Ausblick", "", Seq())
}