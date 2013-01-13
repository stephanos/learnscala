//package com.loops101.web.plugins
//
//import play.api.cache._
//import com.loops101.data.cache._
//import com.loops101.util.{LogUtil, SystemUtil}
//
//class MemCachePlugin(app: play.api.Application)
//    extends CachePlugin with MemCache {
//
//    override lazy val enabled =
//        !app.configuration.getString("memcacheplugin").filter(_ == "disabled").isDefined
//
//    override def onStart() {
//        _memcache.startup()
//    }
//
//    override def onStop() {
//        _memcache.shutdown()
//    }
//
//    lazy val api = new CacheAPI {
//
//        def set(key: String, value: Any, expiration: Int) =
//            _memcache.put(CacheKey(key), value, expiration)
//
//        def get(key: String) =
//            _memcache.get(CacheKey(key))
//    }
//}