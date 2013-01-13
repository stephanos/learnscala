package controllers.base

import controllers._
import play.api.mvc.RequestHeader
import play.api.templates.Html
import com.loops101.web.controllers.BaseController

class MyController
    extends BaseController with Errors {

    def pick(snippets: Html*): Html = {
        snippets(scala.util.Random.nextInt(snippets.length))
    }

    protected def userIsAdmin(implicit req: RequestHeader) =
        envUtil.isLocal || req.session.get(USER_ID) == Some("stephanos")

    protected def getByName(path: String, classes: List[Class[_]] = List(), args: List[AnyRef] = List()) = {
        val c = Class.forName(path)
        val m = c.getMethod("render", classes: _*)
        m.invoke(null, args: _*).asInstanceOf[Html]
    }
}
