package controllers

import base.MyController
import com.loops101.util.TimeUtil
import com.loops101.web.controllers.impl.AssetsUtil

object MyAssets
  extends MyController
  with AssetsUtil
  with TimeUtil {

  def domain = "learnscala.de"

  // ==== JS

  lazy val js_libs_init =
    List(
      jsUrl("lib/init/require"),
      jsUrl("main.dev")
    )

  lazy val js_libs_pub =
    List(
      jsUrl("lib/dom/jquery"),
      jsUrl("lib/dom/vanadium")
    )

  lazy val js_base = jsUrl("base", false)
  lazy val js_util = jsUrl("util", false)
  lazy val js_slide = jsUrl("slide", false)
  lazy val js_editor = jsUrl("editor", false)


  // ==== CSS

  lazy val css_pub = cssUrl("pub.css")
  lazy val css_app = cssUrl("app.css")
  lazy val css_ide = cssUrl("ide.css")
  lazy val css_slide = cssUrl("slide.css")
  lazy val css_slide_print = cssUrl("slide.print.css")
}