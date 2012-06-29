package plugins

import play.api.cache._
import com.loops101.data.cache.CacheUtil
import com.loops101.data.cache.model.CacheKey

class MemCachePlugin(app: play.api.Application)
    extends CachePlugin with CacheUtil {

    override lazy val enabled = {
        !app.configuration.getString("memcacheplugin").filter(_ == "disabled").isDefined
    }

    override def onStart() {
        memcache.startup()
    }

    override def onStop() {
        memcache.shutdown()
    }


    lazy val api = new CacheAPI {

        def set(key: String, value: Any, expiration: Int) =
            memcache.put(CacheKey(key), value, expiration)

        def get(key: String) =
            memcache.get(CacheKey(key))

    }

    lazy val memcache = new MemCache
}