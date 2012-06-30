package controllers

import play.api.mvc._
import controllers.base.MyController
import play.api.templates.Html

object Slide extends MyController {

    def index = Action {
        Ok(views.html.app.slides())
    }

    def index2 =
        index

    def redirect(id: String) = Action {
        Redirect(routes.Slide.load(id))
    }

    def redirect2(id1: String, id2: String) = Action {
        Redirect(routes.Slide.load2(id1, id2))
    }

    def load(id: String) =
        load2(null, id)

    def load2(id1: String, id2: String) = Action {
        try {
            val dir = if (id1 == null) "" else id1.replaceAllLiterally("-", "_") + "."
            val file = id2.replaceAllLiterally("-", "_")

            val c = Class.forName("views.html.slides." + dir + file)
            val m = c.getMethod("render")
            Ok(m.invoke(null).asInstanceOf[Html])
        } catch {
            case e =>
                Ok(views.html.error.notfound())
        }
    }
}
