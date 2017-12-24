organization := "example"
name := "scala-sandbox"
version := "1.0.0-SNAPSHOT"

scalaVersion := "2.12.2"

scalacOptions ++= Seq(
  "-feature"
  , "-deprecation"
  , "-unchecked"
  , "-encoding"
  , "UTF-8"
  , "-Xfatal-warnings"
  , "-language:_"
  , "-Ywarn-adapted-args" // Warn if an argument list is modified to match the receiver
  , "-Ywarn-dead-code" // Warn when dead code is identified.
  , "-Ywarn-inaccessible" // Warn about inaccessible types in method signatures.
  , "-Ywarn-infer-any" // Warn when a type argument is inferred to be `Any`.
  , "-Ywarn-nullary-override" // Warn when non-nullary `def f()' overrides nullary `def f'
  , "-Ywarn-nullary-unit" // Warn when nullary methods return Unit.
  , "-Ywarn-numeric-widen" // Warn when numerics are widened.
  , "-Ywarn-unused" // Warn when local and private vals, vars, defs, and types are are unused.
  , "-Ywarn-unused-import" // Warn when imports are unused.
)

libraryDependencies ++= Seq(
  "org.scalatest"          %%   "scalatest"             % "3.0.1" % Test,
  "org.scalikejdbc"        %%   "scalikejdbc"           % "2.5.2",
  "org.scalikejdbc"        %%   "scalikejdbc-config"    % "2.5.2",
  "org.scalikejdbc"        %%   "scalikejdbc-test"      % "2.5.2" % Test,
  "ch.qos.logback"          %   "logback-classic"      % "1.2.3",
  "org.skinny-framework"   %%   "skinny-orm"              % "2.3.7",
  "mysql"                   %   "mysql-connector-java" % "6.0.6"
)

