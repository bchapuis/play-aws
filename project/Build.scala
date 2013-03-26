import sbt._
import Keys._

object ApplicationBuild extends Build {

    val appName         = "play-aws"
    val appVersion      = "0.1"

    val appDependencies = Seq(
      "com.amazonaws" % "aws-java-sdk" % "1.4.1",
      "spy" % "spymemcached" % "2.8.1"
    )

    val main = play.Project(appName, appVersion, appDependencies).settings(
      resolvers += "Spy Repository" at "http://files.couchbase.com/maven2/"
    )

}