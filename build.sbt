organization  := "jsonrandomizer"

version       := "0.1"

scalaVersion  := "2.11.6"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

resolvers ++= Seq(
  "ivy releases"          at  "http://repo.typesafe.com/typesafe/ivy-releases/, [organization]/[module]/(scala_[scalaVersion]/)(sbt_[sbtVersion]/)[revision]/[type]s/[artifact](-[classifier]).[ext]",
  "maven releases"        at  "http://repo1.maven.org/maven2/",
  "scalasbt releases"     at  "http://scalasbt.artifactoryonline.com/scalasbt/repo/, [organization]/[module]/scala_[scalaVersion]/sbt_[sbtVersion]/[revision]/[type]s/[artifact].[ext]",
  "bintray releases"      at  "http://dl.bintray.com/scalaz/releases/",
  "sonatype releases"     at  "http://oss.sonatype.org/content/repositories/releases",
  "typesafe releases" 	  at  "http://dl.bintray.com/typesafe/maven-releases/",
  "scala sbt"             at  "http://repo.scala-sbt.org/scalasbt/sbt-plugin-releases"
)

libraryDependencies ++= {
  Seq(
    "net.liftweb"                 %   "lift-json_2.11"          % "2.6.2",
	"io.spray"                    %%  "spray-client"            % "1.3.2",
	"io.spray"                    %%  "spray-can"               % "1.3.2",
	"com.typesafe.akka"           %%  "akka-actor"              % "2.3.9",
	"com.typesafe.akka"           %%  "akka-slf4j"              % "2.3.9",
    "ch.qos.logback"              %   "logback-classic"         % "1.0.7",
    "org.scalatest"       	      %%  "scalatest"             	% "2.2.4"  % "test"
  )
}

scalariformSettings