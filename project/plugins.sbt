logLevel := Level.Warn


// repository: Typesafe
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "Typesafe snapshots" at  "http://repo.typesafe.com/typesafe/snapshots/"


// plugin: IntelliJ
addSbtPlugin("com.github.mpeltonen" %% "sbt-idea" % "1.1.0-M2-TYPESAFE")


// plugin: Play framework
addSbtPlugin("play" % "sbt-plugin" % "2.1-RC1")


// library: HTML compressor
//libraryDependencies += "com.googlecode.htmlcompressor" % "htmlcompressor" % "1.5.2"