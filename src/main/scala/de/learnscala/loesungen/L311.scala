package de.learnscala.loesungen

import de.learnscala.base.Loesung

object L311 extends Loesung {

    /**
     * Summiert die Elemente der Liste.
     */
    def sum(l: MyList[Int]) =
        l.foldLeft(0)(_ + _)


    /**
     * Multipliziert alle Elemente der Liste (leere Liste ergibt 1).
     */
    def product(l: MyList[Int]) =
        l.foldLeft(1)(_ * _)


    /**
     * Zählt die Elemente der Liste.
     */
    def count(l: MyList[Int]) =
        l.foldLeft(0)((sum, _) => sum + 1)


    /**
     * Liefert das letzte Ergebnis der Liste (leere Liste: None).
     */
    def last[A](list: MyList[A]): Option[A] =
        if (list.isEmpty) None
        else Some(list.foldLeft(list.head)((_, c) => c))


    /**
     * Invertiert die Reihenfolge der Liste.
     */
    def reverse[A](list: MyList[A]): MyList[A] =
        list.foldLeft(MyList[A]())((r, c) => c :: r)


    /**
     * Ermittelt ob ein Element Teil der Liste ist.
     */
    def contains[A](list: MyList[A], item: A): Boolean =
        list.foldLeft(false)(_ || _ == item)


    /**
     * Transformiert die Elemente der Liste durch Anwendung der übergebenen Funktion.
     */
    def map[A, B](list: MyList[A], f: A => B): MyList[B] =
        reverse(list.foldLeft(MyList[B]())((res, cur) => f(cur) :: res))


    /**
     * Filtert die Elemente aus der Liste heraus, bei denen die Funktion 'true' ergibt.
     */
    def filter[A](list: MyList[A], p: A => Boolean): MyList[A] =
        reverse(list.foldLeft(MyList[A]())((r, c) => if (p(c)) c :: r else r))


    /**
     * Erstellt eine Liste ohne doppelte Werte.
     */
    def unique[A](list: MyList[A]): MyList[A] =
        reverse(list.foldLeft(MyList[A]()) {
            (r, c) =>
                if (contains(r, c)) r else c :: r
        })
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