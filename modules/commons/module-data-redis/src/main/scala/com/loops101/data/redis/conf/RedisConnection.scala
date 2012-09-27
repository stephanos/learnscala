package com.loops101.data.redis.conf

import com.loops101.util.Logging
import com.loops101.util.SystemUtil
import redis.clients.jedis._

// see https://github.com/xetorthio/jedis/wiki/Getting-started
object RedisConnection extends Logging {

    private var pool: JedisPool =
        null


    //~ INTERFACE =================================================================================

    def isOnline =
        connect().isDefined

    def startup() {
        getClient
    }

    def shutdown() {
        getPool.map(_.destroy())
        pool = null
    }

    def getClient: Option[Jedis] =
        getPool.map(_.getResource)


    //~ INTERNALS =================================================================================

    private def getPool: Option[JedisPool] =
        pool match {
            case null => connect()
            case p => Some(p)
        }

    private def connect(): Option[JedisPool] =
        try {
            // read config
            val url: String = SystemUtil.getEnvProperty("REDISTOGO_URL")
                .getOrElse(SystemUtil.getEnvProperty("REDIS_URL").getOrElse("localhost"))

            // build client
            log.info("connecting to redis: {}", url)
            pool = new JedisPool(new JedisPoolConfig(), url)
            Some(pool)
        } catch {
            case e => None
        }
}
