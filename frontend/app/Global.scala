import com.loops101.system.config.AppVersion
import com.loops101.web.BaseGlobal
import controllers.base.MyController
import controllers._
import play.api._
import system.MyConfig

object Global
  extends MyController
  with BaseGlobal
  with MyConfig
  with Errors {

  def NAME = "WEB"


  //~ INTERFACE =================================================================================

  override def beforeStart(app: Application) {
    println("Application starting ... ")

    // init database
    //MyDocDB.initDB()
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
    //if(isProduction) MyDocDB.stopDB()
  }


  //~ SHARED ====================================================================================

  override protected def getAppPath =
    "/app"

  override protected def redirectToLogin =
    Redirect("/users/login").flashing(("message", "Bitte authentifizieren Sie sich für den Teilnehmerbereich"), ("type", "info"))

  override protected def isHiddenForLoggedInUsers(p: String) =
    !p.startsWith("/app") && !p.startsWith("/assets") && !p.startsWith("/api")

  override protected def isRestrictedPath(p: String) =
    envUtil.isCloud && p.startsWith("/app")

  override protected def isEncryptedWhenLoggedOut(p: String): Boolean =
    !p.contains("/oeffentlich/") // encrypt everything except iframe pages

  /*
  override protected def onInternalRoute(req: RequestHeader, action: Action[_]): Action[_] = {
      val path = req.path
      val isLocal = req.host contains "localhost"
      (isLocal || UserRepo.findUser(currentUserId(req).getOrElse("")).map(_.confirmed.value.getOrElse(false)).getOrElse(false)) match {
          case false if(isRestrictedPath(path) && !path.endsWith("/logout") && !path.endsWith("/wait")) => Action(Redirect("/app/wait"))
          case _ => super.onInternalRoute(req, action)
      }
  }
  */
}