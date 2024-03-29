package controllers

import play.api.mvc._
import controllers.base.MyController

object Slide extends MyController {

  def index = Action {
    Ok(views.html.app.content())
  }

  def indexR = Action {
    Redirect(routes.Slide.index())
  }

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

  def loadNotes(id1: String, id2: String) = Action {
    Ok(views.html.slides.notes())
  }

  def loadSlide(id1: String, id2: String) = {
    val dir = if (id1 == null || id1 == "") "" else id1.replaceAllLiterally("-", "_") + "."
    val file = id2.replaceAllLiterally("-", "_")
    getByName("views.html.slides.sub." + dir + file)
  }
}
