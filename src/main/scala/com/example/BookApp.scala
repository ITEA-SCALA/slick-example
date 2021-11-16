package com.example

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.pathPrefix
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.example.repository.BookRepositoryPostgre
import com.example.routes.BookRoute
import org.slf4j.LoggerFactory
import scala.concurrent.ExecutionContext
import com.softwaremill.macwire.wire
import com.example.config.{actorSystemName, appConfig}


object BookApp extends App {
  implicit val system: ActorSystem             = ActorSystem(actorSystemName)
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val ec: ExecutionContext            = system.dispatcher

  val log = LoggerFactory.getLogger(BookApp.getClass)

  val bookRepository = wire[BookRepositoryPostgre]
  val bookRoute = wire[BookRoute]

  val routes: Route = pathPrefix("api") {
    bookRoute.route
  }

  Http().bindAndHandle(
    routes, appConfig.application.host, appConfig.application.port)

  system.registerOnTermination {
    log.info("Terminating {} ActorSystem", actorSystemName)
  }
}
