package controllers

import controllers.base.MyController

object Quiz extends MyController {

  def hierarchy() =
    views.html.quizzes.hierarchy()

  def variable() =
    views.html.quizzes.variable()

  def operator() =
    views.html.quizzes.operator()

  def functions1() =
    views.html.quizzes.functions1()

  def functions2() =
    views.html.quizzes.functions2()

  def functions3() =
    views.html.quizzes.functions3()

  def functions4() =
    views.html.quizzes.functions4()

  def method1() =
    views.html.quizzes.method1()

  def method2() =
    views.html.quizzes.method2()
}
