package controllers

import play.api.mvc._
import play.api.templates.Html
import controllers.base.MyController

object Slide extends MyController {

    def index = Action {
        Ok(views.html.app.content())
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
        Ok(loadSlide(id1, id2))
        //Ok(views.html.slides.wrap(id1, id2))
    }

    def loadSlide(id1: String, id2: String) = {
        val dir = if (id1 == null || id1 == "") "" else id1.replaceAllLiterally("-", "_") + "."
        val file = id2.replaceAllLiterally("-", "_")
        getByName("views.html.slides.sub." + dir + file)
    }

    def glossary1 =
        glossary2

    def glossary2 = Action {
        Ok(views.html.slides.glossary.all())
    }

    def glossary(id: String) = Action {
        Ok(getByName("views.html.slides.glossary." + id))
    }

    def glossaryR(id: String) = Action {
        Redirect(routes.Slide.glossary(id))
    }

    private def getByName(path: String) = {
        val c = Class.forName(path)
        val m = c.getMethod("render")
        m.invoke(null).asInstanceOf[Html]
    }
}
