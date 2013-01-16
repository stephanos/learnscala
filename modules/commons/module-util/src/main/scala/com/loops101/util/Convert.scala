package com.loops101.util

import scala.reflect.ClassTag
import scala.collection.JavaConversions._
import collection.immutable.HashMap

object Convert {

    def array[A <: Object : ClassTag](set: java.util.Set[A]) = {
        val res = Array.ofDim[A](set.size)
        set.toArray(res)
        res
    }

    def map[A, B](javaMap: java.util.Map[A, B]) =
        HashMap() ++ mapAsScalaMap[A, B](javaMap)

    def addX[Y](data: List[Y]) =
        (0 to data.length).zip(data).toList
}