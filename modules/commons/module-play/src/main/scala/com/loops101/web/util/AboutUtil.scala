package com.loops101.web.util

import com.loops101.system.config.ConfigUtil
import org.joda.time.DateTime

trait AboutUtil {

  self: ConfigUtil =>


  // when did the app start?
  val boot =
    AboutUtil.boot


  // what's the version of the app?
  def build: String

}

object AboutUtil {

  val boot =
    new DateTime()
}