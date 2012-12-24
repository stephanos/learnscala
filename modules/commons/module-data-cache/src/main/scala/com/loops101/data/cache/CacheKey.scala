package com.loops101.data.cache

case class CacheKey(values: Any*) {

    override def toString =
        values.mkString("_")

}
