package controllers

import play.api.mvc._
import controllers.base.MyController

object Glossary extends MyController {

  def index() = Action {
    Ok(views.html.app.glossary())
  }

  def indexR() = Action {
    Redirect(routes.Glossary.index())
  }

  def page(id: String) = Action {
    Ok(getByName("views.html.glossaries._" + id, List(classOf[String]), List("/app/glossary")))
  }

  def pageR(id: String) = Action {
    Redirect(routes.Glossary.page(id))
  }


  def publicIndex() = Action {
    Ok(views.html.glossar())
  }

  def publicIndexR() = Action {
    Redirect(routes.Glossary.publicIndex())
  }

  def publicPage(id: String) = Action {
    Ok(getByName("views.html.glossaries._" + id, List(classOf[String]), List("/glossar")))
  }

  def publicPageR(id: String) = Action {
    Redirect(routes.Glossary.publicPage(id))
  }

}
