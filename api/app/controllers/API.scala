package controllers

import play.api.mvc._

object API
    extends Controller
    with de.learnscala.api.API {

    protected def userIsAdmin(implicit req: RequestHeader) = true
}
