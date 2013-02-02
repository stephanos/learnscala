package com.loops101.system.config


class MockConfigUtil
  extends DefaultConfig {

  override lazy val confHolder = new DefaultConfigHolderImpl {
    override lazy val confLoader = new ConfigLoaderImpl {
      override def get =
        Map(
          (MailHost.name) -> "host", (MailPort.name) -> "port",
          (MailUser.name) -> "user", (MailPass.name) -> "pass"
        )
    }
  }

}