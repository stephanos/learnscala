//package services.data.model
//
//import com.loops101.data.mongo.model._
//import services.data.MyDocDB
//
//import com.foursquare.rogue.Rogue._
//
//import net.liftweb.mongodb.record._
//import com.loops101.data.mongo.dao.Query.Asc
//
//class SessionDoc
//    extends MongoRecord[SessionDoc] with DocMongoId[SessionDoc] {
//
//    def meta = SessionDoc
//
//    object name extends TextField(this) with MyField {
//        def naming = n("name")
//    }
//
//}
//
//object SessionDoc
//    extends SessionDoc with MongoMetaRecord[SessionDoc] with MyMongoMetaRecord[SessionDoc] {
//
//    override def mongoIdentifier = MyDocDB
//
//    override def collectionName = name("sessions", "ls")
//
//    val nameIdx = SessionDoc.index(_.name, Asc)
//    override val mongoIndexList = List(nameIdx)
//
//    override protected def initColl() {
//        ensureIndex(idx(nameIdx), unique)
//    }
//}
