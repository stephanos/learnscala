package com.loops101.util

import util.Random

class RandUtil {

    def randomBool(): Boolean =
        randomLong(0, 1) == 1

    def randomInt(max: Int = java.lang.Integer.MAX_VALUE): Int =
        new Random().nextInt(max)

    def randomLong(): Long =
        randomLong(0)

    def randomBigLong(): Long =
        randomLong(100000)

    def randomLong(min: Int): Long =
        randomLong(min, java.lang.Integer.MAX_VALUE)

    def randomLong(min: Int, max: Int): Long =
        min + scala.math.round((max - min) * new Random().nextDouble)

    def randomTextNum(len: Int): String =
        randomLong(math.pow(10, len).toInt, math.pow(10, len + 1).toInt).toString

    def randomText(len: Int): String = {
        def isSafe(c: Char) = (c != 'a' && c != 'e' && c != 'i' && c != 'o' && c != 'u' &&
            c != 'A' && c != 'E' && c != 'I' && c != 'O' && c != 'U')
        Random.alphanumeric.filter(isSafe).take(len).mkString
    }

    def randomText(min: Int, max: Int): String =
        randomText(randomLong(min, max).toInt)
}

object RandUtil extends RandUtil