package com.loops101.util

import com.loops101.test.spec.UnitSpec

class PassUtilSpec
  extends UnitSpec
  with PassUtil {

  "PassUtil" should {

    "encode string" >> {
      passUtil.encode("123") must not beNull

      passUtil.encode("") must be empty

      passUtil.encode(null) must be empty
    }

    "compare raw and encoded string" >> {
      val enc = passUtil.encode("123")

      passUtil.isValid(enc, "123") === true
      passUtil.isValid(enc, "test") === false
      passUtil.isValid(enc, null) === false
      passUtil.isValid(enc, "") === false
    }
  }
}