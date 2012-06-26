package com.loops101.util

object Opt {

    def apply(v: Int): Option[Int] =
        opt(v, -1)

    def apply(v: Int, df: Int): Option[Int] =
        opt(v, df)


    def apply(v: Long): Option[Long] =
        opt(v, -1L)

    def apply(v: Long, df: Long): Option[Long] =
        opt(v, df)


    def apply(v: String): Option[String] =
        opt(v, "")

    def apply(v: String, df: String): Option[String] =
        opt(v, df)


    private def opt[T](v: T, df: T): Option[T] =
        if (v == df) None else Option(v)
}
