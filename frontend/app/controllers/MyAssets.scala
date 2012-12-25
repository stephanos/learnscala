package controllers

import base.MyController
import com.loops101.web.controllers.impl.BaseAssets

object MyAssets
    extends MyController with BaseAssets {

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

    lazy val js_base = jsUrl("base")
    lazy val js_util = jsUrl("util")
    lazy val js_slide = jsUrl("slide")
    lazy val js_editor = jsUrl("editor")


    // ==== CSS

    lazy val css_pub = cssUrl("pub.css")
    lazy val css_app = cssUrl("app.css")
    lazy val css_ide = cssUrl("ide.css")
    lazy val css_slide = cssUrl("slide.css")
    lazy val css_slide_print = cssUrl("slide.print.css")
}