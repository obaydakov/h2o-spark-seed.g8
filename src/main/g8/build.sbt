import sbt._

import com.typesafe.sbt.packager.Keys._

import sbt.Keys._

import com.typesafe.sbt.SbtNativePackager._

import com.typesafe.sbteclipse.plugin.EclipsePlugin.EclipseKeys
import com.typesafe.sbteclipse.plugin.EclipsePlugin.EclipseCreateSrc

EclipseKeys.createSrc in ThisBuild := EclipseCreateSrc.Default + EclipseCreateSrc.Resource

EclipseKeys.withSource := true

scalaVersion := "2.11.8"

scalacOptions += "-target:jvm-1.8"

javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint:deprecation")

val packageVersion = "1.0.0"

version := packageVersion

assemblySettings

name := "app"

organization := "com.yarenty.h2o"


resolvers in ThisBuild ++= Seq(
  "Sonatype Releases" at "http://oss.sonatype.org/content/repositories/releases",
  "Apache Snapshot Repository" at "http://repository.apache.org/content/groups/snapshots",
  "TypeSafe Repository" at "http://typesafe.artifactoryonline.com/typesafe/repo",
  "Typesafe Snapshots" at "http://repo.typesafe.com/typesafe/snapshots/",
  "spray" at "http://repo.spray.io/",
  "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases",
  "Twitter Maven Repo" at "http://maven.twttr.com",
  "Confluent Repo" at "http://packages.confluent.io/maven/"
)

resolvers in ThisBuild += Resolver.sonatypeRepo("public")

resolvers in ThisBuild += Resolver.bintrayRepo("cakesolutions", "maven")

resolvers in ThisBuild += Resolver.mavenLocal




libraryDependencies ++= {
  Seq(
    "ch.qos.logback" % "logback-classic" % "1.1.2",

    "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
    "org.scalaz.stream" % "scalaz-stream_2.11" % "0.7a",

     "com.github.wookietreiber" % "scala-chart_2.11" % "0.5.1",
    "com.itextpdf" % "itextpdf" % "5.5.10",
    "org.scala-lang.modules" % "scala-swing_2.11" % "2.0.0",


  "ai.h2o" % "sparkling-water-core_2.11" % "2.1.8",
    "ai.h2o" % "sparkling-water-ml_2.11" % "2.1.8"
  ) 
}


//libraryDependencies += "com.typesafe.scala-logging" % "scala-logging_2.11" % "3.1.0"


addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

TaskKey[Set[File]]("copy-for-deploy") <<=
  (fullClasspath in Runtime, target) map
    { (cp, out) =>
      val entries: Seq[File] = cp.files
      val toDirectory: File = out / "lib"
      IO.copy( entries x flat(toDirectory) )
    }

