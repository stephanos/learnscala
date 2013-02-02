package system

import com.loops101.system.config._

trait MyConfig
  extends ConfigUtil {

  // access singleton instance - must be 'lazy' or app not launched yet
  override lazy val confHolder =
    MyConfig.confHolder

  // make execution context implicitly available
  implicit lazy val execContext =
    confHolder.execContext
}


private[system] object MyConfig {

  val confHolder =
    new {

      // use Play's file configuration
      val fileConf =
        play.api.Play.current.configuration.underlying

    } with ConfigHolder with ConfigLoader {

      // use Play's actor system
      def actorSystem =
        play.libs.Akka.system

      def confLoader = new ConfigLoaderImpl {
        def get = Map()
      }
    }
}