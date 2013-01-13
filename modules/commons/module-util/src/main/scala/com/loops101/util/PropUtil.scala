package com.loops101.util

import java.util.Properties

/**
 * A very simple Scala wrapper of Properties.  Loads and/or stores a
 * map of name->value.  Note that there is a default value of "", so
 * using a non-existent name will not cause an exception.
 */
trait PropUtil extends JarUtil {

  class PropUtilImpl {

    def loadFile(fname: String): Option[Map[String, String]] = {
      try {
        val props = new Properties()
        props.load(jarUtil.getResourceAsStream(fname))
        val iter = props.entrySet.iterator
        val vals = scala.collection.mutable.Map[String, String]()
        while (iter.hasNext) {
          val item = iter.next
          vals += (item.getKey.toString -> item.getValue.toString)
        }
        Some(vals.toMap.withDefaultValue(""))
      }
      catch {
        case e: Exception => None
      }
    }

    /*
    def saveFile(sprops: Map[String, String], fname: String): Boolean = {
        try {
            val jprops = new Properties
            sprops.foreach(a => jprops.put(a._1, a._2))
            val file = new FileOutputStream(fname)
            jprops.store(file, "Scala Properties: " + fname)
            file.close
            true
        }
        catch {
            case e: Exception => println("Properties.saveFile: " + e)
            false
        }
    }
    */
  }

  lazy val propUtil = new PropUtilImpl
}