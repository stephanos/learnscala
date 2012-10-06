package controllers

import controllers.base.MyController

object Quiz extends MyController {

    def hierarchy() =
        views.html.quizzes.hierarchy()

    def variable() =
        views.html.quizzes.variable()

    def operator() =
        views.html.quizzes.operator()

    def collection() =
        views.html.quizzes.collection()

    def method1() =
        views.html.quizzes.method1()

    def method2() =
        views.html.quizzes.method2()
}
