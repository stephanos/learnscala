package com.loops101.system.mail

import com.loops101.system.config.MockConfigUtil
import com.loops101.system.mail.impl.{MailSession, MailFormatter}
import com.loops101.test.spec.MockFactorySpec

class MailFormatterSpec
  extends MockFactorySpec[MailFormatter] {

  "Mail Formatter" should {


    "format" >> {

      val m = Mail("me", List("you", "him"), "subject", "body")
      val r = target.mailFormatter.format(m)
      r !== null

      (r.getFrom).mkString === "me"
      (r.getReplyTo).mkString === "me"
      (r.getAllRecipients).mkString(",") === "you,him"

      r.getContent === "body"
      r.getSubject === "subject"
      r.getContentType === "text/plain"
    }

    "format with reply-to" >> {

      val m = Mail("me", List("you"), "subject", "body", replyTo = Some("me2"))
      val r = target.mailFormatter.format(m)
      (r.getReplyTo).mkString === "me2"
    }

    "format with HTML" >> {

      val m = Mail("me", List("you"), "subject", "body", html = Some("<html></html>"))
      val r = target.mailFormatter.format(m)

      pending

      /*
      val body = (r.getContent).asInstanceOf[MimeMultipart]
      body.getCount === 2

      val text = (body.getBodyPart(0)).asInstanceOf[MimeMultipart]

      val html = (body.getBodyPart(1)).asInstanceOf[MimeMultipart]
      */
    }
  }


  //~ SETUP =====================================================================================

  lazy val factory =
    new MockConfigUtil with MailFormatter with MailSession
}
