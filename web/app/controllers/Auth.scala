package controllers

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._

import controllers.base.MyController
import service._
import com.loops101.util.EnvUtil
import scala.Some

object Auth extends MyController {

    def logout = Action {
        Redirect("/").withNewSession
    }

    // ===== CREDENTIALS

    private val loginForm =
        Form(tuple("mail" -> nonEmptyText, "pass" -> nonEmptyText))

    def login = Action {
        implicit req =>
            Ok(views.html.login())
    }

    def doLogin = Action {
        implicit req =>
            loginForm.bindFromRequest.fold(
                errForm => {
                    goToLoginPage(("message", "Please fill in your e-mail and password"), ("type", "warn"))
                },
                form => {
                    goToLoginPage(("message", "Your e-mail or password is incorrect"), ("type", "warn"))
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
                                                // TODO: create user
                                                Redirect(oauthDomain + routes.App.index().url)
                                                    .withSession((USER_ID, "TODO"), (USER_MAIL, "TODO"), (USER_NAME, "TODO"))
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
