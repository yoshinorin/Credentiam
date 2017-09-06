import com.typesafe.sbt.SbtScalariform._

import scalariform.formatter.preferences._

name := "credentiam"

version := "0.0.1"

scalaVersion := "2.12.3"

resolvers += Resolver.jcenterRepo

scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-unchecked"
)
libraryDependencies ++= Seq(
  "com.mohiva" %% "play-silhouette" % "5.0.0",
  "com.mohiva" %% "play-silhouette-password-bcrypt" % "5.0.0",
  "com.mohiva" %% "play-silhouette-persistence" % "5.0.0",
  "com.mohiva" %% "play-silhouette-crypto-jca" % "5.0.0",
  "com.mohiva" %% "play-silhouette-testkit" % "5.0.0" % "test",
  "com.unboundid" % "unboundid-ldapsdk" % "4.0.1",
  specs2 % Test,
  ehcache,
  guice,
  cache,
  filters
)
