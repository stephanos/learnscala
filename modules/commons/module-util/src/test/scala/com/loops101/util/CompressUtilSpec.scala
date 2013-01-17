package com.loops101.util

import com.loops101.util.CompressUtil._
import com.loops101.test.spec.UnitSpec

class CompressUtilSpec extends UnitSpec {

  "Compress Util" should {

    val data = "1234567890!%&/()=,.-#+*'_:;"

    "do snappy compression" >> {
      val c = fastCompress(data)
      //log.debug("snapped: {} [{}]", new String(c), c.length)
      val s = new String(fastUncompress(c))
      s === data
    }

    "know zip compression" >> {
      "recognize" >> {
        isZipped(data) === false
        isZipped(zip(data)) === true
      }
      "do " >> {
        val c = zip(data)
        //log.debug("zipped: {} [{}]", new String(c), c.length)
        val s = new String(unzip(c))
        s === data
      }
    }

    "know deflate compression" >> {
      "recognize" >> {
        isDeflated(data) === false
        //isDeflated(deflate(data)) === true TODO?
      }
      "do " >> {
        val c = deflate(data)
        //log.debug("deflated: {} [{}]", new String(c), c.length)
        val s = new String(inflate(c))
        s === data
      }
    }
  }
}