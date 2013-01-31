package com.loops101.system.mail

case class Mail(from: String,
                to: List[String],
                subject: String,
                text: String,
                replyTo: Option[String] = None,
                html: Option[String] = None)