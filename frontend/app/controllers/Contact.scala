package controllers

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import controllers.base.MyController
import services.Mail

object Contact extends MyController {

    private val mailForm = Form(tuple("mail" -> email, "text" -> text))

    def doMail = Action {
        implicit req =>
            mailForm.bindFromRequest.fold(
                errForm =>
                    Redirect("/").flashing(("type", "error"), ("message", "Es gab einen Fehler bei der Übertragung, bitte senden Sie doch eine Mail an stephan@learnscala.de")),
                f => {
                    val (mail, msg) = f
                    println("MESSAGE from '" + mail + "'")
                    if (isProduction)
                        Mail.sendMail(mail, List("contact@learnscala.de"), Some(mail), "Kontakt", msg, None)
                    Redirect(routes.Home.contact()).flashing(("type", "success"), ("message", "Vielen Dank für Ihr Nachricht."))
                }
            )
    }
}