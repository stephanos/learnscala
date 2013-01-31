package controllers

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import controllers.base.MyController
import com.loops101.system.mail.{Mail, MailUtil}
import com.loops101.util._
import com.loops101.system.config.ConfigUtil
import system.MyConfig

object Contact
  extends MyController
  with MailUtil
  with LogUtil
  with MyConfig
  with SystemUtil
  with PropUtil
  with TimeUtil {


  private val mailForm = Form(tuple("mail" -> email, "text" -> text))

  def doMail = Action {
    implicit req =>
      mailForm.bindFromRequest.fold(
        errForm =>
          Redirect("/").flashing(("type", "error"), ("message", "Es gab einen Fehler bei der Übertragung, bitte senden Sie doch eine Mail an stephan@learnscala.de")),
        f => {
          val (mail, msg) = f
          println("MESSAGE from '" + mail + "'")
          if (envUtil.isProduction)
            mailSender.invoke(Mail(mail, List("contact@learnscala.de"), "Kontakt", msg))
          Redirect(routes.Home.contact()).flashing(("type", "success"), ("message", "Vielen Dank für Ihr Nachricht."))
        }
      )
  }
}