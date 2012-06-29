package com.loops101.util

import org.mindrot.jbcrypt.BCrypt

class PassUtil {

    def encode(rawPass: String) =
        rawPass match {
            case "" | null => "" // don't encode empty passwords
            case s => BCrypt.hashpw(s, BCrypt.gensalt())
        }

    def isValid(encPass: String, rawPass: String) =
        if(encPass.isEmpty) // force empty password to always fail
            false
        else
            BCrypt.checkpw(rawPass, encPass)
}

object PassUtil extends PassUtil