package com.loops101.data.cache.model

case class CacheKey(vals: Any*) {

    override def toString =
        vals.mkString("_")
}
