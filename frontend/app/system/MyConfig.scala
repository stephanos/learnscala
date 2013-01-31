package system

import com.loops101.system.config.ConfigUtil

// TODO: make singleton ???
trait MyConfig
  extends ConfigUtil {


  class MyConfigHolderImpl extends ConfigHolderImpl {

    // override: use Play's actor system
    override lazy val actorSystem =
      play.libs.Akka.system

    // override: use Play's file configuration
    override lazy val fileConf =
      play.api.Play.current.configuration.underlying
  }


  override lazy val confHolder = new MyConfigHolderImpl
}