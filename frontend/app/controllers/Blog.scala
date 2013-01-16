package controllers

import controllers.base.MyController
import play.api.mvc.Action
import java.util.{Locale, Date}
import java.text.SimpleDateFormat

object Blog extends MyController {

  def index = Action {
    implicit req =>
      Ok(views.html.blogs())
  }

  def entry3(year: Int, month: Int, day: Int, lang: String, part: String, url: String) = Action {
    implicit req =>
      try {
        Ok(load(year, month, day, lang, part, url, full = true))
      } catch {
        case RedirectException(t) =>
          Redirect(t)
      }
  }

  def entry2(year: Int, month: Int, day: Int, lang: String, url: String) =
    entry3(year, month, day, lang, null, url)

  def entry(year: Int, month: Int, day: Int, url: String) =
    entry3(year, month, day, null, null, url)

  def load(year: Int, month: Int, day: Int, lang: String = null, part: String = null, url: String = null, full: Boolean = false) = {
    val dir = "d" + year + "." + "d" + month.formatted("%02d") + "." + "d" + day.formatted("%02d") + Option(lang).map("." + _).getOrElse("") + Option(part).map("." + _).getOrElse("")
    val c = Class.forName("views.html.blog." + dir + ".meta")
    val m_url =
      (("." + dir).replaceAllLiterally(".d3", "/3")
        .replaceAllLiterally(".d2", "/2")
        .replaceAllLiterally(".d1", "/1")
        .replaceAllLiterally(".d0", "/0")
        .replaceAllLiterally(".", "/")) + "/" + c.getMethod("url").invoke(null).asInstanceOf[String]
    val m_title = c.getMethod("title").invoke(null).asInstanceOf[String]
    val m_langs = c.getMethod("langs").invoke(null).asInstanceOf[List[String]]

    val d = new Date(year, month - 1, day)
    val isOld = d.getTime < new Date(2012, 8, 25).getTime
    val dateStr = new SimpleDateFormat("dd. MMMM", Locale.GERMAN).format(d)

    lazy val head = getByName("views.html.blog." + dir + ".head")

    if (url != null) {
      if (!m_url.endsWith(url))
        throw new RedirectException(m_url)
      val body = getByName("views.html.blog." + dir + ".body")
      views.html.blog.page(m_title, dateStr, m_url, !isOld, m_langs)(head)(body)
    } else {
      views.html.blog.snippet(m_title, m_url, dateStr)(head)
    }
  }

  case class RedirectException(target: String) extends Exception

}
