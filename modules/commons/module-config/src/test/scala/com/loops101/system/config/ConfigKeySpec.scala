package com.loops101.system.config

import com.loops101.test.spec.UnitSpec

class ConfigKeySpec
  extends UnitSpec {

  "Config Key" should {

    "generate key name" >> {
      int.toString === "internal.int"
      pub.toString === "public.pub"
    }
  }


  //~ SETUP =====================================================================================

  case object pub extends PublicKey("pub")
  case object int extends InternalKey("int")

}
