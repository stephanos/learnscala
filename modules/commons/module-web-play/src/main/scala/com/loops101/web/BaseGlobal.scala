package com.loops101.web

import com.loops101.util._

import controllers.impl._
import play.api._
import mvc._
import scala.Some
import util.WebUtil

trait BaseGlobal
    extends GlobalSettings with WebUtil with BaseErrors with BaseSecure with LogUtil {

    self: Controller =>

    //~ INTERFACE =================================================================================

    /**
     * Behaviour for 500: Internal Server Error
     */
    override final def onError(req: RequestHeader, ex: Throwable) =
        isProduction match {
            case false => super.onError(req, ex)
            case _ =>
                log.error("internal error", ex)
                internalError(req)
        }

    /**
     * Behaviour for 404: Not Found
     */
    override final def onHandlerNotFound(req: RequestHeader) =
        isProduction match {
            case false => super.onHandlerNotFound(req)
            case _ =>
                log.warn("not found: {}", req.uri)
                notFound(req)
        }

    /**
     * Behaviour for 400: Bad Request
     */
    override final def onBadRequest(req: RequestHeader, error: String) =
        isProduction match {
            case false => super.onBadRequest(req, error)
            case _ =>
                log.error("bad request: {}", error)
                badRequest(req)
        }

    /**
     * General routing behaviour (TODO: use filters from 2.1 ?)
     */
    override final def onRouteRequest(req: RequestHeader) =
        super.onRouteRequest(req).map {
            handler =>
                handler match {
                    case action: Action[_] =>
                        req.domain.startsWith("static.") match {
                            case true => onAssetRoute(req, action)
                            case _ =>
                                currentUserId(req) match {
                                    case None => onPublicRoute(req, action)
                                    case _ => onInternalRoute(req, action)
                                }
                        }
                    case other => other
                }
        }

    protected def onInternalRoute(req: RequestHeader, action: Action[_]): Action[_] =
        req.path match {
            case p if isAdminPath(p) => // admin? -> require HTTPS and ADMIN
                Encrypted(Admin(action))
            case p if isHiddenForLoggedInUsers(p) =>
                Action(redirectToHttps(req, getAppPath, reason = Some("hidden")))
            case _ =>
                Encrypted(action)
        }


    //~ INTERNAL ==================================================================================

    private def onAssetRoute(req: RequestHeader, action: Action[_]): Action[_] =
        req.path.startsWith(assetBase) match {
            case true => action
            case false => Action(NotFound) // only allow access to assets on static domain
        }

    private def onPublicRoute(req: RequestHeader, action: Action[_]): Action[_] =
        req.path match {
            case p if isRestrictedPath(p) => // requires login? -> authenticate
                Authenticated(action)
            case p =>
                if (isEncryptedWhenLoggedOut(p)) // requires HTTPS? -> redirect
                    Encrypted(action)
                else
                    Unencrypted(action)
        }


    //~ CONFIG ====================================================================================

    protected def getAppPath =
        "/"

    protected def assetBase =
        "/assets"

    protected def isHiddenForLoggedInUsers(p: String) =
        false

    protected def isRestrictedPath(p: String) =
        false

    protected def isEncryptedWhenLoggedOut(p: String): Boolean =
        false // NO HTTPS by default

    protected def isAdminPath(path: String) =
        path.startsWith("/mysecretadmin")
}
