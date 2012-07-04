package controllers

import play.api.mvc._
import controllers.base.MyController

object Home extends MyController {

    def index = Action {
        implicit req =>
            Ok(views.html.index())
    }

    def contact = Action {
        implicit req =>
            Ok(views.html.contact())
    }

    def booking = Action {
        implicit req =>
            Ok(views.html.booking())
    }

    def events = Action {
        implicit req =>
            Ok(views.html.events())
    }

    def details = Action {
            implicit req =>
                Ok(views.html.details())
        }

    def imprint = Action {
        implicit req =>
            Ok(views.html.imprint())
    }

    def backdoor = Action {
        implicit req =>
            Ok(views.html.backdoor())
    }

    def gverify = Action {
        Ok("google-site-verification: google1388dbffebf166ff.html")
    }
}