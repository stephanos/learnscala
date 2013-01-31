package com.loops101.util

import com.loops101.util.IdUtil._
import com.loops101.test.spec.UnitSpec

class IDUtilSpec
  extends UnitSpec {

  "ID Util" should {

    "obfuscate" >> {
      unobfuscate(obfuscate(1L)) === 1L
      unobfuscate(obfuscate(1000L)) === 1000L
      unobfuscate(obfuscate(1000000L)) === 1000000L
    }

    "shrink" >> {
      unshrink(shrink(1L)) === 1L
      unshrink(shrink(1000L)) === 1000L
      unshrink(shrink(1000000L)) === 1000000L
    }

    "shrink (url safe)" >> {
      urlUnshrink(urlShrink(1L)) === 1L
      urlUnshrink(urlShrink(1000L)) === 1000L
      urlUnshrink(urlShrink(1000000L)) === 1000000L
    }
  }
}
