package de.learnscala.uebungen

import de.learnscala.base.Uebung

object U140 extends Uebung {

    def enabled = false

    /**
     * Gehaltsabrechnung.
     *
     * - jeder normale Angestellte verdient 50.000€ im Jahr
     *   - ist er mit dem Manager bedfreundet, bekommt er das doppelte
     *   - ist er jedoch illegaler Einwanderer, halbiert sich sein Lohn
     *   - ist er Teil des Aufsichtsrats ist er automatisch Freund des Managers
     * - der Manager bekommt das 5x eines normalen Angestellten
     */


    /**
     * Das Gehalt eines normalen Angestellten.
     */
    def salaryForClerk =
        TODO

    /**
     * Das Gehalt eines Managers.
     */
    def salaryForManager =
        TODO

    /**
     * Das Gehalt eines Board Members.
     */
    def salaryForBoardMember =
        TODO

    /**
     * Das Gehalt eines Angestellten, der befreundet mit dem Manager ist.
     */
    def salaryForfriendOfManagerClerk =
        TODO

    /**
     * Das Gehalt eines Angestellten der illegal eingewandert ist.
     */
    def salaryForIllegalImmigrantClerk =
        TODO

    /**
     * Das Gehalt eines illegal eingewanderten Managers.
     */
    def salaryForIllegalImmigrantManager =
        TODO

    /**
     * Das Gehalt eines illegal eingewanderten Board Members.
     */
    def salaryForIllegalImmigrantBoardMember =
        TODO
}