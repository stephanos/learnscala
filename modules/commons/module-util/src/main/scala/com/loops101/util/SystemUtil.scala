package com.loops101.util

import java.util.UUID

class SystemUtil {

    lazy val tempDir =
        getProperty("java.io.tmpdir").get

    lazy val userDir =
        getProperty("user.dir").get

    lazy val isWindows =
        getProperty("os.name") match {
            case Some(n) => n.startsWith("Windows")
            case _ => false
        }

    lazy val isUnitTest =
        false //ClassUtil.checkIfClasspathContains("com.crashnote.spec")

    lazy val getNewline =
        getProperty("line.separator").get

    def guid =
        UUID.randomUUID().toString

    def getProperty(key: String): Option[String] =
        Option(System.getProperty(key))

    def getProperty(key: String, default: String = null): String =
        getProperty(key).getOrElse(default)

    def getEnvProperty(key: String) =
        Option(System.getenv(key))
}

object SystemUtil extends SystemUtil