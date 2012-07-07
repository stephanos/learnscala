package controllers

import play.api.mvc._
import controllers.base.MyController

object App extends MyController {

    def index = Action {
        Ok(views.html.app.index())
    }

    def waitingRoom = Action {
        Ok(views.html.app.wait())
    }

    def invalidBrowser = Action {
        Ok(views.html.error.unsupported())
    }

    def index2 =
        index
}
