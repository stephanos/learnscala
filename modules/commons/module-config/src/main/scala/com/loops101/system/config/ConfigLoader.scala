package com.loops101.system.config

trait ConfigLoader {


  class ConfigLoaderImpl {

    def get: Map[String, _ <: AnyRef] = Map()
  }


  lazy val confLoader = new ConfigLoaderImpl
}
