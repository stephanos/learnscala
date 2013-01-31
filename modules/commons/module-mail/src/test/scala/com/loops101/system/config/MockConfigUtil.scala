package com.loops101.system.config

class MockConfigUtil
  extends ConfigUtil {


  override lazy val confLoader = new ConfigLoaderImpl {

    override def get =
      Map(
        (MailHost.name) -> "host", (MailPort.name) -> "port",
        (MailUser.name) -> "user", (MailPass.name) -> "pass"
      )
  }


}