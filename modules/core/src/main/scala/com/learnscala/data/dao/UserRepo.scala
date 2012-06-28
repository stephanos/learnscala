package com.learnscala.data.dao

import com.learnscala.data.model.UserDoc

import com.loops101.data.mongo.dao._
import com.loops101.data.mongo.dao.Query._

import com.mongodb.WriteConcern

object UserRepo extends CommonDAO {

    def findUser(gid: Int) =
        UserDoc
            .where(_.gid eqs gid)
            .get()

    def findUser(name: String) =
        UserDoc
            .where(_.name eqs name)
            .get()

    def findOrCreate(d: UserDoc) =
        findUser(d.gid.value) match {
            case Some(u) =>
                u
            case _ =>
                d.save(WriteConcern.JOURNAL_SAFE)
                d
        }
}
