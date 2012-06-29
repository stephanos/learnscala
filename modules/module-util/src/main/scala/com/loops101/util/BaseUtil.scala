package com.loops101.util

import java.math.BigInteger
import org.apache.commons.codec.binary._

class BaseUtil {

    // === Base 30 (no vowels, good for small numbers used in url)

    private val base30 = "0123456789bcdfghjkmnpqrstvwxyz"
    private val base30Len = BigInteger.valueOf(base30.length)

    def hash30(num: BigInteger): String =
        hash(num, base51, base30Len)

    def hash30(num: Long): String =
        hash(num, base30, base30Len)

    def unhash30(s: String): BigInteger =
        unhash(s, base30, base30Len)


    // === Base 51 (no vowels, good for big numbers used in url)

    private val base51 = "0123456789bcdfghjkmnpqrstvwxyzBCDFGHJKLMNPQRSTVWXYZ"
    private val base51Len = BigInteger.valueOf(base51.length)

    def hash51(num: BigInteger): String =
        hash(num, base51, base51Len)

    def hash51(num: Long): String =
        hash(num, base51, base51Len)

    def unhash51(s: String): BigInteger =
        unhash(s, base51, base51Len)


    // === Base 64 (good for simple compression)

    def hash64(num: Long): String =
        new String(Base64.encodeInteger(BigInteger.valueOf(num)))

    def unhash64(s: String): BigInteger =
        Base64.decodeInteger(s.getBytes)


    //~ INTERNALS ==================================================================================

    private def hash(num: Long, base: String, baseLen: BigInteger): String =
        hash(BigInteger.valueOf(num), base, baseLen)

    private def hash(num: BigInteger, base: String, baseLen: BigInteger): String = {
        var ret: String = ""
        var div = BigInteger.ZERO
        var tmp = num
        while (tmp.compareTo(baseLen) != -1) {
            div = tmp.divide(baseLen)
            ret = base.charAt((tmp.subtract(baseLen.multiply(div))).intValue) + ret
            tmp = div
        }
        base.charAt(tmp.intValue) + ret
    }

    private def unhash(s: String, base: String, baseLen: BigInteger): BigInteger = {
        var ret = BigInteger.ZERO
        var c: Char = 0
        var i = 0
        while (i < s.length) {
            c = s.charAt(i)
            ret = ret.add(BigInteger.valueOf(base.indexOf(c)))
            i += 1
            if (i < s.length) {
                ret = ret.multiply(baseLen)
            }
        }
        ret
    }
}

object BaseUtil extends BaseUtil