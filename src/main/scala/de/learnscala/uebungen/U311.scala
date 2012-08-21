package de.learnscala.uebungen

import de.learnscala.base.Uebung

object U311 extends Uebung {

    def enabled = false

    /**
     * Summiert die Elemente der Liste.
     */
    def sum(l: MyList[Int]) =
        TODO


    /**
     * Multipliziert alle Elemente der Liste (leere Liste ergibt 1).
     */
    def product(l: MyList[Int]) =
        TODO


    /**
     * Zählt die Elemente der Liste.
     */
    def count(l: MyList[Int]) =
        TODO


    /**
     * Liefert das letzte Ergebnis der Liste (leere Liste: None).
     */
    def last[A](list: MyList[A]) =
        TODO


    /**
     * Invertiert die Reihenfolge der Liste.
     */
    def reverse[A](list: MyList[A]) =
        TODO


    /**
     * Ermittelt ob ein Element Teil der Liste ist.
     */
    def contains[A](list: MyList[A], item: A) =
        TODO


    /**
     * Transformiert die Elemente der Liste durch Anwendung der übergebenen Funktion.
     */
    def map[A, B](list: MyList[A], f: A => B) =
        TODO


    /**
     * Filtert die Elemente aus der Liste heraus, bei denen die Funktion 'true' ergibt.
     */
    def filter[A](list: MyList[A], p: A => Boolean) =
        TODO


    /**
     * Erstellt eine Liste ohne doppelte Werte.
     */
    def unique[A](list: MyList[A]) =
        TODO
}


case class MyList[A](private val l: List[A] = List()) {

    def head =
        l.head

    def tail =
        l.tail

    def foldLeft[B](z: B)(f: (B, A) => B): B =
        l.foldLeft(z)(f)

    def isEmpty =
        l.isEmpty

    def ::[B >: A](x: B): MyList[B] =
        new MyList(x :: l)
}