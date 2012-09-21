package controllers

import play.api.mvc._
import controllers.base.MyController

object Home extends MyController {

    def redirect = Action {
        Redirect(routes.Home.index())
    }

    def redirect2 = Action {
        Redirect(routes.Home.index())
    }

    def AGB = Action {
        implicit req =>
            Ok(views.html.agb())
    }

    def index = Action {
        implicit req =>
            Ok(views.html.index())
    }

    def links = Action {
        implicit req =>
            Ok(views.html.links())
    }

    def folien = Action {
        implicit req =>
            Ok(views.html.folien())
    }

    def contact = Action {
        implicit req =>
            Ok(views.html.contact())
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