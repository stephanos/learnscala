package controllers

import play.api.mvc._
import play.api.templates.Html
import controllers.base.MyController

object Editor extends MyController {

    def index = Action {
        Ok(views.html.app.editor())
    }

    def indexR = Action {
        Redirect(routes.Editor.index())
    }

    def publicIndex = Action {
        Ok(views.html.editor())
    }

    def publicIndexR = Action {
        Redirect(routes.Editor.publicIndex())
    }
}
