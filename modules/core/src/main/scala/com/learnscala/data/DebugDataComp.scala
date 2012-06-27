package com.learnscala.data

import com.learnscala.data.model.UserDoc
import com.loops101.util._

object DebugData {

    def reset() {
        if (!EnvUtil.isProduction) {
            MyDocDB.recreateDB()

            UserDoc.create
                .gid(159852).name("stephanos").password(PassUtil.encode("tbsdZjqFBdxXrkt8Gm2Z7MqA"))
                .githubToken("84fcbeedc955429ba2e14105f60be8d9715f1c23")
                .save(safe = true)

            simulate()
        }
    }

    def simulate() {
        if (!EnvUtil.isProduction) {

            // TODO
        }
    }
}