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
