package controllers

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import controllers.base.MyController
import services.Mail

object Contact extends MyController {

    private val mailForm = Form("mail" -> email)

    def doMail = Action {
        implicit req =>
            mailForm.bindFromRequest.fold(
                errForm =>
                    Redirect("/").flashing(("type", "error"), ("message", "Es gab einen Fehler bei der Registrierung, bitte senden Sie doch eine Mail an stephan@learnscala.de")),
                m => {
                    println("SIGNUP: " + m)
                    if (isProduction)
                        Mail.sendMail(m, List("subscribe@learnscala.de"), Some(m), "SUBSCRIBE: " + m, m, None)
                    Redirect("/").flashing(("type", "success"), ("message", "Vielen Dank für Ihr Interesse - Sie bekommen als Erster bescheid!"))
                }
            )
    }
}