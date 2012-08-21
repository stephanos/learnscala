package de.learnscala.loesungen

import de.learnscala.base.Loesung

object L140 extends Loesung {

    /**
     * Gehaltsabrechnung.
     *
     * - jeder normale Angestellte verdient 50.000€ im Jahr
     *   - ist er mit dem Manager bedfreundet, bekommt er das doppelte
     *   - ist er jedoch illegaler Einwanderer, halbiert sich sein Lohn
     *   - ist er Teil des Aufsichtsrats ist er automatisch Freund des Managers
     * - der Manager bekommt das 5x eines normalen Angestellten
     */

    trait Employee {
        def salary = 50000
    }

    class Manager extends Employee {
        override def salary = super.salary * 5
    }

    class BoardMember extends Employee with FriendOfManager

    class Clerk extends Employee


    trait FriendOfManager extends Employee {
        override def salary = super.salary * 2
    }

    trait IllegalImmigrant extends Employee {
        override def salary = super.salary / 2
    }


    /**
     * Das Gehalt eines normalen Angestellten.
     */
    def salaryForClerk =
        (new Clerk).salary

    /**
     * Das Gehalt eines Managers.
     */
    def salaryForManager =
        (new Manager).salary

    /**
     * Das Gehalt eines Board Members.
     */
    def salaryForBoardMember =
        (new BoardMember).salary

    /**
     * Das Gehalt eines Angestellten, der befreundet mit dem Manager ist.
     */
    def salaryForfriendOfManagerClerk =
        (new Clerk with FriendOfManager).salary

    /**
     * Das Gehalt eines Angestellten der illegal eingewandert ist.
     */
    def salaryForIllegalImmigrantClerk =
        (new Clerk with IllegalImmigrant).salary

    /**
     * Das Gehalt eines illegal eingewanderten Managers.
     */
    def salaryForIllegalImmigrantManager =
        (new Manager with IllegalImmigrant).salary

    /**
     * Das Gehalt eines illegal eingewanderten Board Members.
     */
    def salaryForIllegalImmigrantBoardMember =
        (new BoardMember with IllegalImmigrant).salary
}