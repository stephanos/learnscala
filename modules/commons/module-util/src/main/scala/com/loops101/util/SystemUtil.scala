package com.loops101.util

trait SystemUtil {

  class SystemUtilImpl {

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

    def getProperty(key: String): Option[String] =
      Option(System.getProperty(key))

    def getProperties =
      System.getProperties

    def getProperty(key: String, default: String = null): String =
      getProperty(key).getOrElse(default)

    def getEnvProperties =
      System.getenv()

    def getEnvProperty(key: String) =
      Option(System.getenv(key))
  }

  lazy val sysUtil = new SystemUtilImpl
}