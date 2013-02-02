package com.loops101.system.config

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

/**
 * Default config (for testing purposes only)
 */
private[config] class DefaultConfig
  extends ConfigUtil {


  class DefaultConfigHolderImpl
    extends ConfigHolder with ConfigLoader {

    override protected[config] def fileConf =
      ConfigFactory.empty

    override protected[config] def actorSystem =
      ActorSystem("default")

    override def confLoader = new ConfigLoaderImpl {
      override def get:Map[String, _ <: AnyRef] =
        Map()
    }
  }


  override def confHolder = new DefaultConfigHolderImpl
}