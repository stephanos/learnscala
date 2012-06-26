package controllers

import play.api.mvc._
import controllers.base.MyController

object Home extends MyController {

    def index = Action {
        implicit req =>
            Ok(views.html.index())
    }

    def imprint = Action {
        Ok(views.html.imprint())
    }

    def backdoor = Action {
        Ok(views.html.backdoor())
    }

    def gverify = Action {
        Ok("google-site-verification: google1388dbffebf166ff.html")
    }
}