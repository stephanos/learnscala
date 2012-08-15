package controllers

import controllers.base.MyController

object Quiz extends MyController {

    def hierarchy() =
        views.html.quizzes.hierarchy()

    def variables() =
        views.html.quizzes.variables()

    def collection() =
        views.html.quizzes.collection()

    def methoden1() =
        views.html.quizzes.methoden1()

    def methoden2() =
        views.html.quizzes.methoden2()
}
