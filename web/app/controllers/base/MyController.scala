package controllers.base

import controllers._
import play.api.mvc.RequestHeader

class MyController
    extends BaseController with Errors {

    protected def userIsAdmin(implicit req: RequestHeader) =
        req.session.get(USER_NAME) == Some("stephanos")
}
