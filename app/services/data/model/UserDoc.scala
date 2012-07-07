package services.data.model

import com.loops101.data.mongo.model._
import services.data.MyDocDB

import com.foursquare.rogue.Rogue._

import net.liftweb.mongodb.record._
import net.liftweb.record.field._
import com.loops101.data.mongo.dao.Query.Asc

class UserDoc
    extends MongoRecord[UserDoc] with DocMongoId[UserDoc] {

    def meta = UserDoc

    object name extends TextField(this) with MyField {
        def naming = n("name")
    }

    object gid extends OptionalIntField(this) with MyOptField {
        def naming = n("gh_id")
    }

    object githubToken extends OptionalTextField(this) with MyOptField {
        def naming = n("gh_token")
    }

    object confirmed extends OptionalBooleanField(this) with MyOptField {
        def naming = n("confirmed")
    }

    object password extends OptionalTextField(this) with MyOptField {
        def naming = n("pass")
    }

    object email extends OptionalTextField(this) with MyOptField {
        def naming = n("email")
    }

    object fullname extends OptionalTextField(this) with MyOptField {
        def naming = n("fullname")
    }

}

object UserDoc
    extends UserDoc with MongoMetaRecord[UserDoc] with MyMongoMetaRecord[UserDoc] {

    override def mongoIdentifier = MyDocDB

    override def collectionName = name("users", "ls")

    val nameIdx = UserDoc.index(_.name, Asc)
    override val mongoIndexList = List(nameIdx)

    override protected def initColl() {
        ensureIndex(idx(nameIdx), unique)
    }
}