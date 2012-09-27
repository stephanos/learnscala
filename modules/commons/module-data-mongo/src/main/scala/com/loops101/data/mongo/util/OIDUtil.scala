package com.loops101.data.mongo.util

import org.bson.types._
import java.math.BigInteger
import com.loops101.util.BaseUtil

class OIDUtil {

    def obfuscate(id: ObjectId): String =
        BaseUtil.hash51(new BigInteger(id.toByteArray))

    def unobfuscate(id: String): ObjectId =
        new ObjectId(BaseUtil.unhash51(id).toByteArray)
}

object OIDUtil extends OIDUtil