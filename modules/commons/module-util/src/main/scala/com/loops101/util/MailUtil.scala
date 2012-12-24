package com.loops101.util

import javax.mail._
import internet._
import java.util.Properties

trait MailUtil extends LogUtil {

    //~ SETUP ======================================================================================

    var port: String = _
    var user: String = _
    var host: String = _
    var pass: String = _


    //~ INTERFACE ==================================================================================

    def sendMail(from: String, to: List[String], replyTo: Option[String],
                 subject: String, text: String, html: Option[String]) = {

        val transport = session.getTransport
        val mail = new MimeMessage(session)

        // TO
        mail.setRecipients(Message.RecipientType.TO, to.mkString(","))
        if (replyTo.isDefined) mail.setReplyTo(Array(new InternetAddress(replyTo.get)))

        // FROM
        mail.setFrom(new InternetAddress(from))

        // CONTENT
        mail.setSubject(subject)
        html match {
            case Some(h) =>
                val content = new MimeMultipart("alternative")

                // TEXT PART
                val textPart = new MimeBodyPart()
                textPart.setText(text)
                textPart.setHeader("MIME-Version", "1.0")
                textPart.setHeader("Content-Type", textPart.getContentType)
                content.addBodyPart(textPart)

                // HTML PART
                val htmlPart = new MimeBodyPart()
                htmlPart.setContent(h, htmlPart.getContentType)
                htmlPart.setHeader("MIME-Version", "1.0")
                htmlPart.setHeader("Content-Type", htmlPart.getContentType)
                content.addBodyPart(htmlPart)

                mail.setHeader("MIME-Version", "1.0")
                mail.setHeader("Content-Type", content.getContentType)
                mail.setHeader("X-Mailer", "Java-Mailer")
                mail.setSentDate(TimeUtil.now)
                mail.setContent(content)

            case None =>
                mail.setContent(text, "text/plain")
        }

        // SEND
        try {
            transport.connect()
            Transport.send(mail, mail.getRecipients(Message.RecipientType.TO))
            transport.close()
            log.info("sent email '{}' from '{}' to '{}'",  subject, from, to)
            true
        } catch {
            case e: Throwable =>
                log.error(e, "error sending email '" + subject + "' from '" + from + "' to '" + to + "'")
                false
        }
    }

    //~ INTERNALS =================================================================================

    private lazy val session = {

        if (!loadFromProps(Map("user" -> getEnv("SMTP_USER"), "pass" -> getEnv("SMTP_PASS"),
            "host" -> getEnv("SMTP_HOST"), "port" -> getEnv("SMTP_PORT"))))
            loadFromProps(PropUtil.loadFile("smtp.properties").getOrElse(Map()))

        log.info("SMTP via user {} at {}:{}", user, host, port)

        val props = new Properties()

        props.put("mail.smtp.host", host)
        props.put("mail.smtp.port", port)
        props.put("mail.smtp.auth", "true")

        props.put("mail.smtp.sendpartial", "true") // send mail even if some addresses are invalid
        props.put("mail.transport.protocol", "smtp")
        props.put("mail.smtp.starttls.enable", "true") // required for GMail

        //props.put("mail.smtp.socketFactory.port", port)
        //props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Session.getInstance(props,
            new javax.mail.Authenticator() {
                override def getPasswordAuthentication = new PasswordAuthentication(user, pass)
            })
    }

    private def getEnv(name: String) =
        SystemUtil.getEnvProperty(name).getOrElse(null)

    private def loadFromProps(props: Map[String, String]): Boolean = {
        user = props.getOrElse("user", null)
        host = props.getOrElse("host", null)
        pass = props.getOrElse("pass", null)
        port = props.getOrElse("port", null)
        (user != null && host != null && pass != null && port != null)
    }
}

object MailUtil extends MailUtil