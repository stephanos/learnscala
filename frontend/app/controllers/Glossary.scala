package controllers

import play.api.mvc._
import play.api.templates.Html
import controllers.base.MyController

object Glossary extends MyController {

    def index1 =
        index2

    def index2 = Action {
        Ok(views.html.app.glossary())
    }

    def page(id: String) = Action {
        Ok(getByName("views.html.glossaries._" + id))
    }

    def pageR(id: String) = Action {
        Redirect(routes.Glossary.page(id))
    }

}
