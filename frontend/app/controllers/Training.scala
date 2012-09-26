package controllers

import play.api.mvc._
import controllers.base.MyController
import play.api.templates.Html

object Training extends MyController {

    def index = Action {
        implicit req =>
            Ok(views.html.training())
    }

    def redirect = Action {
        Redirect(routes.Training.index())
    }

    def redirect2 = Action {
        Redirect(routes.Training.index())
    }

    def folien = Action {
        implicit req =>
            Ok(views.html.folien())
    }

    def why = Action {
        implicit req =>
            Ok(views.html.why())
    }

    def pick(snippets: Html*): Html = {
        snippets(scala.util.Random.nextInt(snippets.length))
    }

    def trainings(place: String) = Action {
        implicit req =>
            Ok(getByName("views.html.events." + place))
    }
}