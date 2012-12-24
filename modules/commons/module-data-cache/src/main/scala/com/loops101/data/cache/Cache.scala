//package com.loops101.data.cache
//
//import com.loops101.util.{LogUtil, SystemUtil}
//import com.loops101.data.cache.dist.impl.{Memcache, MemcacheConnect}
//
//trait Cache {
//
//    self: Memcache with MemcacheConnect =>
//
//    class CacheImpl {
//
//        def isOnline =
//            _memcache.isOnline
//
//        def startup() =
//            _memcache.startup()
//
//        def shutdown() =
//            _memcache.shutdown()
//
//
//        def get[T](key: CacheKey) =
//            _memcache.get[T](key)
//
//        def getAll[T](keys: List[CacheKey]) =
//            _memcache.getAll[T](keys)
//
//        def put(key: CacheKey, data: Any, exp: Int, localCopy: Boolean = false) =
//            _memcache.put(key, data, exp, localCopy)
//
//        def decr(key: CacheKey, delta: Long, initValue: Long = 0, exp: Int = 0, timeout: Long = _memcache.timeout) =
//            _memcache.decr(key, delta, initValue, exp, timeout)
//
//        def incr(key: CacheKey, delta: Long, initValue: Long = 0, exp: Int = 0, timeout: Long = _memcache.timeout) =
//            _memcache.incr(key, delta, initValue, exp, timeout)
//    }
//
//    val cache: CacheImpl
//}