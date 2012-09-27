package com.loops101.util

class EnvUtil extends EnvUtilBase

object EnvUtil extends EnvUtilBase


trait EnvUtilBase {

    import SystemUtil._

    // MODE
    lazy val mode: String =
        getEnvProperty("APP.MODE").getOrElse(getProperty("APP.MODE", "dev"))

    lazy val isDevelopment =
        !isStaging && !isProduction

    lazy val isStaging =
        mode == "staging"

    lazy val isProduction =
        mode == "production"

    lazy val modeCode =
        if (isStaging) "stage"
        else {
            if (isProduction) "prod" else "dev"
        }


    // ENV
    lazy val env: String =
        getEnvProperty("APP.ENV").getOrElse(getProperty("APP.ENV", "local"))

    lazy val isLocal =
        !isCloud

    lazy val isCloud =
        env == "cloud"

    // ==== CREDENTIALS

    lazy val getAmazonCredentials = {
        val access = SystemUtil.getEnvProperty("AWS_ACCESS")
        val secret = SystemUtil.getEnvProperty("AWS_SECRET")

        if (access.isDefined && secret.isDefined) Some(access.get, secret.get)
        else None
    }
}