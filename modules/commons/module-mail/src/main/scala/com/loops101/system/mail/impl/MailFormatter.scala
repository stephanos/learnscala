package com.loops101.system.mail.impl

import com.loops101.system.mail.Mail
import com.loops101.util.TimeUtil
import javax.mail.Message
import javax.mail.internet.{MimeBodyPart, MimeMultipart, InternetAddress, MimeMessage}

private[mail] trait MailFormatter
  extends TimeUtil {

  self: MailSession =>


  class MailFormatterImpl {

    def format(mail: Mail): MimeMessage = {

      val mime = new MimeMessage(mailSession.get)

      // TO
      mime.setRecipients(Message.RecipientType.TO, mail.to.mkString(","))

      // REPLY TO
      mail.replyTo foreach {
        rpl => mime.setReplyTo(Array(new InternetAddress(rpl)))
      }

      // FROM
      mime.setFrom(new InternetAddress(mail.from))

      // CONTENT
      mime.setSubject(mail.subject)
      mail.html match {
        case Some(h) =>
          val content = new MimeMultipart("alternative")

          // TEXT PART
          val textPart = new MimeBodyPart()
          textPart.setText(mail.text)
          textPart.setHeader("MIME-Version", "1.0")
          textPart.setHeader("Content-Type", textPart.getContentType)
          content.addBodyPart(textPart)

          // HTML PART
          val htmlPart = new MimeBodyPart()
          htmlPart.setContent(h, htmlPart.getContentType)
          htmlPart.setHeader("MIME-Version", "1.0")
          htmlPart.setHeader("Content-Type", htmlPart.getContentType)
          content.addBodyPart(htmlPart)

          mime.setHeader("MIME-Version", "1.0")
          mime.setHeader("Content-Type", content.getContentType)
          mime.setHeader("X-Mailer", "Java-Mailer")
          mime.setSentDate(timeUtil.now.toDate)
          mime.setContent(content)

        case None =>
          mime.setContent(mail.text, "text/plain")
      }

      mime
    }
  }


  lazy val mailFormatter = new MailFormatterImpl
}