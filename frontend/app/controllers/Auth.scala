package controllers

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._

import controllers.base.MyController
import com.loops101.util._

object Auth extends MyController with PassUtil {

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
          goToLoginPage(("message", "Bitte geben Sie Name und Passwort ein"), ("type", "warn"))
        },
        form => {
          if (passUtil.isValid(passUtil.encode("tbsdZjqFBdxXrkt8Gm2Z7MqA"), form._2))
            Redirect(routes.App.index().url).withSession((USER_ID, "stephanos"), (USER_NAME, "stephanos"))
          else
            goToLoginPage(("message", "Ihr Name oder Password ist falsch"), ("type", "warn"))
          /*
          (UserRepo.findUser(form._1) flatMap {
              usr =>
                  if (PassUtil.isValid(usr.password.value.getOrElse(""), form._2) || (EnvUtil.isLocal && form._1 == form._2))
                      Some(goToAppPage(usr))
                  else
                      None
          }).getOrElse(goToLoginPage(("message", "Ihr Name oder Password ist falsch"), ("type", "warn")))
          */
        }
      )
  }

  // ===== OAUTH2

  //    def oauth = Action {
  //        Redirect(GithubOAuth.getAuthorizeUrl)
  //    }

  //    def doOAuth = Action {
  //        implicit req =>
  //            val code: String = req.queryString.get("code").get.head
  //            Async {
  //                GithubOAuth.getAccessTokenEndpoint(code).map {
  //                    authResp =>
  //                        val authData =
  //                            authResp.body.split('&').foldLeft(Map[String, String]()) {
  //                                (m, t) =>
  //                                    val pair = t.split('=')
  //                                    m ++ Map(pair(0) -> pair(1))
  //                            }
  //                        authData.get("access_token") match {
  //                            case Some(t) =>
  //                                log.info("received response from GitHub OAuth: {}", authData)
  //                                Async {
  //                                    GithubAPI.getUser().fetch(t).map {
  //                                        userResp =>
  //                                            val userData = userResp.body
  //                                            val usrStatus = userResp.status
  //
  //                                            if (usrStatus == 200) {
  //                                                log.info("received GitHub user data: {}", userData)
  //                                                goToAppPage(UserRepo.findOrCreate(GithubAPI.parseUser(userData).githubToken(t)))
  //                                            } else {
  //                                                log.warn("unable to get GitHub user data: {}", userData)
  //                                                goToLoginPage(("message", "Kommunikation mit GitHub fehlgeschlagen"), ("type", "error"))
  //                                            }
  //                                    }
  //                                }
  //                            case _ =>
  //                                log.warn("unable to get access token from GitHub: {}", authData)
  //                                goToLoginPage(("message", "Kommunikation mit GitHub fehlgeschlagen"), ("type", "error"))
  //                        }
  //                }
  //            }
  //    }

  //private def oauthDomain =
  //    "http://" + (if (EnvUtil.isProduction) "learnscala.de" else "localhost:9000")

  //    private def goToAppPage(user: UserDoc) =
  //        Redirect(routes.App.index().url)
  //            .withSession((USER_ID, user.name.value), (USER_NAME, user.fullname.value.getOrElse("unknown")))
  //
  private def goToLoginPage(flash: (String, String)*)(implicit f: Flash) =
    Redirect(routes.Auth.login().url).flashing(flash: _*)
}