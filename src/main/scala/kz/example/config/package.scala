package kz.example

import com.typesafe.config.{Config, ConfigFactory}
import pureconfig.loadConfigOrThrow
import slick.jdbc.PostgresProfile.api._


package object config {

  private val config: Config = ConfigFactory.load()
  val actorSystemName = config.getString("akka.system.name")
  val appConfig: ApplicationConfig = loadConfigOrThrow[ApplicationConfig](config)
  val db = Database.forConfig("database.postgre")

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
