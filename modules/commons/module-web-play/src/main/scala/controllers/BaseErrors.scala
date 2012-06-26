package controllers

import play.api.mvc._

trait BaseErrors {

    self: Controller =>


    def accessDenied(req: RequestHeader): Result

    def notFound(req: RequestHeader): Result

    def internalError(req: RequestHeader): Result

    def badRequest(req: RequestHeader): Result
}
