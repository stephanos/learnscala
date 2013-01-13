package com.loops101.web.controllers.impl

import play.api.mvc._
import play.api.mvc.Results._
import com.loops101.util._

trait BaseSecure {

    self: BaseErrors with EnvUtil =>

    protected val USER_ID = "id"
    protected val USER_SRC = "src"
    protected val USER_NAME = "name"
    protected val USER_MAIL = "mail"

    protected val REDIRECT_TO = "redirect_to"
    protected val REDIRECT_ON = "redirect_on"


    // ==== HTTPS

    def isHTTPS(implicit req: RequestHeader): Boolean =
        getProtocol(req.headers) == "https"

    protected def getProtocol(headers: Headers) =
        headers.get("x-forwarded-proto").getOrElse("http")

    protected case class Protocol[A](action: Action[A], proto: String) extends Action[A] {

        def apply(req: Request[A]) = {
            envUtil.isCloud match {
                case true =>
                    if (proto == getProtocol(req.headers))
                        action(req)
                    else
                        redirectTo(req, proto, reason = Some(proto + " required"))
                case _ =>
                    action(req)
            }
        }

        lazy val parser = action.parser
    }

    protected object Encrypted {

        def apply[A](action: Action[A]) =
            Protocol(action, "https")
    }

    protected object Unencrypted {

        def apply[A](action: Action[A]) =
            Protocol(action, "http")
    }

    protected def redirectToHttps(req: RequestHeader, customPath: String = null, reason: Option[String] = None) =
        redirectTo(req, "https", customPath)

    protected def redirectToHttp(req: RequestHeader, customPath: String = null, reason: Option[String] = None) =
        redirectTo(req, "http", customPath, reason)

    private def redirectTo(req: RequestHeader, proto: String, customPath: String = null, reason: Option[String] = None) = {
        val protocol = if (envUtil.isCloud) proto + "://" else "http://"
        Redirect(protocol + req.headers.get("host").get + Option(customPath).getOrElse(req.uri))
            .withHeaders(("X-Redirect-Reason", reason.getOrElse("none")))
    }

    // ==== AUTH

    protected def userId(req: RequestHeader) =
        req.session.get(USER_ID)

    protected case class Authenticated[A](action: Action[A]) extends Action[A] {

        def apply(req: Request[A]): Result =
            userId(req).map {
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

    //currentUserId(req).filter(_ equals 1).isDefined ||
    //    currentUserMail(req).filter(_ equals "bigguy@crashnote.com").isDefined ||
    //    (!EnvUtil.isProduction && currentUserMail(req).filter(_ equals "admin").isDefined)


    protected def currentUserId(implicit req: RequestHeader) =
        req.session.get(USER_ID)

    protected def currentUserMail(implicit req: RequestHeader) =
        req.session.get(USER_MAIL)

    protected def currentUserName(implicit req: RequestHeader) =
        req.session.get(USER_NAME)

    //protected def currentUser(implicit req: RequestHeader) = currentUserId(req) match {
    //    case Some(id) => Some(UserVO(id, currentUserMail(req).get)
    //    case _ => None
    //}

    protected def isLoggedIn()(implicit req: RequestHeader) =
        currentUserId(req).isDefined


    // ==== BASE AUTH

    def BaseAuth[A](username: String, password: String)(action: Action[A]) =
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
