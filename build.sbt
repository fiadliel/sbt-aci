name := "sbt-aci"

organization := "com.gilt.sbt"

sbtPlugin := true

// The Typesafe repository
resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.0.3" % "provided")
