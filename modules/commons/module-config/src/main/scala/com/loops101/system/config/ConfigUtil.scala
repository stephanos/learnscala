package com.loops101.system.config

import com.loops101.system.config.impl.ConfigHolder

trait ConfigUtil
  extends ConfigHolder
  with ConfigLoader {


  class ConfigUtilImpl {

    def getDouble[T](key: ConfigKey[T]): Option[Double] =
      confHolder.getDouble(key)

    def getInt[T](key: ConfigKey[T]): Option[Int] =
      confHolder.getInt(key)

    def getString[T](key: ConfigKey[T]): Option[String] =
      confHolder.getString(key)
  }


  lazy val config = new ConfigUtilImpl
}