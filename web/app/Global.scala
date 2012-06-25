import controllers.Home
import play.api._
import play.api.mvc.RequestHeader
import play.api.mvc.Results._

object Global extends GlobalSettings {

    override def onStart(app: Application) {
    }

    override def onHandlerNotFound(req: RequestHeader) =
        Home.isProduction match {
            case true => NotFound(views.html.notfound())
            case _ => super.onHandlerNotFound(req)
        }
}