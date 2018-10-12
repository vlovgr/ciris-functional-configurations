lazy val root = project
  .in(file("."))
  .settings(scalaSettings)
  .settings(publishSettings)
  .settings(dependencySettings)
  .enablePlugins(GhpagesPlugin, TutPlugin)

lazy val scalaSettings = Seq(
  scalaVersion := "2.12.7",
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding", "UTF-8",
    "-feature",
    "-language:existentials",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-language:postfixOps",
    "-unchecked",
    "-Yno-adapted-args",
    "-Ywarn-dead-code",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard",
    "-Xfuture"
  )
)

lazy val publishSettings = Seq(
  ghpagesNoJekyll := true,
  makeSite := (makeSite dependsOn tut).value,
  siteSourceDirectory := tutTargetDirectory.value,
  ghpagesPushSite := (ghpagesPushSite dependsOn makeSite).value,
  git.remoteRepo := "git@github.com:vlovgr/ciris-functional-configurations.git",
  mappings in makeSite ++= Seq((resourceDirectory in Compile).value / "ciris.svg" -> "images/ciris.svg")
)

lazy val cirisVersion = "0.12.0"

lazy val http4sVersion = "0.19.0-M4"

lazy val pureConfigVersion = "0.9.2"

lazy val refinedVersion = "0.9.2"

lazy val dependencySettings = Seq(
  resolvers += Resolver.bintrayRepo("ovotech", "maven"),
  libraryDependencies ++= Seq(
    "is.cir" %% "ciris-cats",
    "is.cir" %% "ciris-cats-effect",
    "is.cir" %% "ciris-core",
    "is.cir" %% "ciris-enumeratum",
    "is.cir" %% "ciris-generic",
    "is.cir" %% "ciris-refined",
    "is.cir" %% "ciris-spire",
    "is.cir" %% "ciris-squants"
  ).map(_ % cirisVersion) ++ Seq(
    "org.http4s" %% "http4s-dsl",
    "org.http4s" %% "http4s-blaze-server"
  ).map(_ % http4sVersion) ++ Seq(
    "eu.timepit" %% "refined",
    "eu.timepit" %% "refined-cats",
    "eu.timepit" %% "refined-pureconfig"
  ).map(_% refinedVersion) ++ Seq(
    "com.github.pureconfig" %% "pureconfig",
    "com.github.pureconfig" %% "pureconfig-cats-effect"
  ).map(_ % pureConfigVersion) ++ Seq(
    "com.ovoenergy" %% "ciris-kubernetes" % "0.10",
    "com.beachape" %% "enumeratum" % "1.5.13",
    "com.typesafe" % "config" % "1.3.2"
  ) ++ Seq(
    "org.typelevel" %% "kittens" % "1.1.0"
  )
)
