package com.loops101.model.dto.mail

import com.loops101.model.types._

case class MailVO(var typeOf: Int,
                  var content: MailContentVO,
                  var context: Map[String, String],
                  var from: MailSenderVO,
                  var to: MailRecipientVO,
                  var status: Int = MailStatus.NEW.id)

object MailVO {

    def apply(typeOf: Int, content: MailContentVO, context: Map[String, String], to: MailRecipientVO): MailVO =
        new MailVO(typeOf, content, context, new MailSenderVO(MailPartyType.ME.id), to)
}


// SENDER
case class MailSenderVO(var typeOf: Int = MailPartyType.ME.id,
                        var email: Option[String] = None,
                        var userId: Option[String] = None)

object MailSenderVO {

    def apply(email: String, userId: Option[String]): MailSenderVO =
        new MailSenderVO(MailPartyType.USER.id, Option(email), userId)
}


// RECIPIENT
case class MailRecipientVO(var typeOf: Int = MailPartyType.ME.id,
                           var email: List[String] = List(),
                           var userId: Option[String] = None,
                           var segment: Option[Long] = None)

object MailRecipientVO {

    def apply(email: String): MailRecipientVO =
        new MailRecipientVO(MailPartyType.USER.id, List(email))

    def apply(email: String, userId: Option[String]): MailRecipientVO =
        new MailRecipientVO(MailPartyType.USER.id, List(email), userId)
}


// CONTENT
case class MailContentVO(var subject: String, var msg: String = "")