package services.data.model

import com.loops101.data.mongo.model._
import com.loops101.data.mongo.dao.Query.Asc

import services.data.MyDocDB
import com.foursquare.rogue.Rogue._

import net.liftweb.mongodb.record._
import field.MongoListField

class StatusDoc
    extends MongoRecord[StatusDoc] with DocMongoId[StatusDoc] {

    def meta = StatusDoc

    object exercise extends TextField(this) with MyField {
        def naming = n("exercise")
    }
    object user extends TextField(this) with MyField {
        def naming = n("user")
    }
    object results extends MongoListField[StatusDoc, Int](this) with MyField {
        def naming = n("results")
    }
}

object StatusDoc
    extends StatusDoc with MongoMetaRecord[StatusDoc] with MyMongoMetaRecord[StatusDoc] {

    override def mongoIdentifier = MyDocDB

    override def collectionName = name("status", "ls")

    override val maxSize = 100

    val exerciseUserIdx = StatusDoc.index(_.exercise, Asc, _.user, Asc)
    override val mongoIndexList = List(exerciseUserIdx)

    override protected def initColl() {
        ensureIndex(idx(exerciseUserIdx), unique)
    }
}