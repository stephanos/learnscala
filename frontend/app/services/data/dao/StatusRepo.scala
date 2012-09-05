package services.data.dao

import services.data.model._

import com.loops101.data.mongo.dao._
import com.loops101.data.mongo.dao.Query._

import com.mongodb.WriteConcern

object StatusRepo extends CommonDAO {

    def write (status: StatusDoc) {
        StatusDoc
            .where(_.exercise eqs status.exercise.is)
            .and(_.user eqs status.user.is)
            .modify(_.results setTo status.results.is)
            .upsertOne(WriteConcern.SAFE)
    }

    def read (id: String): List[StatusDoc] =
        StatusDoc
            .where(_.exercise eqs id)
            .fetch()
}
