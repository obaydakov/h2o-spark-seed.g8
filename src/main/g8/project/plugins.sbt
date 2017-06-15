resolvers += Classpaths.typesafeResolver

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.3.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "0.6.3")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.11.2")

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

