package com.loops101.system.config

import com.loops101.system.config.impl.ConfigHolder
import com.loops101.test.spec.MockFactorySpec
import com.typesafe.config.ConfigFactory

class ConfigHolderSpec
  extends MockFactorySpec[ConfigHolder] {

  "Config Holder" should {

    "provide config" >> {
      val h = target.confHolder

      h.getInt(int) must beSome(1)
      h.getString(str) must beSome("load")

      h.getString(non) must beNone
    }
  }


  //~ SETUP =====================================================================================

  case object str extends PublicKey[String]("STRING")
  case object int extends InternalKey[Int]("INT")
  case object non extends PublicKey[Double]("")

  lazy val factory =
    new ConfigHolder with ConfigLoader {

      override lazy val confHolder = new ConfigHolderImpl {
        override lazy val fileConf = {
          import scala.collection.JavaConverters._
          ConfigFactory.parseMap(Map(
            str.toString -> "file"
          ).asJava)
        }
      }

      override lazy val confLoader = new ConfigLoaderImpl {
        override def get = Map(
          str.toString -> "load",
          int.toString -> "1"
        )
      }
    }
}
