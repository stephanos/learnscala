package com.loops101.data.cache.local

import com.loops101.util.LogUtil
import com.loops101.data.cache.{CacheKey, ICache}

trait EHCache extends ICache {

    self: LogUtil =>


    class EHCacheImpl extends ICacheImpl {

        import net.sf.ehcache._


        //~ INTERFACE =============================================================================

        def isOnline =
            true

        def startup() {
            client
        }

        def shutdown() {
            client.shutdown()
        }


        // ==== READ

        def get[T](key: CacheKey): Option[T] =
            try
                Option(cache.get(key)).map(_.getObjectValue.asInstanceOf[T])
            catch {
                case e: Throwable =>
                    log.error("unable to get key '{}': {}", key.toString, e.getMessage)
                    None
            }

        def getAll[T](keys: List[CacheKey]): Map[String, T] =
            try {
                import collection.JavaConversions._
                cache.getAll(keys.map(_.toString)).map(t => (t._1.toString, t._2.getObjectValue.asInstanceOf[T])).toMap
            } catch {
                case e: Throwable =>
                    log.error("unable to get keys '{}': {}", keys.mkString(","), e.getMessage)
                    Map()
            }


        // ==== WRITE

        def put(key: CacheKey, data: Any, exp: Int, localCopy: Boolean = false, async: Boolean = true) = {
            val element = new Element(key, data)
            if (exp == 0) element.setEternal(true)
            element.setTimeToLive(exp)
            cache.put(element)
            Some(true)
        }

        def remove(key: CacheKey, async: Boolean = true) =
            Some(cache.remove(key.toString))

        def decr(key: CacheKey, delta: Long, initValue: Long = 0, exp: Int = 0, timeout: Long = 0) =
            sys.error("NOT IMPLEMENTED")

        def incr(key: CacheKey, delta: Long, initValue: Long = 0, exp: Int = 0, timeout: Long = 0) =
            sys.error("NOT IMPLEMENTED")


        //~ INTERNALS =============================================================================

        private var _manager: CacheManager = _

        private def client: CacheManager = {
            _manager = CacheManager.create()
            _manager
        }

        private def cache: Cache = {
            client.addCache(_cacheName)
            client.getCache(_cacheName)
        }
    }

    val _cacheName: String = "default"
}