package controllers

import play.api.mvc._
import controllers.base.MyController

object Training extends MyController {

  val scala = "Objektfunktionale Programmierung mit Scala"
  val scalaS = scala // "Schulung \"" + scala + "\""

  val akka = "Skalierbare, ausfallsichere Systeme mit Akka"
  val akkaS = akka // "Schulung \"" + akka + "\""

  def akkaIndex = Action {
    implicit req =>
      Ok(views.html.training.akka.description())
  }

  def scalaIndex = Action {
    implicit req =>
      Ok(views.html.training.scala.description())
  }

  def redirect = Action {
    Redirect(routes.Blog.index())
  }

  def redirect2 = Action {
    Redirect(routes.Blog.index())
  }

  def scalaFolien = Action {
    implicit req =>
      Ok(views.html.training.scala.folien())
  }

  def scalaWhy = Action {
    implicit req =>
      Ok(views.html.training.scala.why())
  }

  def scalaJava = Action {
    implicit req =>
      Ok(views.html.training.scala.java())
  }

  def scalaTrainings(place: String) = Action {
    implicit req =>
      Ok(getByName("views.html.events." + place))
  }
}