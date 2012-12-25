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
//class SystemDoc
//    extends MongoRecord[SystemDoc] with DocMongoId[SystemDoc] {
//
//    def meta = SystemDoc
//
//    // global flags
//    //  - enable API
//    //  - compiler flags
//}
//
//object SystemDoc
//    extends SystemDoc with MongoMetaRecord[SystemDoc] with MyMongoMetaRecord[SystemDoc] {
//
//    override def mongoIdentifier = MyDocDB
//
//    override def collectionName = name("system", "ls")
//
//    override val mongoIndexList = List()
//
//    override protected def initColl() {
//    }
//}
