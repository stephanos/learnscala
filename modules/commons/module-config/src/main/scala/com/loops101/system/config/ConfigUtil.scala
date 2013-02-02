package com.loops101.system.config

/**
 * Configuration interface
 */
trait ConfigUtil {


  val config =
    new {
      def getDouble[T](key: ConfigKey[T]): Option[Double] =
        confHolder.getDouble(key)

      def getInt[T](key: ConfigKey[T]): Option[Int] =
        confHolder.getInt(key)

      def getString[T](key: ConfigKey[T]): Option[String] =
        confHolder.getString(key)
    }


  def confHolder: ConfigHolder
}