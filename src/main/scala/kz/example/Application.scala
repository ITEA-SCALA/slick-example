package kz.example

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.{Config, ConfigFactory}
import kz.example.repository.{BooksRepositoryImpl, BooksRepository}
import kz.example.routers.Routes
import org.slf4j.LoggerFactory
import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

/*
 * Здесь создаются и собираются компоненты (бины) приложения:
 * 1. restRoutes ........ компонент
 * 2. booksRepository ... компонент
 */
object Application extends App {

  val config: Config = ConfigFactory.load()
  val actorSystemName = config.getString("akka.system.name")

  implicit val system: ActorSystem = ActorSystem(actorSystemName)
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher

  val log = LoggerFactory.getLogger(Application.getClass)

  val booksRepository: BooksRepository = new BooksRepositoryImpl()

  booksRepository.prepareRepository().onComplete {
    case Success(_) => log.info("Books repository was successfully prepared")

    case Failure(exception) =>
      log.error("Failed to prepare books repository with exception = {}", exception.toString)
      throw exception
  }

  val restRoutes = new Routes(booksRepository)

  val host = config.getString("application.host")
  val port = config.getInt("application.port")
  Http().bindAndHandle(restRoutes.route, host, port)

  log.info("{} ActorSystem started", actorSystemName)

  system.registerOnTermination {
    log.info("Terminating {} ActorSystem", actorSystemName)
  }

}
