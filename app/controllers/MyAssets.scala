package controllers

import base.MyController

object MyAssets
    extends MyController with BaseAssets {

    def domain = "learnscala.de"

    // ==== JS

    lazy val js_codemirror = jsUrl("/codemirror.min.js")
    lazy val js_ide = jsUrl("/ide/script.min.js")
    lazy val js_util = jsUrl("/util/script.min.js")
    lazy val js_slide = jsUrl("/slide/script.min.js")
    lazy val js_formval = jsUrl("/vanadium.min.js")
    lazy val js_modernizr = jsUrl("/modernizr.min.js")

    lazy val js_jquery: (String, String) =
        if (isCloud)
            ("http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js",
                "https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js")
        else
            (jsUrl("/jquery.js")._1, jsUrl("/jquery.js")._2)


    // ==== CSS

    lazy val css_main = cssUrl("/main.min.css")
    lazy val css_slide = cssUrl("/slide.min.css")
}