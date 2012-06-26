package controllers

import play.api.mvc._
import play.api.mvc.Controller
import controllers.base.MyController

object Slide extends MyController {

    def test = Action {
        Ok(views.html.slides.test())
    }

}
