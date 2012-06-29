package com.loops101.util

class JavaConverter {

    def array[A <: Object](set: java.util.Set[A])(implicit m: ClassManifest[A]) = {
        val res = Array.ofDim[A](set.size)
        set.toArray(res)
        res
    }

    implicit def toInt(in: java.lang.Integer) = in match {
        case null => -1
        case _ => in.intValue()
    }

    def opt[T](option: Option[T]) = option match {
        case None => null
        case Some(v) => v
    }
}

object JavaConverter extends JavaConverter