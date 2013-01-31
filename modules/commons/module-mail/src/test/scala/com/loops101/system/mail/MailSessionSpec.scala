package com.loops101.system.mail

import com.loops101.system.config.MockConfigUtil
import com.loops101.test.spec.MockFactorySpec
import impl.MailSession
import scala.collection.JavaConverters._

class MailSessionSpec
  extends MockFactorySpec[MailSession] {

  "Mail Session" should {

    "create SMTP session" >> {

      val r = target.mailSession.get
      r !== null

      // check for host & port
      val props = r.getProperties.asScala
      props must contain("mail.smtp.host" -> "host")
      props must contain("mail.smtp.port" -> "port")

      // check for authentication
      //val auth = r.getPasswordAuthentication(new javax.mail.URLName(ConfigKey.MailHost.name))
      //auth.getUserName === "user"
      //auth.getPassword === "pass"
    }
  }


  //~ SETUP =====================================================================================

  lazy val factory =
    new MockConfigUtil with MailSession

}
