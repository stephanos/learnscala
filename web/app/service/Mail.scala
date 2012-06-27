package service

import javax.mail._
import javax.mail.internet._
import javax.mail.Transport
import java.util.Properties
import java.util.Date

object Mail {

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
                mail.setSentDate(new Date())
                mail.setContent(content)

            case None =>
                mail.setContent(text, "text/plain")
        }

        // SEND
        try {
            transport.connect()
            Transport.send(mail, mail.getRecipients(Message.RecipientType.TO))
            transport.close()
            println("sent email '" + subject + "' from '" + from + "' to '" + to + "'")
            true
        } catch {
            case e =>
                e.printStackTrace()
                println(e, "error sending email '" + subject + "' from '" + from + "' to '" + to + "'")
                false
        }
    }

    //~ INTERNALS =================================================================================

    private lazy val session = {

        user = System.getenv("SMTP_USER")
        host = System.getenv("SMTP_HOST")
        pass = System.getenv("SMTP_PASS")
        port = System.getenv("SMTP_PORT")

        println("SMTP via user " + user + " at " + host + ":" + port)

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
}
