package kz.example

import com.typesafe.config.{Config, ConfigFactory}
import pureconfig.loadConfigOrThrow


package object config {
  private val config: Config = ConfigFactory.load()
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
