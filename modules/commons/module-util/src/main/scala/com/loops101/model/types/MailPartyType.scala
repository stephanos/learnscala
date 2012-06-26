package com.loops101.model.types

object MailPartyType extends Enumeration {

    case class V(n: Int, descr: String) extends Val(n, n.toString)

    type MailPartyType = V

    val ME = V(0, "Me")
    val USER = V(1, "A User")
    val USERS = V(2, "Multiple Users")
    val USER_GROUP = V(3, "User Group")
}