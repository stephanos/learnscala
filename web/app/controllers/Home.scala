package controllers

import play.api.mvc._
import java.util
import play.api.data.Form
import play.api.data.Forms._

object Home extends Controller {

    def index = Action {
        implicit req =>
            Ok(views.html.index())
    }

    def scala = Action {
        Ok(views.html.scala())
    }

    def advjs = Action {
        Ok(views.html.advjs())
    }

    def imprint = Action {
        Ok(views.html.imprint())
    }

    def backdoor = Action {
        Ok(views.html.backdoor())
    }

    private val mailForm = Form("mail" -> email)

    def doMail = Action {
        implicit req =>
            mailForm.bindFromRequest.fold(
                errForm =>
                    Redirect("/").flashing(("type", "error"), ("message", "Es gab einen Fehler bei der Registrierung, bitte senden Sie doch eine Mail an stephan@101loops.de")),
                m => {
                    println("SIGNUP: " + m)
                    if(isProduction)
                        Mail.sendMail(m, List("subscribe@101loops.com"), Some(m), "BERLEARN: " + m, m, None)
                    Redirect("/").flashing(("type", "success"), ("message", "Vielen Dank für Ihr Interesse, wir melden uns bei Neuigkeiten!"))
                }
            )
    }

    def gverify = Action {
        Ok("google-site-verification: google1388dbffebf166ff.html")
    }

    lazy val isProduction =
        Option(System.getenv("APP.MODE")).getOrElse(System.getProperty("APP.MODE", "dev")) match {
            case "production" => true
            case _ => false
        }

    lazy val v =
        new util.Date().getTime
}