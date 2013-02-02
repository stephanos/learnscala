package com.loops101.system.config

trait ConfigLoader {


  trait ConfigLoaderImpl {

    def get: Map[String, _ <: AnyRef]
  }


  def confLoader: ConfigLoaderImpl
}
