import controllers.base.MyController
import controllers._
import play.api._

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

    override protected def isHiddenForLoggedInUsers(p: String) =
        p.startsWith("/users")

    override protected def isRestrictedPath(p: String) =
        p.startsWith("/app") || p.startsWith("/user/") || p.startsWith("/api/")

    override protected def isEncryptedWhenLoggedOut(path: String): Boolean =
        path.startsWith("/users") || path.endsWith("/contact") || path.contains("/interface")

    override protected def isAdminPath(path: String) =
        path.startsWith("/mysecretadmin")
}