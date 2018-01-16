import com.typesafe.sbt.SbtScalariform._
import scalariform.formatter.preferences._

name := "credentiam"
version := "0.0.1"
scalaVersion := "2.12.4"

resolvers += Resolver.jcenterRepo

scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-unchecked"
)

libraryDependencies ++= Seq(
  "net.codingwell" %% "scala-guice" % "4.1.0",
  "com.iheart" %% "ficus" % "1.4.1",
  "com.mohiva" %% "play-silhouette" % "5.0.3",
  "com.mohiva" %% "play-silhouette-password-bcrypt" % "5.0.3",
  "com.mohiva" %% "play-silhouette-persistence" % "5.0.3",
  "com.mohiva" %% "play-silhouette-crypto-jca" % "5.0.3",
  "com.mohiva" %% "play-silhouette-testkit" % "5.0.3" % "test",
  "com.unboundid" % "unboundid-ldapsdk" % "4.0.3",
  "org.ehcache" % "ehcache" % "3.4.0",
  specs2 % Test,
  ehcache,
  guice,
  filters
)

lazy val root = (project in file(".")).enablePlugins(PlayScala)

routesGenerator := InjectedRoutesGenerator

//********************************************************
// Scalariform settings
//********************************************************

defaultScalariformSettings

ScalariformKeys.preferences := ScalariformKeys.preferences.value
  .setPreference(FormatXml, false)
  .setPreference(DoubleIndentClassDeclaration, false)
  .setPreference(DanglingCloseParenthesis, Preserve)

//********************************************************
// Packaging
//********************************************************

enablePlugins(RpmPlugin, RpmDeployPlugin)
enablePlugins(DebianPlugin, DebianDeployPlugin)
enablePlugins(WindowsPlugin, WindowsDeployPlugin)

maintainer := "YoshinoriN"
packageSummary := "Credentiam"
packageDescription := """Credentiam package"""

// wix information
packageSummary in Windows := "Credentiam"
packageDescription in Windows := """Credentiam Installer"""
wixProductId := java.util.UUID.randomUUID().toString
wixProductUpgradeId := java.util.UUID.randomUUID().toString
