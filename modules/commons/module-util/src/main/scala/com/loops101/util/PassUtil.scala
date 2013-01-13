package com.loops101.util

import org.mindrot.jbcrypt.BCrypt

/**
 * Password utilities
 */
trait PassUtil {

  class PassUtilImpl {

    def encode(rawPass: String): String =
      rawPass match {
        case "" | null => "" // don't encode empty passwords
        case s => BCrypt.hashpw(s, BCrypt.gensalt())
      }

    def isValid(encPass: String, rawPass: String): Boolean =
      encPass match {
        case "" => false // force empty password to always fail
        case p => BCrypt.checkpw(rawPass, p)
      }
  }

  lazy val passUtil = new PassUtilImpl
}