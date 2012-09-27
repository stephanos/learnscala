package controllers

import play.api.mvc._
import controllers.base.MyController

object Home extends MyController {

    def redirect = Action {
        Redirect(routes.Blog.index())
    }

    def AGB = Action {
        implicit req =>
            Ok(views.html.agb())
    }

    def links = Action {
        implicit req =>
            Ok(views.html.links())
    }

    def glossary = Action {
        implicit req =>
            Ok(views.html.links())
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