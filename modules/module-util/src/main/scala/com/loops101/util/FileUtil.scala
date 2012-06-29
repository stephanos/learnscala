package com.loops101.util

import java.net.URL
import scala.collection.JavaConversions._
import org.apache.commons.io.{IOUtils, FileUtils}
import java.io.{FileOutputStream, File}

class FileUtil {

    // === WRITE FILE

    def write(f: File, data: String) {
        IOUtils.write(data, new FileOutputStream(f))
    }

    // === READ RESOURCES

    def loadResource(file: File): String =
        loadResource(file.toURI.toURL)

    def loadResource(url: URL): String =
        io.Source.fromURL(url).getLines().mkString

    def loadResource(path: String): String =
        loadResource(getClass.getResource(path))

    def listResources(path: String, extensions: String*): Seq[File] = {
        val file = new File(getClass.getResource(path).toURI)
        FileUtils.listFiles(file, extensions.toArray, true).toSeq
    }
}

object FileUtil extends FileUtil