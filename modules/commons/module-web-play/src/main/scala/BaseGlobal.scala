import controllers._

import com.loops101.boot.BaseBoot
import com.loops101.util._

import play.api._
import play.api.mvc._

trait BaseGlobal
    extends GlobalSettings with BaseBoot with WebUtil with BaseErrors with BaseSecure with Logging {

    self: Controller =>

    override def onError(req: RequestHeader, ex: Throwable) =
        isProduction match {
            case false => super.onError(req, ex)
            case _ =>
                log.error("internal error", ex)
                internalError(req)
        }

    override def onHandlerNotFound(req: RequestHeader) =
        isProduction match {
            case false => super.onHandlerNotFound(req)
            case _ =>
                log.warn("not found: {}", req.uri)
                notFound(req)
        }

    override def onBadRequest(req: RequestHeader, error: String) =
        isProduction match {
            case false => super.onBadRequest(req, error)
            case _ =>
                log.error("bad request: {}", error)
                badRequest(req)
        }

    override def onRouteRequest(req: RequestHeader): Option[Action[_]] =
        super.onRouteRequest(req).map {
            case action: Action[_] =>
                req.domain.startsWith("static.") match {
                    case true =>
                        // only allow access to /assets on static domain
                        req.path.startsWith("/assets") match {
                            case true => action
                            case false => Action(NotFound)
                        }
                    case _ =>
                        currentUserId(req) match {
                            case None =>
                                req.path match {
                                    case p if isRestrictedPath(p) => // requires login
                                        Authenticated(action)
                                    case p =>
                                        if (isEncryptedWhenLoggedOut(p))
                                            Encrypted(action) // required HTTPS for user/
                                        else
                                            Unencrypted(action)
                                }
                            case _ =>
                                req.path match {
                                    case p if isAdminPath(p) => // require HTTPS and ADMIN
                                        Encrypted(Admin(action))
                                    case p if isHiddenForLoggedInUsers(p) =>
                                        Action(redirectToHttps(req, "/", reason = Some("hidden")))
                                    case _ =>
                                        Encrypted(action)
                                }
                        }
                }
        }

    override protected def addShutdownHook() {
        // do nothing, let framework handle it
    }

    protected def isHiddenForLoggedInUsers(p: String) =
        p.startsWith("/users")

    protected def isRestrictedPath(p: String) =
        p.startsWith("/app") || p.startsWith("/user/") || p.startsWith("/api/")

    protected def isEncryptedWhenLoggedOut(path: String): Boolean =
        path.startsWith("/users") || path.endsWith("/contact") || path.contains("/interface")

    protected def isAdminPath(path: String) =
        path.startsWith("/mysecretadmin")
}
