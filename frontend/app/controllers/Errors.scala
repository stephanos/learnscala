package controllers

import play.api.mvc._
import controllers.base.MyController

object Errors
    extends MyController

trait Errors
    extends BaseErrors {

    self: Controller =>


    def accessDenied(req: RequestHeader) =
        Unauthorized(views.html.error.accessdenied()(req))

    def notFound(req: RequestHeader) =
        if (req.domain.startsWith("static."))
            NotFound
        else
            NotFound(views.html.error.notfound()(req))

    def internalError(req: RequestHeader) =
        InternalServerError(views.html.error.internalerror()(req))

    def badRequest(req: RequestHeader) =
        BadRequest(views.html.error.internalerror()(req))
}