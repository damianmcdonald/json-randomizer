/*
 * Copyright 2015 Damian McDonald
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

name          := "json-randomizer"

version       := "0.1"

scalaVersion  := "2.11.6"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

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

publishTo <<= version { v: String =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

organization := "com.github.damianmcdonald"

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { x => false }

pomExtra := (
  <url>https://github.com/damianmcdonald/json-randomizer</url>
    <licenses>
      <license>
        <name>Apache 2</name>
        <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
      </license>
    </licenses>
    <scm>
      <connection>scm:git:github.com/damianmcdonald/json-randomizer</connection>
      <developerConnection>scm:git:git@github.com:damianmcdonald/json-randomizer</developerConnection>
      <url>https://github.com/damianmcdonald/json-randomizer</url>
    </scm>
    <developers>
      <developer>
        <id>damianmcdonald</id>
        <name>Damian McDonald</name>
        <url>https://github.com/damianmcdonald</url>
      </developer>
    </developers>
)

scalariformSettings