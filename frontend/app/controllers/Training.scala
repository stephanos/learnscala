package controllers

import play.api.mvc._
import controllers.base.MyController
import play.api.templates.Html

object Training extends MyController {

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

    def akkaWhy = Action {
        implicit req =>
            Ok(views.html.training.akka.why())
    }

    def scalaTrainings(place: String) = Action {
        implicit req =>
            Ok(getByName("views.html.events." + place))
    }
}