package com.loops101.util

import Character._
import java.util.regex.Pattern
import scala.annotation.tailrec

class StringUtil {

    private val wordRegex = Pattern.compile("\\w+")

    /**
     * Not only trims first and last whitespaces, but also the multiple ones in-between
     */
    final def extendedTrim(str: String): String =
        str.trim().replaceAll("""\s+""", " ")

    final def growToSize(str: String, n: Int): String =
        String.format("%1$-" + n + "s", str)

    final def limit(str: String, maxlen: Int = 255, ellipsis: Option[String] = Some("..")): String =
        str.length() > maxlen match {
            case true =>
                val targetLen = maxlen - (ellipsis.getOrElse("").length)
                str.substring(0, targetLen) + ellipsis.getOrElse("")
            case false => str
        }

    @tailrec
    final def getFirstWord(str: String, r: String = ""): String = {
        if (str.length > 0) {
            val c = str.charAt(0)
            if (c != ' ')
                getFirstWord(str.substring(1), r + c)
            else
            if (r.length > 0) r else getFirstWord(str.substring(1), r)
        } else
            r
    }

    final def getBaseWords(str: String): String = {
        val m = str.split(' ')
        if (m.length > 0)
            extendedTrim((0 to m.length - 1).map {
                i =>
                    val word = m(i)
                    if (!isEnclosed(word)) {
                        val syms = nonLetters(word)
                        if (syms.length > 0) {
                            val len = word.length
                            if (syms.length == 1) {
                                if (word.charAt(len - 1) == syms(0))
                                    word.substring(0, len - 1)
                                else {
                                    if (word.endsWith("n't") || word.startsWith("non-"))
                                        word
                                    else
                                        ""
                                }
                            } else if (syms.length == 2)
                                if (word.endsWith("(s)"))
                                    word
                                else
                                    ""
                            else
                                ""
                        } else
                            word
                    } else
                        ""
            }.mkString(" "))
        else ""
    }

    final def nonLetters(s: String): Array[Char] =
        s.toCharArray.filter(!isLetter(_))

    final def isEnclosed(s: String): Boolean = {
        if (s.length >= 3)
            !isLetter(s.charAt(0)) && !isLetter(s.charAt(s.length - 1))
        else
            false
    }

    final def getHalfs(s: String): (String, String) = {
        val mid = s.length() / 2
        (s.substring(0, mid), s.substring(mid))
    }

    final def getSubstring(s: String, count: Int) = {
        val l = scala.math.min(s.length, count)
        s.substring(0, l)
    }
}

object StringUtil extends StringUtil