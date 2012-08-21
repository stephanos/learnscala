package de.learnscala.uebungen

import de.learnscala.base.Uebung

object U150 extends Uebung {

    def enabled = false

    /**
     * 'OpTree' ist ein Baum, bei dem jeder Knoten einen Operator und zwei Unterbäume besitzt.
     * Mögliche Operatoren sind: '+', '-', '*', '/'.
     *
     * Die Methode 'sum' erhält einen Baum als Argument und errechnet die Summe,
     * wobei der Operator auf die Summen der Unterbäume anzuwenden ist.
     *
     * Zum Beispiel:
     *      +
     *    /  \
     *   *    -
     *  / \  / \
     *  3 8  2  5
     *  => Summe ist (3 * 8) + (2 - 5) = 24 + -3 = 21
     */


    // TESTS
    def s1 = TODO //sum(Leaf(5))
    def s2 = TODO //sum(Node('+', Leaf(5), Leaf(5)))
    def s3 = TODO //sum(Node('*', Leaf(5), Leaf(5)))
    def s4 = TODO //sum(Node('/', Leaf(5), Leaf(5)))
    def s5 = TODO //sum(Node('-', Leaf(5), Leaf(5)))
    def s6 = TODO //sum(Node('+', Node('-', Leaf(10), Leaf(2)), Node('*', Leaf(5), Leaf(5))))
}