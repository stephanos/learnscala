package services.data.dao

import services.data.model.UserDoc

import com.loops101.data.mongo.dao._
import com.loops101.data.mongo.dao.Query._

import com.mongodb.WriteConcern

object UserRepo extends CommonDAO {

    def findUser(name: String) =
        UserDoc
            .where(_.name eqs name)
            .get()

    def findOrCreate(d: UserDoc) = {
        val name = d.name.value
        findUser(name) match {
            case Some(_) =>
                UserDoc
                    .where(_.name eqs name)
                    .findAndModifyOpt(d.gid.value)(_.gid setTo _)
                    .findAndModifyOpt(d.email.value)(_.email setTo _)
                    .findAndModifyOpt(d.fullname.value)(_.fullname setTo _)
                    .findAndModifyOpt(d.githubToken.value)(_.githubToken setTo _)
                    .updateOne(returnNew = true).get
            case _ =>
                d.save(WriteConcern.JOURNAL_SAFE)
                d
        }
    }
}
