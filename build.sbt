name         := "slick-example"
version      := "0.1.0-SNAPSHOT"
scalaVersion := "2.12.8"

val akkaVer           = "2.5.20"
val akkaHttpVer       = "10.1.7"
val slickVer          = "3.3.0"
val postgresVer       = "42.2.5"
val json4sVer         = "3.6.4"
val akkaHttpJson4sVer = "1.25.2"
val macwireVer        = "2.4.1"
val logbackClassicVer = "1.2.3"
val scalaTestVer      = "3.0.8-RC4"

libraryDependencies ++= Seq(
  "com.typesafe.akka"        %% "akka-actor"       % akkaVer,
  "com.typesafe.akka"        %% "akka-http"        % akkaHttpVer,
  "com.typesafe.slick"       %% "slick"            % slickVer,
  "com.typesafe.slick"       %% "slick-hikaricp"   % slickVer,
  "org.postgresql"           %  "postgresql"       % postgresVer,
  "org.json4s"               %% "json4s-native"    % json4sVer,
  "org.json4s"               %% "json4s-jackson"   % json4sVer,
  "de.heikoseeberger"        %% "akka-http-json4s" % akkaHttpJson4sVer,
  "com.softwaremill.macwire" %% "macros"           % macwireVer         % "provided",
  "ch.qos.logback"           %  "logback-classic"  % logbackClassicVer,
  "org.scalatest"            %% "scalatest"        % scalaTestVer % Test
)

