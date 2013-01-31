package com.loops101.web.controllers.impl

import play.api.mvc.Results._
import play.api.mvc._

/**
 * Defines basic security logic
 */
trait SecurityUtil
  extends SessionUtil
  with HttpsUtil {

  self: IError =>


  //~ SHARED ======================================================================================

  protected val REDIRECT_TO = "redirect_to"
  protected val REDIRECT_ON = "redirect_on"


  protected case class Authenticated[A](action: Action[A]) extends Action[A] {

    def apply(req: Request[A]): Result =
      currentUserId(req).map {
        uid => action(req)
      }.getOrElse(
        redirectToLogin
        //.withCookies(Cookie("PLAY_REDIRECT", navdata)(REDIRECT_TO, req.path), (REDIRECT_ON, new Date().getTime.toString))
      )

    lazy val parser = action.parser
  }

  protected def redirectToLogin: Result =
    Redirect("/users/login#required")

  protected case class Admin[A](action: Action[A]) extends Action[A] {

    def apply(req: Request[A]) =
      userIsAdmin(req) match {
        case true => action(req)
        case _ => notFound(req)
      }

    lazy val parser = action.parser
  }

  protected def userIsAdmin(implicit req: RequestHeader): Boolean

  /**
   * BaseAuth
   */
  protected def BaseAuth[A](username: String, password: String)(action: Action[A]) =
    Action(action.parser) {
      req =>
        req.headers.get("Authorization").flatMap {
          authorization =>
            authorization.split(" ").drop(1).headOption.filter {
              encoded =>
                new String(org.apache.commons.codec.binary.Base64.decodeBase64(encoded.getBytes)).split(":").toList match {
                  case u :: p :: Nil if u == username && password == p => true
                  case _ => false
                }
            }.map(_ => action(req))
        }.getOrElse {
          Unauthorized.withHeaders("WWW-Authenticate" -> """Basic realm="Heroku"""")
        }
    }
}
