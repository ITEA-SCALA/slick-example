package com.example

import com.typesafe.config.{Config, ConfigFactory}
import pureconfig.loadConfigOrThrow
import slick.jdbc.PostgresProfile.api._


package object config {

  private val config: Config       = ConfigFactory.load()
  val actorSystemName: String      = config.getString("akka.system.name")
  val appConfig: ApplicationConfig = loadConfigOrThrow[ApplicationConfig](config)
  val PostgreDB: Database          = Database.forConfig("db.localhost.postgre")

  case class ApplicationConfig(
    schedulerExpression: SchedulerExpressionConfig,
    application: Application,
  )

  case class SchedulerExpressionConfig(
    every5Seconds: String
  )

  case class Application(
    host: String,
    port: Int
  )
}
