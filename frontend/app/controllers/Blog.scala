package controllers

import controllers.base.MyController
import play.api.mvc.Action

object Blog extends MyController {

    def index = Action {
        implicit req =>
            Ok(views.html.blogs())
    }

    def entry(year: Int, month: Int, day: Int, url: String) = Action {
        implicit req =>
            try {
                Ok(load(year, month, day, url, full = true))
            } catch {
                case RedirectException(t) =>
                    Redirect(t)
            }
    }

    def load(year: Int, month: Int, day: Int, url: String = null, full: Boolean = false) = {
        val dir = "d" + year + "." + "d" + month.formatted("%02d") + "." + "d" + day.formatted("%02d")
        val c = Class.forName("views.html.blog." + dir + ".meta")
        println(dir)
        val m_url = "/" + dir.replaceAllLiterally(".", "/").replaceAllLiterally("d", "") + "/" + c.getMethod("url").invoke(null).asInstanceOf[String]
        val m_title = c.getMethod("title").invoke(null).asInstanceOf[String]

        lazy val head = getByName("views.html.blog." + dir + ".head")

        if(url != null) {
            if(!m_url.endsWith(url))
                throw new RedirectException(m_url)
            val body = getByName("views.html.blog." + dir + ".body")
            views.html.blog.page(m_title)(head + body)
        } else {
            views.html.blog.snippet(m_title, m_url)(head)
        }
    }

    case class RedirectException(target: String) extends Exception
}
