package com.loops101.data.mongo.dao

import com.loops101.util._
import net.liftweb.mongodb.record.MongoRecord
import com.mongodb.BasicDBList

trait CommonDAO
    extends Logging {

    protected def listHead[T](list: List[T]): Option[T] =
        list match {
            case l: BasicDBList =>
                l.size match {
                    case 0 => None
                    case n => Some(l.get(0).asInstanceOf[T])
                }
            case l =>
                l.headOption
        }

    protected def fastSave[D <: MongoRecord[D]](obj: D): D =
        save(obj, safe = false)

    protected def save[D <: MongoRecord[D]](obj: D, safe: Boolean = true): D = {
        log.debug("[{}.save({})]", obj.meta.collectionName, obj.asDBObject)
        obj.save(safe)
    }

    def success_?(fn: => Unit): Boolean = {
        try {
            fn
            true
        } catch {
            case e: Exception =>
                log.error(e, "mongo error")
                false
        }
    }
}