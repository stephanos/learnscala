//package services.data.model
//
//import com.loops101.util.EnvUtil
//import com.loops101.data.mongo.model._
//import com.loops101.data.mongo.dao.Query.Asc
//
//import services.data.MyDocDB
//import com.foursquare.rogue.Rogue._
//
//import net.liftweb.mongodb.record._
//import net.liftweb.record.field.IntField
//import net.liftweb.mongodb.record.field.ObjectIdField
//
//class CodeDoc
//    extends MongoRecord[CodeDoc] with DocMongoId[CodeDoc] {
//
//    def meta = CodeDoc
//
//    object session extends ObjectIdField(this) with MyField {
//        def naming = n("session")
//    }
//    object source extends TextField(this) with MyField {
//        def naming = n("source")
//    }
//    object result extends IntField(this) with MyField {
//        def naming = n("result")
//    }
//    object output extends TextField(this) with MyField {
//        def naming = n("output")
//    }
//
//}
//
//object CodeDoc
//    extends CodeDoc with MongoMetaRecord[CodeDoc] with MyMongoMetaRecord[CodeDoc] {
//
//    override def mongoIdentifier = MyDocDB
//
//    override def collectionName = name("codes", "ls")
//
//    override val maxSize = if (EnvUtil.isProduction) 50 else 2
//
//    val sessionIdx = CodeDoc.index(_.session, Asc)
//    override val mongoIndexList = List(sessionIdx)
//
//    override protected def initColl() {
//        ensureIndex(idx(sessionIdx))
//    }
//}