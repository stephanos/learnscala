package controllers

import controllers.base.MyController

object Quiz extends MyController {

  def hierarchy1() =
    views.html.quizzes.hierarchy1()

  def hierarchy2() =
    views.html.quizzes.hierarchy2()

  def list() =
    views.html.quizzes.list()

  def data1_a() =
    views.html.quizzes.data1_a()

  def data1_b() =
    views.html.quizzes.data1_b()

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

  def packages() =
    views.html.quizzes.packages()
}
