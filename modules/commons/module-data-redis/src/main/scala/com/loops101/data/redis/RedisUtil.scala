package com.loops101.data.redis

import com.loops101.util.LogUtil
import com.loops101.data.redis.conf.RedisConnection

trait RedisUtil {

    class Redis {

        def isOnline =
            RedisUtil.isOnline

    }

    val redis: Redis
}

object RedisUtil extends LogUtil {

    def isOnline =
        RedisConnection.isOnline

    def startup() =
        RedisConnection.startup()

    def shutdown() =
        RedisConnection.shutdown()


    //~ INTERNALS =================================================================================

    def getClient =
        RedisConnection.getClient
}
