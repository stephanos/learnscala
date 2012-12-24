package com.loops101.web.controllers.impl

import java.util.Date
import controllers.Assets
import com.loops101.web.util.WebUtil

trait BaseAssets {

    self: WebUtil =>

    def domain: String

    //~ INTERFACE =================================================================================

    lazy val robots_yes = Assets.at("/public", "robots.txt")
    lazy val robots_no = Assets.at("/public", "robots.no.txt")
    lazy val robots = if (isProduction) robots_yes else robots_no

    def at(typeOf: String, file: String, ver: String = "") = {
        val path = typeOf match {
            case "js" => ("/public/scripts", file)
            case "css" => ("/public/styles", file)
            case "img" => ("/public/images", file)
            case _ => ("/public", file)
        }
        Assets.at(path._1, path._2)
    }

    /*
    def urlsOf(path: String, hash: Boolean = true) = {
        val uri = "/assets/" + build + path + (if(hash) randomHash else "")
        (httpDomain + uri, httpsDomain + uri)
    }

    def urlOf(path: String, secure: Boolean) = {
        val urls = urlsOf(path)
        if(secure) urls._2 else urls._1
    }
    */

    lazy val assetBase =
        httpsDomain + "/assets/" + build

    def urlOf(file: String, folder: String, full: Boolean = false) =
        if (isDevelopment && !full)
            file
        else
            assetBase + "/" + folder + "/" + file + hash

    def embed(url: String*): String =
        url.mkString("','")


    //~ SHARED ====================================================================================

    protected def cssUrl(file: String) =
        urlOf(file, "css", true)

    protected def jsUrl(file: String, full: Boolean = false) =
        urlOf(file + jsExt, "js", full)


    //~ INTERNALS =================================================================================

    private def hash =
        if (isCloud) "" else "?" + new Date().getTime

    private def jsExt =
        ".js" // if (isCloud) ".min.js" else ".js"

    private lazy val baseDomain = if (isProduction) "static." + domain else ""
    //private lazy val httpDomain = if (isProduction) "http://" + baseDomain else baseDomain
    private lazy val httpsDomain = if (isProduction) "https://" + baseDomain else baseDomain
}
