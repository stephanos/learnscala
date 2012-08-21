import services.data._
import services.data.dao.UserRepo
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
        Redirect("/users/login").flashing(("message", "Bitte authentifizieren Sie sich für den Teilnehmerbereich"), ("type", "info"))

    override protected def isHiddenForLoggedInUsers(p: String) =
        !isRestrictedPath(p) && !p.startsWith("/assets")

    override protected def isRestrictedPath(p: String) =
        p.startsWith("/app")

    override protected def isEncryptedWhenLoggedOut(p: String): Boolean =
        true // encrypt everything

    override protected def onInternalRoute(req: RequestHeader, action: Action[_]): Action[_] = {
        val path = req.path
        UserRepo.findUser(currentUserId(req).getOrElse("")).map(_.confirmed.value.getOrElse(false)).getOrElse(false) match {
            case false if(isRestrictedPath(path) && !path.endsWith("/logout") && !path.endsWith("/wait")) => Action(Redirect("/app/wait"))
            case _ => super.onInternalRoute(req, action)
        }
    }


    //~ INTERNALS ====================================================================================

    /*
    private def setStdOut(out: PrintStream) {
        //System.setOut(out)
        Console.setOut(out)
    }
    */
}