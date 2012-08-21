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
            .modify(_.fail setTo status.fail.is)
            .modify(_.pending setTo status.pending.is)
            .modify(_.error setTo status.error.is)
            .modify(_.skip setTo status.skip.is)
            .modify(_.success setTo status.success.is)
            .upsertOne(WriteConcern.SAFE)
    }

    def read (id: String): List[StatusDoc] =
        StatusDoc
            .where(_.exercise eqs id)
            .fetch()
}
