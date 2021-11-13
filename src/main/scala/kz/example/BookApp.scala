package kz.example

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.pathPrefix
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.typesafe.config.{Config, ConfigFactory}
import kz.example.repository.BookRepositoryImpl
import kz.example.routers.BookRoute
import org.slf4j.LoggerFactory
import scala.concurrent.ExecutionContext
import com.softwaremill.macwire.wire
import kz.example.config.{actorSystemName, appConfig}


object BookApp extends App {

  implicit val system: ActorSystem = ActorSystem(actorSystemName)
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val ec: ExecutionContext = system.dispatcher

  val log = LoggerFactory.getLogger(BookApp.getClass)


  val bookRepository = wire[BookRepositoryImpl]
  val bookRoute = wire[BookRoute]

//  val routes: Route = pathPrefix("api") {
  val routes: Route = {
    bookRoute.route
  }

  Http().bindAndHandle(routes, appConfig.application.host, appConfig.application.port)

  log.info("{} ActorSystem started", actorSystemName)

  system.registerOnTermination {
    log.info("Terminating {} ActorSystem", actorSystemName)
  }

}
