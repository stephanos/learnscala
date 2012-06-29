package com.loops101.unit

import org.bson.types.ObjectId

import com.loops101.UnitSpec
import com.loops101.data.mongo.util.OIDUtil

class OIDUtilSpec extends UnitSpec {

    "OID Util" should {

        "write & read publishable OID" >> {
            roundtrip("497dab624ee47b3a675d2d9c") === "497dab624ee47b3a675d2d9c"
            roundtrip("4bec5869fdc212081d000000") === "4bec5869fdc212081d000000"
            roundtrip("4bec5869fdc212081d000001") !== "4bec5869fdc212081d000000"
        }
    }

    private def roundtrip(str: String) =
        OIDUtil.unobfuscate(OIDUtil.obfuscate(new ObjectId(str))).toString
}