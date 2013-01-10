logLevel := Level.Warn


// repository: Typesafe
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "Typesafe snapshots" at  "http://repo.typesafe.com/typesafe/snapshots/"


// plugin: IntelliJ
addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.2.0")


// plugin: Play framework
addSbtPlugin("play" % "sbt-plugin" % "2.1-RC2")