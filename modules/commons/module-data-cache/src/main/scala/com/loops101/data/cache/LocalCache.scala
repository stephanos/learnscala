package com.loops101.data.cache

import com.loops101.util.{LogUtil, SystemUtil}
import local.EHCache

trait LocalCache extends EHCache
    with SystemUtil with LogUtil {

    override lazy val cache = new EHCacheImpl()
}