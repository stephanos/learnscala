package com.loops101.util

import com.loops101.util.HashUtil._
import com.loops101.test.spec.UnitSpec

class HashUtilSpec extends UnitSpec {

  "Hash Util" should {

    "calculate hash of String" >> {
      hash("test") === 32651531097669748L
      hash("abcdefghijklmnopqrstuvwxyz") === -5000401121361069959L
    }

    "calculate same hash for same String" >> {
      hash("test") === hash("test")
      hash("testA") !== hash("testB")
    }
  }
}