package controllers

import play.api.mvc._
import controllers.base.MyController

object App extends MyController {

    def index = Action {
        Ok(views.html.app.index())
    }

    def index2 =
        index
}
