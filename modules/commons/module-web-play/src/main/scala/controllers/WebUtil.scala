package controllers

import com.loops101.util.EnvUtilBase

trait WebUtil extends EnvUtilBase {

    protected val boot =
        MyPlay.boot

    lazy val build =  // must ve lazy or config not found
        MyPlay.build
}