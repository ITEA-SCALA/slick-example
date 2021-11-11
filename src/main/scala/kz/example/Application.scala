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
 * Этот класс - создает и собирает компоненты (бины) приложения:
 * 1. restRoutes ........ компонент
 * 2. booksRepository ... компонент
 * ***
 *
 * Слой репозитория (базы данных) реализован в repository.BooksRepositoryImpl
 * А подключение к базе данных реализован в repository.package
 * Relational Mapping таблицы в для Scala реализован в repository.table.BooksTable
 *
 * Клиентский запрос приходит в 'BookRoute' - он выполняет роль контроллера
 *
 * А сервисный слой для реализован на актерах
 * class actor.book.BookManager, который наследуется от Actor (RequestActor)
 * + метод 'receive: Receive'
 *   - получает сообщение (объект клиентского запроса) из контроллера и передает действие в репозиторий (базы данных)  'booksRepository.add(request.book)'
 *   - Сам же 'actor.RequestActor' использует 'routers.Request' - который является обверткой (в случае успешного выполнения действия с базой данных, он копирует данные из клиентского запроса и потом эти данные возвращает в ответ)
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
