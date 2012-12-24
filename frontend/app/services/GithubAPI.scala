package services

import play.api.libs.ws.WS
import net.liftweb.json._
import com.loops101.util.LogUtil
import services.data.model.UserDoc

object GithubAPI extends LogUtil {

    private val base = "https://api.github.com"

    case class Request(url: String, data: String*) {

        def fetch(token: String) = {
            val req = base + url.format(data)
            log.debug("WS GET " + req)
            WS.url(req).withHeaders(("Authorization", "token " + token)).get()
        }
    }

    def getUser(id: Option[String] = None) =
        Request("/user" + id.map("s/" + _).getOrElse(""))

    def parseUser(s: String) = {
        val json = parse(s)

        val id = (json \ "id") match {
            case JInt(v) => Some(v.toInt)
            case _ => None
        }
        val login = (json \ "login") match {
            case JString(v) => Some(v)
            case _ => None
        }
        val email = (json \ "email") match {
            case JString(v) => Some(v)
            case _ => None
        }
        val name = (json \ "name") match {
            case JString(v) => Some(v)
            case _ => None
        }

        UserDoc.create
            .gid(id).email(email).fullname(name).name(login)
    }
}