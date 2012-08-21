//package services.data.dao
//
//import services.data.model._
//
//import com.loops101.data.mongo.dao.Query._
//import com.loops101.data.mongo.dao.CommonDAO
//import com.loops101.data.mongo.util.OIDUtil
//
//object CodeRepo extends CommonDAO{
//
//    def add(code: CodeDoc) =
//        code.save(safe = true)
//
//    def get(session: String) =
//        CodeDoc
//            .where(_.session eqs OIDUtil.unobfuscate(session))
//            .orderNaturalDesc
//            .fetch()
//}
