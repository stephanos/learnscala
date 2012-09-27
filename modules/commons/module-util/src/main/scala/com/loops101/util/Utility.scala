package com.loops101.util

class Utility {

    /**
     * Takes an Option[Input] and transforms it into a Option[Output]
     */
    def rewrap[I, O](obj: Option[I], fn: I => O)(implicit m: Manifest[I]) =
        obj match {
            case None => None
            case Some(v) => Some(fn apply obj.get)
        }

    def toLong(s: String) =
        try Some(s.toLong) catch {
            case _: NumberFormatException => None
        }
}

object Utility extends Utility