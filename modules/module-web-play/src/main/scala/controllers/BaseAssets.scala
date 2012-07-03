package controllers

import java.util.Date

trait BaseAssets {

    self: WebUtil =>

    def domain: String

    //~ INTERFACE =================================================================================

    lazy val robots_yes = Assets.at("/public", "robots.txt")
    lazy val robots_no = Assets.at("/public", "robots.no.txt")
    lazy val robots = if (isProduction) robots_yes else robots_no

    def at(typeOf: String, file: String, ver: String = "") = {
        val path = typeOf match {
            case "js" => ("/public/javascripts", file)
            case "css" => ("/public/stylesheets", file)
            case "img" => ("/public/images", file)
            case "view" => ("/public/templates", file)
            case _ => ("/public", file)
        }
        Assets.at(path._1, path._2)
    }

    def urlsOf(path: String) = {
        val uri = "/assets/" + build + path + randomHash
        (httpDomain + uri, httpsDomain + uri)
    }

    def urlOf(path: String, secure: Boolean) = {
        val urls = urlsOf(path)
        if(secure) urls._2 else urls._1
    }

    def embed(secure: Boolean, urls: (String, String) *): String = {
        val paths: Seq[String] = urls.map(u => (if(secure) u._2 else u._1) + randomHash)
        paths.mkString("','")
    }


    //~ SHARED ====================================================================================

    protected def cssUrl(path: String) =
        urlsOf("/css" + path)

    protected def jsUrl(path: String) =
        urlsOf("/js" + path)


    //~ INTERNALS =================================================================================

    private def randomHash =
        if (isCloud) "" else "?" + new Date().getTime

    private lazy val baseDomain = if (isProduction) "static." + domain else ""
    private lazy val httpDomain = if (isProduction) "http://" + baseDomain else baseDomain
    private lazy val httpsDomain = if (isProduction) "https://" + baseDomain else baseDomain
}