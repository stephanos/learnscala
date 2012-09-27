package controllers

import java.util.Date
import play.api.Play

object MyPlay {

    val boot =
        new Date()

    lazy val build = // must ve lazy or config not found
        Play.current.configuration.getString("application.build").get
}