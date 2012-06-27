package service

import play.api.libs.ws.WS
import com.loops101.util.Logging

object GithubAPI extends Logging {

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
}