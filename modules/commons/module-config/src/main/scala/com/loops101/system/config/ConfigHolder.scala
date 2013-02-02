package com.loops101.system.config

import akka.actor.ActorSystem
import com.loops101.util.{LogUtil, SystemUtil}
import com.typesafe.config.{Config, ConfigFactory}
import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContext

trait ConfigHolder
  extends SystemUtil
  with LogUtil {

  self: ConfigLoader =>


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

  private val conf = load

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

  protected[config] def fileConf: Config

  protected[config] def actorSystem: ActorSystem

  final val execContext: ExecutionContext =
    ExecutionContext.global


  //~ INTERFACE =================================================================================

  def getInt(key: ConfigKey[_]) =
    get(key)(conf.getInt(_))

  def getDouble(key: ConfigKey[_]) =
    get(key)(conf.getDouble(_))

  def getString(key: ConfigKey[_]) =
    get(key)(conf.getString(_))

}
