package com.loops101.system.mail

import com.loops101.system.config.ConfigUtil
import com.loops101.system.mail.impl.{MailSession, MailFormatter, MailSender}

trait MailUtil
  extends MailSender
  with MailFormatter
  with MailSession {

  self: ConfigUtil =>

}