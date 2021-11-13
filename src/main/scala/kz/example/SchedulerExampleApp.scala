package kz.example

import java.time.{LocalDateTime, ZonedDateTime}
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import kz.example.Printer.Greeting
import kz.example.config.ApplicationConfig
import com.typesafe.akka.extension.quartz.QuartzSchedulerExtension
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import com.typesafe.config.{Config, ConfigFactory}
import pureconfig.loadConfigOrThrow


object Printer {
  def props: Props = Props[Printer]
  final case class Greeting(greeting: String)
}

class Printer extends Actor with ActorLogging {
  import Printer._

  def receive: PartialFunction[Any, Unit] = {
    case Greeting(greeting) =>
      log.info(s"Приветствие получено: $greeting в ${LocalDateTime.now()}") // ZonedDateTime.now()
  }
}

object SchedulerExampleApp extends App {

  val config: Config = ConfigFactory.load()
  val appConfig: ApplicationConfig = loadConfigOrThrow[ApplicationConfig](config)

  val system: ActorSystem = ActorSystem("scheduler-example")
  val printer: ActorRef = system.actorOf(Printer.props, "printer")

  //  printer ! Greeting("test message")
  //
  //  system.scheduler.schedule(initialDelay = 10.seconds, interval = 1.minute) {
  //    println(s"[${LocalDateTime.now}] [AkkaSchedulerExample] Executing something ...")
  //  }

  QuartzSchedulerExtension(system).createSchedule("schedule_name", Some("description"), appConfig.schedulerExpression.every5Seconds) // TODO: каждые 5-секунд  ( https://www.freeformatter.com/cron-expression-generator-quartz.html )
  QuartzSchedulerExtension(system).schedule("schedule_name", printer, Greeting("Запланированное сообщение"))

}
