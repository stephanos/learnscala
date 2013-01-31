package com.loops101.web.controllers.impl

import com.loops101.util._
import play.api.mvc.Results._
import play.api.mvc._

/**
 * Defines basic security logic
 */
trait HttpsUtil
  extends EnvUtil {


  def isHTTPS(implicit req: RequestHeader): Boolean =
    getProtocol(req.headers) == "https"


  //~ SHARED ======================================================================================

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


  //~ INTERNALS ===================================================================================

  private def redirectTo(req: RequestHeader, proto: String, customPath: String = null, reason: Option[String] = None) = {
    val protocol = if (envUtil.isCloud) proto + "://" else "http://"
    Redirect(protocol + req.headers.get("host").get + Option(customPath).getOrElse(req.uri))
      .withHeaders(("X-Redirect-Reason", reason.getOrElse("none")))
  }
}
