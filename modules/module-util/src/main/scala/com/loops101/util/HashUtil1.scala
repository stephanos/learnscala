//package com.crashnote.util
//
//import java.math.BigInteger
//
///**
// * Rabin Fingerprint implementation using a lookup table.
// * After construction the object is immutable and can savely used in multiple threads.
// */
//object HashUtil {
//
//    val MSD_SET: Long = 1L << 63
//
//    private def createDefaultRabin() =
//        new Hash(new BigInteger("13827942727904890243").longValue())
//
//    def hash(s: String) =
//        createDefaultRabin().hash(s.toCharArray)
//}
//
///**
// * Rabin fingerprint class
// */
//class Hash(val polynom: Long) {
//
//    private val T = createModTable(polynom)
//
//    /**
//     * Calculates the module table for a given polynom
//     */
//    def createModTable(polynom: Long): Array[Long] = {
//        val T = new Array[Long](256)
//        val T1 = PolynomUtil.mod(0, HashUtil.MSD_SET, polynom)
//
//        for (i <- 0 until 256) {
//            val v = PolynomUtil.modmult(i, T1, polynom)
//            val w = (i.toLong) << 63
//            val t = v | w
//            T(i.toInt) = t
//        }
//        T
//    }
//
//    /**
//     * Appends the byte in data to the fingerprint.
//     * @param fingerprint old fingerprint
//     * @param data data that should be in the range 0 <= data <= 255. It represents a single byte.
//     * The "byte" type isn't used because byte is signed in Java (see Java Puzzlers Book).
//     *
//     * @return
//     */
//    def append(fingerprint: Long, data: Int): Long = {
//        val shifted = (fingerprint >> 55).toInt
//        ((fingerprint << 8) | data) ^ T(shifted)
//    }
//
//    def hash(chars: Array[Char]) = {
//        var res = 0L
//        chars.foreach {
//            c => res = append(res, c.toInt)
//        }
//        res
//    }
//}
//
///**
// * Utility class for Polynom operations on a 64-bit integer.
// * In the C++ version, I used the polynom operations provided by cryptopp
// *
// * References: Donald Knuth, The Art of Computer Programming, Volume 2
// */
//object PolynomUtil {
//
//    private def mult(x: Long, y: Long): (Long, Long) = {
//        var hi = 0L
//        var lo = 0L
//        if (testBit(x, 0)) {
//            lo = y
//        }
//        for (i <- 1 until 64) {
//            if (testBit(x, i)) {
//                lo ^= y << i
//                hi ^= y >> (64 - i)
//            }
//        }
//        (hi, lo)
//    }
//
//    def mod(hi: Long, lo: Long, p: Long): Long = {
//        var hi_ = hi
//        var lo_ = lo
//        if (testBit(hi_, 63)) {
//            hi_ = hi_ ^ p;
//        }
//        for (i <- 62 to 0 by -1) {
//            if (testBit(hi_, i)) {
//                hi_ ^= p >> (63 - i)
//                lo_ ^= p << (i + 1)
//                0
//            }
//        }
//        if (testBit(lo_, 63)) {
//            lo_ ^= p;
//        }
//        lo_;
//    }
//
//    def modmult(x: Long, y: Long, p: Long): Long = {
//        val (hi, lo) = mult(x, y)
//        mod(hi, lo, p)
//    }
//
//    private def testBit(x: Long, b: Int): Boolean = (x & (1L << b)) != 0
//}