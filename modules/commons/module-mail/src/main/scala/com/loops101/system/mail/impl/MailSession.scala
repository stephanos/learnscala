package com.loops101.system.mail.impl

import com.loops101.system.config._
import java.util.Properties
import javax.mail.{PasswordAuthentication, Session}

private[mail] trait MailSession {

  self: ConfigUtil =>


  class MailSessionImpl {

    lazy val get: Session = {

      val props = new Properties()

      props.put("mail.smtp.host", config.getString(MailHost).get)
      props.put("mail.smtp.port", config.getString(MailPort).get)
      props.put("mail.smtp.auth", "true")

      props.put("mail.smtp.sendpartial", "true") // send mail even if some addresses are invalid
      props.put("mail.transport.protocol", "smtp")
      props.put("mail.smtp.starttls.enable", "true") // required for GMail

      //props.put("mail.smtp.socketFactory.port", port)
      //props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

      Session.getInstance(
        props,
        new javax.mail.Authenticator() {
          override def getPasswordAuthentication =
            new PasswordAuthentication(
              config.getString(MailUser).get,
              config.getString(MailPass).get
            )
        }
      )
    }
  }


  lazy val mailSession = new MailSessionImpl
}