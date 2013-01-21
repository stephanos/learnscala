package controllers

import controllers.base.MyController

object Quiz extends MyController {

  def hierarchy1() =
    views.html.quizzes.hierarchy1()

  def hierarchy2() =
    views.html.quizzes.hierarchy2()

  def list() =
    views.html.quizzes.list()

  def objekt() =
    views.html.quizzes.objekt()

  def klasse() =
    views.html.quizzes.klasse()

  def vererbung() =
    views.html.quizzes.vererbung()

  def enum() =
    views.html.quizzes.enum()

  def tupel() =
    views.html.quizzes.tupel()

  def opt() =
    views.html.quizzes.opt()

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
