logLevel := Level.Warn

// repository: Typesafe
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

// plugin: Play framework
addSbtPlugin("play" % "sbt-plugin" % "2.0.2")

// library: HTML compressor
libraryDependencies += "com.googlecode.htmlcompressor" % "htmlcompressor" % "1.5.2"

// IntelliJ
resolvers += "sbt-idea-repo" at "http://mpeltonen.github.com/maven"

addSbtPlugin("com.github.mpeltonen" %% "sbt-idea" % "1.0.0")