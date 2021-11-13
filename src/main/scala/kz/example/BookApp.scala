package kz.example

import akka.actor.{Actor, ActorSystem, Props}
import akka.http.scaladsl.settings.ServerSettings
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.pathPrefix
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import kz.example.repository.BookRepositoryImpl
import kz.example.routers.BookRoute
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext
import com.softwaremill.macwire.wire
import kz.example.config.{actorSystemName, appConfig}
import kz.example.config.actorSystemName

import java.time.Duration
import java.util.concurrent.TimeUnit
import scala.concurrent.duration.{Duration, FiniteDuration}


object BookApp extends App {

  implicit val system: ActorSystem = ActorSystem(actorSystemName)
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val ec: ExecutionContext = system.dispatcher

  val log = LoggerFactory.getLogger(BookApp.getClass)




  // https://stackoverflow.com/questions/9714831/how-can-i-have-an-akka-actor-executed-every-5-min
  case class Message()
  class MyActor extends Actor {
    def receive = { case Message() => println("Do something in actor") }
  }

  val actor = system.actorOf(Props(new MyActor), name = "actor")
  system.scheduler.schedule(
    FiniteDuration(50, TimeUnit.MILLISECONDS),
    FiniteDuration(1500, TimeUnit.MILLISECONDS),
    actor,
    Message())



  val bookRepository = wire[BookRepositoryImpl]
  val bookRoute = wire[BookRoute]

//  val routes: Route = pathPrefix("api") {
  val routes: Route = {
    bookRoute.route
  }

  Http().bindAndHandle(routes, appConfig.application.host, appConfig.application.port)
//  Http().newServerAt("0.0.0.0", 8082)


  log.info("{} ActorSystem started", actorSystemName)

  system.registerOnTermination {
    log.info("Terminating {} ActorSystem", actorSystemName)
  }

}
