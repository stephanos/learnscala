package com.loops101.util


object EnvUtil extends EnvUtilImpl with SystemUtil


trait EnvUtil {

  lazy val envUtil = EnvUtil
}


private[util] class EnvUtilImpl {

  self: SystemUtil =>

  // ==== MODE

  lazy val mode: String =
    sysUtil.getEnvProperty("APP.MODE").getOrElse(sysUtil.getProperty("APP.MODE", "dev"))

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


  // ==== ENV

  lazy val env: String =
    sysUtil.getEnvProperty("APP.ENV").getOrElse(sysUtil.getProperty("APP.ENV", "local"))

  lazy val isLocal =
    !isCloud

  lazy val isCloud =
    env == "cloud"


  // ==== CREDENTIALS

  /*
  lazy val getAmazonCredentials = {
    val access = sysUtil.getEnvProperty("AWS_ACCESS")
    val secret = sysUtil.getEnvProperty("AWS_SECRET")

    if (access.isDefined && secret.isDefined) Some(access.get, secret.get)
    else None
  }
  */
}