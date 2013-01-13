package com.loops101.util

import java.io._
import java.util.zip._
import org.xerial.snappy.Snappy
import org.apache.commons.io.IOUtils

class CompressUtil
        extends IGZip with ISnappy with IDeflate with StreamCompress

object CompressUtil
    extends CompressUtil

protected trait IDeflate {

    self: StreamCompress =>

    // === RECOGNIZE

    def isDeflated(str: String, charset: String = "UTF-8"): Boolean =
        isDeflated(str.getBytes(charset))

    def isDeflated(data: Array[Byte]): Boolean =
        false

    // === COMPRESS

    def deflate(data: String, charset: String = "UTF-8"): Array[Byte] =
        deflate(data.getBytes(charset))

    def deflate(data: Array[Byte]): Array[Byte] =
        _comp(data, (bs: ByteArrayOutputStream) => new DeflaterOutputStream(bs, new Deflater()))

    // === UNCOMPRESS

    def inflate(data: Array[Byte]): Array[Byte] =
        _uncomp(data, (bs: ByteArrayInputStream) => new InflaterInputStream(bs, new Inflater()))

    def inflate(data: String, charset: String = "UTF-8"): String =
        new String(inflate(data.getBytes(charset)), charset)
}

protected trait IGZip {

    self: StreamCompress =>

    // === RECOGNIZE

    def isZipped(str: String, charset: String = "UTF-8"): Boolean =
        isZipped(str.getBytes(charset))

    def isZipped(data: Array[Byte]): Boolean =
        data != null && data.length >= 2 && ((data(0) & 0xff) | ((data(1) << 8) & 0xff00)) == GZIPInputStream.GZIP_MAGIC

    // === COMPRESS

    def zip(data: String, charset: String = "UTF-8"): Array[Byte] =
        zip(data.getBytes(charset))

    def zip(data: Array[Byte]): Array[Byte] =
        _comp(data, (bs: ByteArrayOutputStream) => new GZIPOutputStream(bs))

    // === UNCOMPRESS

    def unzip(data: Array[Byte]): Array[Byte] =
        _uncomp(data, (bs: ByteArrayInputStream) => new GZIPInputStream(bs))

    def unzip(data: String, charset: String = "UTF-8"): String =
        new String(unzip(data.getBytes(charset)), charset)
}

protected trait ISnappy {

    // === COMPRESS

    def fastCompress(in: String, charset: String = "UTF-8"): Array[Byte] =
        fastCompress(in.getBytes(charset))

    def fastCompress(arr: Array[Byte]): Array[Byte] =
        Snappy.compress(arr)

    // === UNCOMPRESS

    def fastUncompress(in: String, charset: String = "UTF-8"): Array[Byte] =
        fastUncompress(in.getBytes(charset))

    def fastUncompress(in: Array[Byte]): Array[Byte] =
        Snappy.uncompress(in)

}

protected trait StreamCompress {

    protected def _comp(data: Array[Byte], fn: ByteArrayOutputStream => DeflaterOutputStream): Array[Byte] = {
        val out = new ByteArrayOutputStream
        val compressor = fn(out)
        IOUtils.write(data, compressor)
        IOUtils.closeQuietly(compressor)
        out.toByteArray
    }

    protected def _uncomp(data: Array[Byte], fn: ByteArrayInputStream => InflaterInputStream): Array[Byte] = {
        val out = new ByteArrayOutputStream()
        val uncompressor = fn(new ByteArrayInputStream(data))
        IOUtils.copy(uncompressor, out)
        IOUtils.closeQuietly(uncompressor)
        out.toByteArray
    }
}