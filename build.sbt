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

lazy val cirisVersion = "0.11.0"

lazy val dependencySettings = Seq(
  libraryDependencies ++= Seq(
    "is.cir" %% "ciris-cats",
    "is.cir" %% "ciris-cats-effect",
    "is.cir" %% "ciris-core",
    "is.cir" %% "ciris-enumeratum",
    "is.cir" %% "ciris-generic",
    "is.cir" %% "ciris-refined",
    "is.cir" %% "ciris-spire",
    "is.cir" %% "ciris-squants"
  ).map(_ % cirisVersion)
)
