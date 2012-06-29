package com.loops101.model.types

object MailStatus extends Enumeration {

    case class V(n: Int, descr: String) extends Val(n, n.toString)

    type MailStatus = V

    val NEW = V(0, "New")
    val SENT = V(1, "Sent")
}