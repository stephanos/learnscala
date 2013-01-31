package com.loops101.web.controllers.impl

import play.api.mvc._

/**
 * Defines basic session access
 */
trait SessionUtil {

  //~ INTERNAL ====================================================================================

  protected val USER_ID = "id"
  protected val USER_SRC = "src"
  protected val USER_NAME = "name"
  protected val USER_MAIL = "mail"


  //~ SHARED ======================================================================================

  protected def isLoggedIn()(implicit req: RequestHeader) =
    currentUserId(req).isDefined

  protected def currentUserSource(implicit req: RequestHeader) =
    req.session.get(USER_SRC)

  protected def currentUserId(implicit req: RequestHeader) =
    req.session.get(USER_ID)

  protected def currentUserMail(implicit req: RequestHeader) =
    req.session.get(USER_MAIL)

  protected def currentUserName(implicit req: RequestHeader) =
    req.session.get(USER_NAME)

}
