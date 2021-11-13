name         := "slick-example"
version      := "0.1.0-SNAPSHOT"
scalaVersion := "2.12.8"

//scalaVersion := "2.12.6"
//scalaVersion := "2.13.6"

val akkaVer           = "2.5.20"
//val akkaVer           = "2.6.17"
val akkaHttpVer       = "10.1.7"
//val akkaHttpVer       = "10.2.7"
val slickVer          = "3.3.0"
//val slickVer          = "3.3.3"
val postgresVer       = "42.2.5"
val json4sVer         = "3.6.4"
//val json4sVer         = "4.0.3"
val akkaHttpJson4sVer = "1.25.2"
//val akkaHttpJson4sVer = "1.38.2"
val macwireVer        = "2.4.1"
val pureConfigVer     = "0.9.0"
//val pureConfigVer     = "0.17.0"
//val akkaQuartzVer     = "1.7.1-akka-2.5.x"
val akkaQuartzVer     = "1.8.5-akka-2.6.x"
//val akkaQuartzVer     = "1.9.2-akka-2.6.x"
val logbackClassicVer = "1.2.3"
val scalaTestVer      = "3.0.8-RC4"
//val scalaTestVer      = "3.3.0-SNAP3"

libraryDependencies ++= Seq(
  "com.typesafe.akka"        %% "akka-actor"            % akkaVer,
  "com.typesafe.akka"        %% "akka-http"             % akkaHttpVer,
  "com.typesafe.slick"       %% "slick"                 % slickVer,
  "com.typesafe.slick"       %% "slick-hikaricp"        % slickVer,
  "org.postgresql"           %  "postgresql"            % postgresVer,
  "org.json4s"               %% "json4s-native"         % json4sVer,
  "org.json4s"               %% "json4s-jackson"        % json4sVer,
  "de.heikoseeberger"        %% "akka-http-json4s"      % akkaHttpJson4sVer,
  "com.softwaremill.macwire" %% "macros"                % macwireVer         % "provided",
  "com.github.pureconfig"    %% "pureconfig"            % pureConfigVer,
  "ch.qos.logback"           %  "logback-classic"       % logbackClassicVer,
  "org.scalatest"            %% "scalatest"             % scalaTestVer       % Test,
  "com.enragedginger"        %% "akka-quartz-scheduler" % akkaQuartzVer,
)

