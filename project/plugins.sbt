// Comment to get more information during initialization
logLevel := Level.Warn

// The repositories

resolvers ++= Seq(
	"Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
	"Spy Repository" at "http://files.couchbase.com/maven2"
)

// Use the Play sbt plugin for Play projects
addSbtPlugin("play" % "sbt-plugin" % "2.1-SNAPSHOT")