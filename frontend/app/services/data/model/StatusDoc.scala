package services.data.model

import com.loops101.data.mongo.model._
import com.loops101.data.mongo.dao.Query.Asc

import services.data.MyDocDB
import com.foursquare.rogue.Rogue._

import net.liftweb.mongodb.record._
import net.liftweb.record.field.IntField

class StatusDoc
    extends MongoRecord[StatusDoc] with DocMongoId[StatusDoc] {

    def meta = StatusDoc

    object exercise extends TextField(this) with MyField {
        def naming = n("exercise")
    }
    object user extends TextField(this) with MyField {
        def naming = n("user")
    }

    object fail extends IntField(this) with MyField {
        def naming = n("fail")
    }
    object skip extends IntField(this) with MyField {
        def naming = n("skip")
    }
    object success extends IntField(this) with MyField {
        def naming = n("success")
    }
    object pending extends IntField(this) with MyField {
        def naming = n("pending")
    }
    object error extends IntField(this) with MyField {
        def naming = n("error")
    }
}

object StatusDoc
    extends StatusDoc with MongoMetaRecord[StatusDoc] with MyMongoMetaRecord[StatusDoc] {

    override def mongoIdentifier = MyDocDB

    override def collectionName = name("status", "ls")

    override val maxSize = 1000

    val exerciseUserIdx = StatusDoc.index(_.exercise, Asc, _.user, Asc)
    override val mongoIndexList = List(exerciseUserIdx)

    override protected def initColl() {
        ensureIndex(idx(exerciseUserIdx), unique)
    }
}