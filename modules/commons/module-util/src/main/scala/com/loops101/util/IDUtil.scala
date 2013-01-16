package com.loops101.util

import org.joda.time.DateTime


object IDUtil extends IDUtilImpl


trait IDUtil {
  lazy val idUtil = new IDUtilImpl
}


private[util] class IDUtilImpl {

  private val maxid = 2147483647
  private val prime = 1580030173
  private val prime_inv = 59260789


  //~ INTERFACE ==================================================================================

  /**
   * create a unique ID (only using 64 bit, completely sufficient)
   */
  def getUID: Long =
    java.util.UUID.randomUUID.getMostSignificantBits //UUID.randomUUID().toString

  /**
   * shrink an ID in order to reduce size as much as possible
   */
  def shrink(id: Long): String =
    BaseUtil.hash64(id)

  /**
   * un-Shrink an ID in order to transform it back from a reduced size
   */
  def unshrink(id: String): Long =
    BaseUtil.unhash64(id).longValue


  def urlShrink(id: Long): String =
    BaseUtil.hash30(id)

  def urlShrink(id: DateTime): String =
    urlShrink(id.getMillis)

  def urlUnshrink(id: String): Long =
    BaseUtil.unhash30(id).longValue


  /**
   * obfuscate an ID to use it in public URLs (focus here is readability not compression)
   */
  def obfuscate(id: Long): String =
    BaseUtil.hash30(((id + 1) * prime) & maxid)

  /**
   * unobfuscate an ID from a public URL
   */
  def unobfuscate(id: String): Long =
    ((BaseUtil.unhash30(id).longValue * prime_inv) & maxid) - 1

}