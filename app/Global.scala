import services.data._
import com.loops101.util.EnvUtil
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
        MyDocDB.initDB()
    }

    override def onStart(app: Application) {
        println("Application has started (" + build + ")")

        // init custom stdout
        //setStdOut(MyPrintStream.stdout)

        // start up
        onStartup()
    }

    override def onStop(app: Application) {
        println("Application shutdown ...")

        // close custom stdout
        //setStdOut(MyPrintStream.stdout.orig)

        // stop database
        if (isProduction) MyDocDB.stopDB()

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
        if (!EnvUtil.isProduction)
            DebugData.reset()
    }

    override protected def getAppPath =
        "/app"

    override protected def redirectToLogin =
        Redirect("/users/login").flashing(("message", "Please login in order to access the members area"), ("type", "info"))

    override protected def isHiddenForLoggedInUsers(p: String) =
        !isRestrictedPath(p) && !p.startsWith("/assets")

    override protected def isRestrictedPath(p: String) =
        p.startsWith("/app") || p.startsWith("/api")

    override protected def isEncryptedWhenLoggedOut(p: String): Boolean =
        true // encrypt everything


    //~ INTERNALS ====================================================================================

    /*
    private def setStdOut(out: PrintStream) {
        //System.setOut(out)
        Console.setOut(out)
    }
    */
}