package de.learnscala.loesungen

import de.learnscala.base.Loesung

object L150 extends Loesung {

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

    sealed abstract class OpTree
    case class Leaf(value: Int) extends OpTree
    case class Node(operator: Char, left: OpTree, right: OpTree) extends OpTree

    def sum(tree: OpTree): Int =
        tree match {
            case Leaf(v) => v
            case Node(op, left, right) =>
                op match {
                    case '+' => sum(left) + sum(right)
                    case '-' => sum(left) - sum(right)
                    case '*' => sum(left) * sum(right)
                    case '/' => sum(left) / sum(right)
                }
        }



    // TESTS
    def s1 = sum(Leaf(5))
    def s2 = sum(Node('+', Leaf(5), Leaf(5)))
    def s3 = sum(Node('*', Leaf(5), Leaf(5)))
    def s4 = sum(Node('/', Leaf(5), Leaf(5)))
    def s5 = sum(Node('-', Leaf(5), Leaf(5)))
    def s6 = sum(Node('+', Node('-', Leaf(10), Leaf(2)), Node('*', Leaf(5), Leaf(5))))
}