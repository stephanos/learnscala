package controllers

import play.api.mvc._
import controllers.base.MyController

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

    def trainings(place: String) = Action {
        implicit req =>
            Ok(getByName("views.html.events." + place))
    }
}