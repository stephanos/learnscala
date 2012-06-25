logLevel := Level.Info

resolvers ++= Seq(
    DefaultMavenRepository,
	"sbt-idea-repo" at "http://mpeltonen.github.com/maven",
    "typesafe repo" at "http://repo.typesafe.com/typesafe/releases/")

// plugin: Play framework
addSbtPlugin("play" % "sbt-plugin" % Option(System.getProperty("play.version")).getOrElse("2.0.1"))