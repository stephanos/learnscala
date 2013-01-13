package com.loops101.web.util

import play.api.Play
import java.util.Date
import com.loops101.util.{EnvUtil, SystemUtil}

trait WebUtil
  extends EnvUtil with SystemUtil {

    protected val boot =
        new Date()

    lazy val build =  // must be lazy or config not found
        Play.current.configuration.getString("application.build").get
}