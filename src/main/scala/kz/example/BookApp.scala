package kz.example

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.pathPrefix
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.typesafe.config.{Config, ConfigFactory}
import kz.example.repository.{BookRepositoryImpl}
import kz.example.routers.BookRoute
import org.slf4j.LoggerFactory
import scala.concurrent.ExecutionContext
import com.softwaremill.macwire.wire


object BookApp extends App {

  val config: Config = ConfigFactory.load()
  val actorSystemName = config.getString("akka.system.name")

  implicit val system: ActorSystem = ActorSystem(actorSystemName)
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher

  val log = LoggerFactory.getLogger(BookApp.getClass)


  val bookRepository = wire[BookRepositoryImpl]
  val bookRoute = wire[BookRoute]

//  val routes: Route = pathPrefix("api") {
  val routes: Route = {
    bookRoute.route
  }

  val host = config.getString("application.host")
  val port = config.getInt("application.port")
  Http().bindAndHandle(routes, host, port)

  log.info("{} ActorSystem started", actorSystemName)

  system.registerOnTermination {
    log.info("Terminating {} ActorSystem", actorSystemName)
  }

}
