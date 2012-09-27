//package com.crashnote.util
//
//import java.util.zip.CRC32
//
//object HashUtil {
//
//    def hString(data: String): Long =
//        hBytes(data.getBytes)
//
//    def hBytes(data: Array[Byte]): Long = {
//        val checksum = new CRC32()
//        checksum.update(data, 0, data.length)
//        checksum.getValue
//    }
//}