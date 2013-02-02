package com.loops101.system.config

import com.loops101.test.spec.MockFactorySpec

class ConfigUtilSpec
  extends MockFactorySpec[ConfigUtil] {

  "Config Util" should {

    "return String" >> {
      "public" >> {
        target.config.getString(pubstr) must beSome("abc")
      }
      "internal" >> {
        target.config.getString(intstr) must beSome("xyz")
      }
    }

    "return Int" >> {
      "public" >> {
        target.config.getInt(pubint) must beSome(1)
      }
      "internal" >> {
        target.config.getInt(intint) must beSome(42)
      }
    }

    "return Double" >> {
      "public" >> {
        target.config.getInt(pubdbl) must beSome(0.0)
      }
      "internal" >> {
        target.config.getInt(intdbl) must beSome(1.0)
      }
    }
  }


  //~ SETUP =====================================================================================

  case object pubstr extends PublicKey("pubstr")

  case object pubint extends PublicKey("pubint")

  case object pubdbl extends PublicKey("pubdbl")

  case object intstr extends InternalKey("intstr")

  case object intint extends InternalKey("intint")

  case object intdbl extends InternalKey("intdbl")

  lazy val factory =
    new DefaultConfig {
      override def confHolder = new DefaultConfigHolderImpl {
        override lazy val confLoader = new ConfigLoaderImpl {
          override def get = Map(
            pubstr.toString -> "abc",
            intstr.toString -> "xyz",

            pubint.toString -> "1",
            intint.toString -> "42",

            pubdbl.toString -> "0.0",
            intdbl.toString -> "1.0"
          )
        }
      }
    }
}
