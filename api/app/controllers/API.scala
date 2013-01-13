package controllers

import play.api.mvc._
import com.loops101.util._

object API
    extends Controller
    with de.learnscala.api.API with LogUtil with EnvUtil with SystemUtil {

    protected def userIsAdmin(implicit req: RequestHeader) = true
}
