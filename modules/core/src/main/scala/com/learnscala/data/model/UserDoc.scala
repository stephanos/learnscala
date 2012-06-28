package com.learnscala.data.model

import com.loops101.data.mongo.model._
import com.learnscala.data.MyDocDB

import com.foursquare.rogue.Rogue._

import net.liftweb.mongodb.record._
import net.liftweb.record.field.IntField
import com.loops101.data.mongo.dao.Query.Asc

class UserDoc
    extends MongoRecord[UserDoc] with DocMongoId[UserDoc] {

    def meta = UserDoc

    object name extends TextField(this) with MyField {
        def naming = n("name")
    }
    object gid extends IntField(this) with MyField {
        def naming = n("gh_id")
    }
    object githubToken extends TextField(this) with MyField {
        def naming = n("gh_token")
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

    val gidIdx = UserDoc.index(_.gid, Asc)
    val nameIdx = UserDoc.index(_.name, Asc)
    override val mongoIndexList = List(gidIdx, nameIdx)

    override protected def initColl() {
        ensureIndex(idx(gidIdx), unique)
        ensureIndex(idx(nameIdx), unique)
    }
}