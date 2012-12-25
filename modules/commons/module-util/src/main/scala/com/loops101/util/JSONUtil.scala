//package com.loops101.util
//
//import net.liftweb.json
//import net.liftweb.json._
//import net.liftweb.json.JsonAST.JValue
//import net.liftweb.json.Serialization._
//
//class JSONUtil {
//
//    private val defaultFormat = Serialization.formats(NoTypeHints)
//
//
//    // === OBJ -> JSON
//
//    def out[T <: AnyRef : Manifest](obj: T, f: Formats = defaultFormat): String = {
//        implicit val formats = f
//        write[T](obj)
//    }
//
//    def cout[T <: AnyRef : Manifest](obj: T, f: Formats = defaultFormat): Array[Byte] = // compressed
//        CompressUtil.fastCompress(out[T](obj, f))
//
//    def toString(js: JValue): String =
//        compact(render(js))
//
//    def compress(js: JValue): Array[Byte] =
//        CompressUtil.fastCompress(toString(js))
//
//
//    // === JSON -> OBJ
//
//    def in[T: Manifest](json: String, f: Formats = defaultFormat): T = {
//        implicit val formats = f
//        read[T](json)
//    }
//
//    def cin[T: Manifest](bytes: Array[Byte], f: Formats = defaultFormat): T = // compressed
//        in[T](new String(CompressUtil.fastUncompress(bytes)), f)
//
//    def extract[T: Manifest](json: JValue, f: Formats = defaultFormat): Option[T] = {
//        implicit val format = f
//        Some(json.extract[T])
//    }
//
//    def parse(s: String): JValue =
//        json.parse(s)
//
//    def parse(b: Array[Byte]): JValue =
//        parse(new String(CompressUtil.fastUncompress(b)))
//}
//
//object JSONUtil extends JSONUtil