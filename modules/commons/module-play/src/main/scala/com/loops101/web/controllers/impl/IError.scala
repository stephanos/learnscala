package com.loops101.web.controllers.impl

import play.api.mvc._

/**
 * Defines default error responses
 */
trait IError {

    self: Controller =>


    def accessDenied(req: RequestHeader): Result

    def notFound(req: RequestHeader): Result

    def internalError(req: RequestHeader): Result

    def badRequest(req: RequestHeader): Result
}
