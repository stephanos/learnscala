package com.loops101.model.dto

import org.joda.time.DateTime

trait ILogMsg {

    var msg: String
    var level: String
    var nlevel: Int
    var time: DateTime

    var traces: Array[Byte] = Array()

    var logger: Option[String]
    var thread: Option[String]

    var serverIP: Option[String]
    var serverName: Option[String]
    var serverType: Option[String]

    var requestIP: Option[String]
    var requestURL: Option[String]

    var callerFile: Option[String]
    var callerClass: Option[String]
    var callerMethod: Option[String]
    var callerLine: Option[Int]
}