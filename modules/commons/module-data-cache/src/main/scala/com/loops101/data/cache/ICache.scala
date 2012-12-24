package com.loops101.data.cache

trait ICache {

    trait ICacheImpl {

        // ==== STATUS

        def isOnline: Boolean

        def startup()

        def shutdown()


        // ==== READ

        def get[T](key: CacheKey): Option[T]

        def getAll[T](keys: List[CacheKey]): Map[String, T]


        // ==== WRITE

        def put(key: CacheKey, data: Any, exp: Int, localCopy: Boolean, async: Boolean): Option[Boolean]

        def remove(key: CacheKey, async: Boolean): Option[Boolean]


        def decr(key: CacheKey, delta: Long, initValue: Long = 0, exp: Int = 0, timeout: Long): Option[Long]

        def incr(key: CacheKey, delta: Long, initValue: Long = 0, exp: Int = 0, timeout: Long): Option[Long]
    }

    val cache: ICacheImpl
}