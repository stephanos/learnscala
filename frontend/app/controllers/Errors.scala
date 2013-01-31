package controllers

import play.api.mvc._
import controllers.base.MyController
import com.loops101.web.controllers.impl.IError

object Errors
  extends MyController

trait Errors
  extends IError {

  self: Controller =>


  def accessDenied(req: RequestHeader) =
    Unauthorized(views.html.error.accessdenied())

  def notFound(req: RequestHeader) =
    if (req.domain.startsWith("static."))
      NotFound
    else
      NotFound(views.html.error.notfound())

  def notFoundPage() = Action {
    implicit req =>
      notFound(req)
  }

  def internalError(req: RequestHeader) =
    InternalServerError(views.html.error.internalerror())

  def internalErrorPage() = Action {
    implicit req =>
      internalError(req)
  }

  def badRequest(req: RequestHeader) =
    BadRequest(views.html.error.internalerror())
}