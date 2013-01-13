package com.loops101.util

import org.apache.commons.io.IOUtils
import java.io.{FileOutputStream, InputStream}

trait JarUtil {

  class JarUtilImpl {

    def writeFromJar(resourceName: String, targetPath: String) {

      val in = getResourceAsStream(resourceName)
      val out = new FileOutputStream(targetPath)

      try {
        IOUtils.write(IOUtils.toByteArray(in), out)
      } finally {
        IOUtils.closeQuietly(in)
        IOUtils.closeQuietly(out)
      }
    }

    def readFromJar(resourceName: String): Array[Byte] = {

      var byteData: Array[Byte] = null
      val in = getResourceAsStream(resourceName)

      try {
        byteData = IOUtils.toByteArray(in)
      } finally {
        IOUtils.closeQuietly(in)
      }

      byteData
    }

    def getResourceAsStream(resourceName: String): InputStream = {

      var result: InputStream = null

      // try this class's classloader
      result = this.getClass.getResourceAsStream("/" + resourceName)

      // try this class's classloader
      if (result == null) result = this.getClass.getResourceAsStream(resourceName)

      // try system class loader
      if (result == null) result = ClassLoader.getSystemResourceAsStream("/" + resourceName)

      // try system class loader
      if (result == null) result = ClassLoader.getSystemResourceAsStream(resourceName)

      //  throw exception if not found
      if (result == null) sys.error("No file " + resourceName + " could be found in JARs")

      result
    }
  }

  lazy val jarUtil = new JarUtilImpl
}