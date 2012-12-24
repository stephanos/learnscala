package com.loops101.data.cache

import com.loops101.util.{LogUtil, SystemUtil}
import dist.{MemcacheConnect, Memcache}

trait DistCache
    extends Memcache with MemcacheConnect
    with SystemUtil with LogUtil {

    override lazy val cache = new MemcacheImpl
    override lazy val _cacheConnect = new MemcacheConnectImpl
}