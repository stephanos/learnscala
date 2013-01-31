package com.loops101.system.mail

import com.loops101.system.config.{ConfigUtil, MockConfigUtil}
import com.loops101.system.mail.impl.{MailFormatter, MailSession, MailSender}
import com.loops101.test.spec.MockFactorySpec

class MailSenderSpec
  extends MockFactorySpec[MailSender] {

  "Mail Sender" should {

    /*
    "send mail" >> {

      // mock
      val msg = mocking[MimeMessage] {
        _.getAllRecipients returns Array(new InternetAddress("you"))
      }

      // execute
      target.mailSender.transmit(msg) === true
    }

    "handle error" >> {

      target.mailSender.transmit(null) === false
    }
    */
  }


  //~ SETUP =====================================================================================

  lazy val factory =
    new MockConfigUtil with MailSender with MailSession with MailFormatter

  // PS: javax.mail.Session cannot be mocked
}
