package controllers

import play.api.mvc._
import play.api.templates.Html
import controllers.base.MyController

object Editor extends MyController {

    def index = Action {
        Ok(views.html.app.editor())
    }

    def index2 =
        index
}
