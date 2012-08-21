package de.learnscala.uebungen

import de.learnscala.base.Uebung

object U320 extends Uebung {

    def enabled = false

    case class Book(title: String, isbn: String, price: Float,
                    stock: Option[Int], authors: List[(String, String)])

    def books =
        List(
            Book("Programming In Scala", "0981531644", 36.95f,
                Some(5), List(("Odersky", "Martin"), ("Venners", "Bill"))),
            Book("Programming Scala", "0596155956", 27.95f,
                Some(50), List(("Payne", "Alex"), ("Wampler", "Dean"))),
            Book("Durchstarten mit Scala", "3868020640", 24.90f,
                Some(100), List(("Seeberger", "Heiko"))),
            Book("Java Virtual Machine", "0079132480", 13.90f,
                None, List(("Venners", "Bill"))),
            Book("Functional Programming for Java Developers", "1449311032", 17.60f,
                Some(10), List(("Wampler", "Dean"))),
            Book("Patterns of Enterprise Application Architecture", "0321127420", 36.95f,
                None, List(("Fowler", "Martin"))),
            Book("Programming Scala", "0321127420", 22.95f,
                Some(1), List(("Subramaniam", "Venkat")))
        )


    /**
     * Liste der Titel von Bücher bei denen ein Autor mit Vornamen "Martin" mitgewirkt hat.
     */
    def titlesWithAuthorMartin =
        TODO


    /**
     * Liste der ISB-Nummern von Büchern bei denen ein Autor, dessen Nachname mit "S" beginnt, mitgewirkt hat.
     */
    def isbnsWithAuthorS =
        TODO


    /*
     * Tuple aus Titel und Preis von den Büchern die "Scala" im Titel tragen.
     */
    def priceAndTitleOfScalaBooks =
        TODO


    /*
     * Titel der Bücher die nicht mehr im Lager ("out of stock") sind.
     */
    def titlesOutOfStock =
        TODO

}