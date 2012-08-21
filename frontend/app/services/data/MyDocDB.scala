package services.data

import net.liftweb.mongodb._
import com.loops101.data.mongo.conf.DocDatabase
import services.data.model._

object MyDocDB
    extends DocDatabase with MongoIdentifier {

    val dbName =
        "ls-dev"

    def jndiName =
        DefaultMongoIdentifier.jndiName

    lazy val docs =
        List(UserDoc, StatusDoc, SystemDoc)
}