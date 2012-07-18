package controllers

import play.api.mvc._
import controllers.base.MyController
import play.api.templates.Html

object Slide extends MyController {

    def index = Action {
        Ok(views.html.slides.main())
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
        load2("", id)

    def load2(id1: String, id2: String) = Action {
        Ok(views.html.slides.wrap(id1, id2))
    }

    def loadSlide(id1: String, id2: String) = {
        val dir = if (id1 == null || id1 == "") "" else id1.replaceAllLiterally("-", "_") + "."
        val file = id2.replaceAllLiterally("-", "_")

        val c = Class.forName("views.html.slides.sub." + dir + file)
        val m = c.getMethod("render")
        m.invoke(null).asInstanceOf[Html]
    }
}
