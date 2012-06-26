package com.loops101.data.mongo.conf

import com.loops101.util._
import net.liftweb.mongodb._
import com.mongodb._

object DocConnection
    extends Logging {

    private lazy val mongoURI =
        SystemUtil.getEnvProperty("MONGO_URL").map(new MongoURI(_))

    private var mongo: Option[Mongo] =
        None

    //~ INTERFACE =================================================================================

    def connect(dbName: String = "test") {
        if (mongo.isEmpty) {
            mongo = Some(mongoURI match {
                case Some(uri) =>
                    // location
                    val url = uri.getHosts.get(0)
                    val host = url.substring(0, url.indexOf(":"))
                    val port = url.substring(host.length() + 1).toInt
                    log.info("connecting to remote MongoDB: {}:{}", host, port.toString)

                    // name
                    val db = uri.getDatabase
                    log.info("accessing remote MongoDB: {}", db)

                    // auth
                    val user = uri.getUsername
                    val pass = uri.getPassword.mkString
                    log.info("authenticating at remote MongoDB: {}@{}", user, pass)

                    // config [http://stackoverflow.com/questions/6520439/how-to-configure-mongodb-java-driver-mongooptions-for-production-use]
                    val opt = new MongoOptions()
                    opt.threadsAllowedToBlockForConnectionMultiplier = 1500
                    opt.autoConnectRetry = true
                    opt.connectionsPerHost = 40
                    opt.connectTimeout = 300000 // 30s
                    opt.socketTimeout = 600000 // 60s
                    opt.j = true // force journaling
                    opt.w = 1 // require minimum write safety

                    val m = new Mongo(new ServerAddress(host, port), opt)
                    MongoDB.defineDbAuth(DefaultMongoIdentifier, m, db, user, pass)
                    m

                case _ =>
                    log.info("connecting to local MongoDB")
                    val m = new Mongo("localhost", 27017)
                    MongoDB.defineDb(DefaultMongoIdentifier, m, dbName)
                    m
            })
        }
    }

    def disconnect() {
        mongo.foreach(_.close())
        MongoDB.close
        mongo = None
    }
}
