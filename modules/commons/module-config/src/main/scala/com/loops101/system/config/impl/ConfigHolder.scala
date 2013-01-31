package com.loops101.system.config.impl

import akka.actor.ActorSystem
import com.loops101.system.config.{ConfigKey, ConfigLoader}
import com.loops101.util.{LogUtil, SystemUtil}
import com.typesafe.config.ConfigFactory
import scala.collection.JavaConverters._

private[config] trait ConfigHolder
  extends SystemUtil
  with LogUtil {

  self: ConfigLoader =>


  class ConfigHolderImpl {

    // schedule a regular configuration reload
    /*  TODO
    import scala.concurrent.duration._
    actorSystem.scheduler.scheduleOnce(60 seconds) {
      reload()
    }(execContext)
    */


    //~ INTERNALS =================================================================================

    private val baseConf =
      ConfigFactory.parseMap {
        sysUtil.getEnvProperties.asScala.map(kv => kv._1.toLowerCase -> kv._2).asJava
      } withFallback (fileConf)

    private var conf = load

    /*
    private def reload() {
      val newConf = load()
      synchronized {
        conf = newConf
      }
    }
    */

    private def load =
      ConfigFactory.parseMap {
        confLoader.get.map(kv => kv._1.toLowerCase -> kv._2).asJava
      }.withFallback(baseConf)

    private def get[T](key: ConfigKey[_])(fn: String => T): Option[T] =
      try {
        Some(fn(key.toString))
      } catch {
        case e: Throwable =>
          log.warn("config key not found", e)
          None
      }


    //~ SHARED ====================================================================================

    protected[config] lazy val fileConf = ConfigFactory.empty

    protected[config] lazy val actorSystem = ActorSystem("default")


    //~ INTERFACE =================================================================================

    def getInt(key: ConfigKey[_]) =
      get(key)(conf.getInt(_))

    def getDouble(key: ConfigKey[_]) =
      get(key)(conf.getDouble(_))

    def getString(key: ConfigKey[_]) =
      get(key)(conf.getString(_))

  }


  lazy val confHolder = new ConfigHolderImpl

  lazy val execContext = scala.concurrent.ExecutionContext.Implicits.global
}
