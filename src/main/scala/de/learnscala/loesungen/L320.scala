package de.learnscala.loesungen

import de.learnscala.base.Loesung

object L320 extends Loesung {

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
        for (b <- books; a <- b.authors if a._2 == "Martin") yield b.title


    /**
     * Liste der ISB-Nummern von Büchern bei denen ein Autor, dessen Nachname mit "S" beginnt, mitgewirkt hat.
     */
    def isbnsWithAuthorS =
        for (b <- books; a <- b.authors if a._1 startsWith "S") yield b.isbn


    /*
     * Tuple aus Titel und Preis von den Büchern die "Scala" im Titel tragen.
     */
    def priceAndTitleOfScalaBooks =
        for (b <- books if b.title contains "Scala") yield (b.title -> b.price)


    /*
     * Titel der Bücher die nicht mehr im Lager ("out of stock") sind.
     */
    def titlesOutOfStock =
        for (b <- books if(b.stock.isEmpty)) yield b.title

}