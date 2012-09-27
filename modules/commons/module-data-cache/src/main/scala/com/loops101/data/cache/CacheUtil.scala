package com.loops101.data.cache

import scala.collection.JavaConversions._

import com.loops101.util._
import com.loops101.data.cache.model._
import com.loops101.data.cache.conf.CacheConnection

trait CacheUtil {

    class MemCache {

        def isOnline =
            CacheUtil.isOnline

        def startup() =
            CacheUtil.startup()

        def shutdown() =
            CacheUtil.shutdown()


        def get[T](key: CacheKey) =
            CacheUtil.get[T](key)

        def getAll[T](keys: List[CacheKey]) =
            CacheUtil.getAll[T](keys)

        def put(key: CacheKey, data: Any, exp: Int, localCopy: Boolean = false) =
            CacheUtil.put(key, data, exp, localCopy)

        def decr(key: CacheKey, delta: Long, initValue: Long = 0, timeout: Long = CacheConnection.timeout, exp: Int = 0) =
            CacheUtil.decr(key, delta, initValue, timeout, exp)

        def incr(key: CacheKey, delta: Long, initValue: Long = 0, timeout: Long = CacheConnection.timeout, exp: Int = 0) =
            CacheUtil.incr(key, delta, initValue, timeout, exp)
    }

    val memcache: MemCache
}


object CacheUtil extends Logging {

    def isOnline =
        CacheConnection.isOnline

    def startup() =
        CacheConnection.startup()

    def shutdown() =
        CacheConnection.shutdown()


    // ==== READ

    def get[T](key: CacheKey): Option[T] =
        try
            getClient.map(c => Option(c.get[T](key.toString))).getOrElse(None)
        catch {
            case e =>
                log.error("unable to get key '{}': {}", key, e.getMessage)
                None
        }

    def getAll[T](keys: List[CacheKey]): Map[String, T] =
        try
            getClient.map(_.get[T](keys.map(_.toString))).getOrElse(java.util.Collections.emptyMap[String, T]()).toMap
        catch {
            case e =>
                log.error("unable to get keys '{}': {}", keys.mkString(","), e.getMessage)
                Map()
        }

    // ==== WRITE

    def put(key: CacheKey, data: Any, exp: Int, localCopy: Boolean) {
        try
            getClient.map(_.set(key.toString, exp, data))
        catch {
            case e => log.error("unable to get key '{}': {}", key, e.getMessage)
        }
    }

    def decr(key: CacheKey, delta: Long, initValue: Long = 0, timeout: Long, exp: Int = 0) =
        incr(key, delta * -1, initValue, timeout, exp)

    def incr(key: CacheKey, delta: Long, initValue: Long = 0, timeout: Long, exp: Int = 0) =
        try
            getClient.map(_.incr(key.toString, delta, initValue, timeout, exp))
        catch {
            case e =>
                log.error("unable to incr/decr '{}': {}", key, e.getMessage)
                None
        }


    //~ INTERNALS =================================================================================

    private def getClient =
        CacheConnection.getClient
}