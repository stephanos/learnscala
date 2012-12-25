package controllers

import play.api.mvc._
import com.loops101.util.LogUtil

object API
    extends Controller
    with de.learnscala.api.API with LogUtil {

    protected def userIsAdmin(implicit req: RequestHeader) = true
}
