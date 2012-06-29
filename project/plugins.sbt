logLevel := Level.Info

// repository: Typesafe
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

// plugin: Play framework
addSbtPlugin("play" % "sbt-plugin" % Option(System.getProperty("play.version")).getOrElse("2.0.1"))

// library: HTML compressor
libraryDependencies += "com.googlecode.htmlcompressor" % "htmlcompressor" % "1.5.2"