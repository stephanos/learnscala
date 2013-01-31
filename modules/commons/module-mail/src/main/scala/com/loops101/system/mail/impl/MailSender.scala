package com.loops101.system.mail.impl

import com.loops101.system.mail.Mail
import com.loops101.util.LogUtil
import javax.mail.internet.MimeMessage

private[mail] trait MailSender
  extends LogUtil {

  self: MailSession
    with MailFormatter =>


  class MailSenderImpl {

    def invoke(mail: Mail) =
      transmit(mailFormatter.format(mail))


    //~ INTERNALS =================================================================================

    private[mail] def transmit(msg: MimeMessage) = {

      val transport = mailSession.get.getTransport

      try {
        transport.connect()
        transport.sendMessage(msg, msg.getAllRecipients)

        log.info("sent email '{}' from '{}' to '{}'",
          msg.getSubject, msg.getFrom, msg.getAllRecipients)
        true

      } catch {
        case e: Throwable =>
          log.error(e, s"error sending email '${msg.getSubject}' " +
            s"from '${msg.getFrom}' to '${msg.getAllRecipients}'")
          false

      } finally {
        transport.close()
      }
    }

  }


  lazy val mailSender = new MailSenderImpl
}