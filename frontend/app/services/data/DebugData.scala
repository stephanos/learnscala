//package services.data
//
//import services.data.model.UserDoc
//import com.loops101.util._
//
//object DebugData {
//
//    def reset() {
//        if (!EnvUtil.isProduction) {
//            MyDocDB.recreateDB()
//
//            UserDoc.create
//                .gid(159852).name("stephanos").password(PassUtil.encode("execfps2"))
//                .githubToken("84fcbeedc955429ba2e14105f60be8d9715f1c23").confirmed(true)
//                .save(safe = true)
//
//            simulate()
//        }
//    }
//
//    def simulate() {
//        if (!EnvUtil.isProduction) {
//
//            // TODO
//        }
//    }
//}