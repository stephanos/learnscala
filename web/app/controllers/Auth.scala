package controllers

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._

import controllers.base.MyController
import service._
import com.loops101.util.EnvUtil
import com.learnscala.data.dao.UserRepo

object Auth extends MyController {

    def logout = Action {
        Redirect("/").withNewSession
    }

    // ===== CREDENTIALS

    private val loginForm =
        Form(tuple("name" -> nonEmptyText, "pass" -> nonEmptyText))

    def login = Action {
        implicit req =>
            Ok(views.html.login())
    }

    def doLogin = Action {
        implicit req =>
            loginForm.bindFromRequest.fold(
                errForm => {
                    goToLoginPage(("message", "Please fill in your name and password"), ("type", "warn"))
                },
                form => {
                    goToLoginPage(("message", "Your name or password is incorrect"), ("type", "warn"))
                }
            )
    }

    // ===== OAUTH2

    def oauth = Action {
        Redirect(GithubOAuth.getAuthorizeUrl)
    }

    def doOAuth = Action {
        implicit req =>
            val code: String = req.queryString.get("code").get.head
            Async {
                GithubOAuth.getAccessTokenEndpoint(code).map {
                    authResp =>
                        val authData =
                            authResp.body.split('&').foldLeft(Map[String, String]()) {
                                (m, t) =>
                                    val pair = t.split('=')
                                    m ++ Map(pair(0) -> pair(1))
                            }
                        authData.get("access_token") match {
                            case Some(t) =>
                                log.info("received response from GitHub OAuth: {}", authData)
                                Async {
                                    GithubAPI.getUser().fetch(t).map {
                                        userResp =>
                                            val userData = userResp.body
                                            val usrStatus = userResp.status

                                            if (usrStatus == 200) {
                                                log.info("received GitHub user data: {}", userData)
                                                val user = UserRepo.findOrCreate(GithubAPI.parseUser(userData).githubToken(t))
                                                //guser data: {"total_private_repos":5,"type":"User","public_gists":15,"owned_private_repos":5,"login":"stephanos","private_gists":3,"plan":{"space":614400,"collaborators":1,"private_repos":5,"name":"micro"},"following":0,"html_url":"https://github.com/stephanos","gravatar_id":"d6bafd9b596bb96d81a4700489616488","collaborators":1,"avatar_url":"https://secure.gravatar.com/avatar/d6bafd9b596bb96d81a4700489616488?d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-140.png","followers":1,"created_at":"2009-11-30T12:09:10Z","disk_usage":24240,"url":"https://api.github.com/users/stephanos","id":159852,"public_repos":6}
                                                Redirect(oauthDomain + routes.App.index().url)
                                                    .withSession((USER_ID, user.gid.value.toString), (USER_NAME, user.name.value))
                                            } else {
                                                log.warn("unable to get GitHub user data: {}", userData)
                                                goToLoginPage(("message", "Communication with GitHub failed"), ("type", "error"))
                                            }
                                    }
                                }
                            case _ =>
                                log.warn("unable to get access token from GitHub: {}", authData)
                                goToLoginPage(("message", "Communication with GitHub failed"), ("type", "error"))
                        }
                }
            }
    }

    private def oauthDomain =
        "http://" + (if (EnvUtil.isProduction) "learnscala.de" else "localhost:9000")

    private def goToLoginPage(flash: (String, String)*)(implicit f: Flash) =
        Redirect(oauthDomain + routes.Auth.login().url).flashing(flash: _*)
}