package com.loops101.system.config

trait ConfigKey[T] {
  def name: String

  override def toString =
    name.toLowerCase
}


//~ PUBLIC KEYS ===================================================================================

class PublicKey[T](val name: String) extends ConfigKey[T] {
  override def toString =
    "public." + super.toString
}

// Versions
case object AppVersion extends PublicKey[String]("about.build")


// Feature / Infrastructure Status
case object MailStatus extends PublicKey[String]("status.mail")
case object SearchStatus extends PublicKey[String]("status.search")


// Message
case object GlobalMsgText extends PublicKey[String]("msg.text")
case object GlobalMsgStart extends PublicKey[String]("msg.start")
case object GlobalMsgEnd extends PublicKey[String]("msg.end")



//~ INTERNAL KEYS =================================================================================

class InternalKey[T](val name: String) extends ConfigKey[T] {
  override def toString =
    "internal." + super.toString
}


// Credentials
case object MailHost extends InternalKey[String]("mail.host")
case object MailPass extends InternalKey[String]("mail.pass")
case object MailPort extends InternalKey[Int]("mail.port")
case object MailUser extends InternalKey[String]("mail.user")

case object CacheUser extends InternalKey[String]("cache.user")
case object CachePass extends InternalKey[String]("cache.pass")
case object CacheHost extends InternalKey[String]("cache.host")