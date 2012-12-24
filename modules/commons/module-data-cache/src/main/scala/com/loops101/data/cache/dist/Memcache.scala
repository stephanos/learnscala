package com.loops101.data.cache.dist

import com.loops101.data.cache.{ICache, CacheKey}
import com.loops101.util.LogUtil
import net.rubyeye.xmemcached.MemcachedClient

trait Memcache extends ICache {

    self: LogUtil with MemcacheConnect =>


    class MemcacheImpl extends ICacheImpl {

        //~ INTERFACE =============================================================================

        def isOnline =
            _cacheConnect.isOnline

        def startup() {
            _cacheConnect.startup()
        }

        def shutdown() {
            _cacheConnect.shutdown()
        }

        val timeout =
            MemcachedClient.DEFAULT_OP_TIMEOUT


        // ==== READ

        def get[T](key: CacheKey): Option[T] =
            try
                client.map(c => Option(c.get[T](key.toString))).getOrElse(None)
            catch {
                case e: Throwable =>
                    log.error("unable to get key '{}': {}", key, e.getMessage)
                    None
            }

        def getAll[T](keys: List[CacheKey]): Map[String, T] =
            try {
                import collection.JavaConversions._
                client.map(_.get[T](keys.map(_.toString))).getOrElse(java.util.Collections.emptyMap[String, T]()).toMap
            } catch {
                case e: Throwable =>
                    log.error("unable to get keys '{}': {}", keys.mkString(","), e.getMessage)
                    Map()
            }


        // ==== WRITE

        def put(key: CacheKey, data: Any, exp: Int, localCopy: Boolean = false, async: Boolean = true) =
            try
                if (async)
                    client.map(_.set(key.toString, exp, data))
                else {
                    client.map(_.setWithNoReply(key.toString, exp, data))
                    None
                }
            catch {
                case e: Throwable =>
                    log.error("unable to put key '{}': {}", key, e.getMessage)
                    None
            }

        def remove(key: CacheKey, async: Boolean = true) =
            try
                if (async)
                    client.map(_.delete(key.toString))
                else {
                    client.map(_.deleteWithNoReply(key.toString))
                    None
                }
            catch {
                case e: Throwable =>
                    log.error("unable to remove key '{}': {}", key, e.getMessage)
                    None
            }

        def decr(key: CacheKey, delta: Long, initValue: Long = 0, exp: Int = 0, timeout: Long = timeout) =
            incr(key, delta * -1, initValue, exp, timeout)

        def incr(key: CacheKey, delta: Long, initValue: Long = 0, exp: Int = 0, timeout: Long = timeout) =
            try
                client.map(_.incr(key.toString, delta, initValue, timeout, exp))
            catch {
                case e: Throwable =>
                    log.error("unable to incr/decr '{}': {}", key, e.getMessage)
                    None
            }


        //~ INTERNALS =============================================================================

        private def client =
            _cacheConnect.getClient

    }
}