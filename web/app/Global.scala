import com.loops101.util.EnvUtil
import controllers.base.MyController
import controllers._
import play.api._
import play.api.mvc._

object Global
    extends MyController with BaseGlobal with Errors {

    def NAME = "WEB"

    //~ INTERFACE =================================================================================

    override def beforeStart(app: Application) {
        println("Application starting ... ")

        // init database
        //MyDocDB.initDB()
    }

    override def onStart(app: Application) {
        println("Application has started (" + build + ")")

        // start up
        onStartup()
    }

    override def onStop(app: Application) {
        println("Application shutdown ...")

        // stop database
        //if (isProduction) MyDocDB.stopDB()

        // shut down
        onShutdown()
    }

    //~ SHARED ====================================================================================

    protected def configBoot() {

        // === boot sub-module
        if (!isCloud) {
            addBoot("com.crashnote.api.Boot")
            addBoot("com.crashnote.worker.Boot")
            addBoot("com.crashnote.simulator.Boot")
        }
    }

    override protected def afterBoot() {

        // === initialize debug sequence
        //DebugModule.launch()
    }

    override def onRouteRequest(req: RequestHeader): Option[Action[_]] = {
        if(req.path.endsWith("/do_oauth"))
            Some(Action(Auth.doOAuth))
        else
            super.onRouteRequest(req)
    }

    override protected def getAppPath =
        "/app"

    override protected def redirectToLogin =
        Redirect("/users/login").flashing(("message", "Please login in order to access the members area"), ("type", "info"))

    override protected def isHiddenForLoggedInUsers(p: String) =
        !isRestrictedPath(p)

    override protected def isRestrictedPath(p: String) =
        p.startsWith("/app")

    override protected def isEncryptedWhenLoggedOut(p: String): Boolean =
        true // encrypt everything
}