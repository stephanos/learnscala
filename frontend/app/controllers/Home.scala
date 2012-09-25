package controllers

import play.api.mvc._
import controllers.base.MyController

object Home extends MyController {

    def redirect = Action {
        Redirect(routes.Home.blog())
    }

    def redirect2 = Action {
        Redirect(routes.Home.blog())
    }

    def redirect3 = Action {
        Redirect(routes.Home.blog())
    }

    def AGB = Action {
        implicit req =>
            Ok(views.html.agb())
    }

    def training = Action {
        implicit req =>
            Ok(views.html.training())
    }

    def training2(place: String) = Action {
        implicit req =>
            Ok(getByName("views.html.events." + place))
    }

    def links = Action {
        implicit req =>
            Ok(views.html.links())
    }

    def folien = Action {
        implicit req =>
            Ok(views.html.folien())
    }

    def blog = Action {
        implicit req =>
            Ok(views.html.blogs())
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

    def entry(date: String, title: String) = Action {
        implicit req =>
            Ok(loadEntry(date, title, full = true))
    }

    def loadEntry(date: String, title: String, full: Boolean = false) = {
        val dir = "d" + date.replaceAllLiterally("-", "_") + "."
        val file = title.replaceAllLiterally("-", "_") + (if (full) ".full" else ".content")
        getByName("views.html.blog." + dir + file)
    }

    def gverify = Action {
        Ok("google-site-verification: google1388dbffebf166ff.html")
    }
}