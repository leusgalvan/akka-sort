name := """akka-sort"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.6"

addSbtPlugin("org.wartremover" % "sbt-wartremover" % "2.4.1")
wartremoverErrors ++= Warts.allBut(Wart.ImplicitParameter)
wartremoverExcluded ++= routes.in(Compile).value

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.5.21" % Test