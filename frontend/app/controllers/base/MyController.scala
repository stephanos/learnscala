package controllers.base

import controllers._
import play.api.mvc.RequestHeader
import play.api.templates.Html

class MyController
    extends BaseController with Errors {

    def pick(snippets: Html*): Html = {
        snippets(scala.util.Random.nextInt(snippets.length))
    }

    protected def userIsAdmin(implicit req: RequestHeader) =
        isLocal || req.session.get(USER_ID) == Some("stephanos")

    protected def getByName(path: String) = {
        val c = Class.forName(path)
        val m = c.getMethod("render")
        m.invoke(null).asInstanceOf[Html]
    }
}
