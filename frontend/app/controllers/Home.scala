package controllers

import play.api.mvc._
import controllers.base.MyController

object Home extends MyController {

    def redirect = Action {
        Redirect(routes.Home.training())
    }

    def redirect2 = Action {
        Redirect(routes.Home.training())
    }

    def AGB = Action {
        implicit req =>
            Ok(views.html.agb())
    }

    def training = Action {
        implicit req =>
            Ok(views.html.training())
    }

    def links = Action {
        implicit req =>
            Ok(views.html.links())
    }

    def folien = Action {
        implicit req =>
            Ok(views.html.folien())
    }

    def news = Action {
        implicit req =>
            Ok(views.html.news())
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