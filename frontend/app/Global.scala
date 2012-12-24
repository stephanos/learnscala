import com.loops101.web.BaseGlobal
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
    }

    override def onStop(app: Application) {
        println("Application shutdown ...")

        // close custom stdout
        //setStdOut(MyPrintStream.stdout.orig)

        // stop database
        if(isProduction) MyDocDB.stopDB()
    }


    //~ SHARED ====================================================================================

    override protected def getAppPath =
        "/app"

    override protected def redirectToLogin =
        Redirect("/users/login").flashing(("message", "Bitte authentifizieren Sie sich für den Teilnehmerbereich"), ("type", "info"))

    override protected def isHiddenForLoggedInUsers(p: String) =
        !p.startsWith("/app") && !p.startsWith("/assets")  && !p.startsWith("/api")

    override protected def isRestrictedPath(p: String) =
        EnvUtil.isCloud && p.startsWith("/app")

    override protected def isEncryptedWhenLoggedOut(p: String): Boolean =
        !p.contains("/oeffentlich/") // encrypt everything except iframe pages

    override protected def onInternalRoute(req: RequestHeader, action: Action[_]): Action[_] = {
        val path = req.path
        val isLocal = req.host contains "localhost"
        (isLocal || UserRepo.findUser(currentUserId(req).getOrElse("")).map(_.confirmed.value.getOrElse(false)).getOrElse(false)) match {
            case false if(isRestrictedPath(path) && !path.endsWith("/logout") && !path.endsWith("/wait")) => Action(Redirect("/app/wait"))
            case _ => super.onInternalRoute(req, action)
        }
    }
}