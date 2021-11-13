package kz.example

import com.typesafe.config.{Config, ConfigFactory}
import pureconfig.loadConfigOrThrow
import slick.jdbc.PostgresProfile.api._


package object config {

  private val config: Config       = ConfigFactory.load()
  val actorSystemName              = config.getString("akka.system.name")
  val db                           = Database.forConfig("database.postgre")
  val appConfig: ApplicationConfig = loadConfigOrThrow[ApplicationConfig](config)

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
