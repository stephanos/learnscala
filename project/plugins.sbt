logLevel := Level.Info

// plugin: Play framework
addSbtPlugin("play" % "sbt-plugin" % "2.0.2")

// library: HTML compressor
libraryDependencies += "com.googlecode.htmlcompressor" % "htmlcompressor" % "1.5.2"