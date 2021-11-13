package kz.example.actor.book

import akka.actor.Props
import akka.http.scaladsl.server.{RequestContext, RouteResult}
import kz.example.actor.RequestActor
import kz.example.actor.book.manager.{BookAdder, BookGetter, BookRemover, BookUpdater}
import kz.example.domain.Book
import kz.example.repository.BookRepository

import scala.concurrent.Promise


object BookManager {

  def props(repository: BookRepository,
            requestContext: RequestContext,
            promise: Promise[RouteResult]): Props =
    Props(new BookManager(repository, requestContext, promise))

  sealed trait BookRequest

  case class AddBook(book: Book) extends BookRequest
  case class GetBook(bookId: Int) extends BookRequest
  case class UpdateBook(book: Book) extends BookRequest
  case class DeleteBook(bookId: Int) extends BookRequest

}


class BookManager(val booksRepository: BookRepository, // это как Get/Set-ер
                  val requestContext: RequestContext,
                  val promise: Promise[RouteResult])
  extends RequestActor
    with BookAdder
    with BookGetter
    with BookUpdater
    with BookRemover {

  import BookManager._

  override def receive: Receive = {
    case request: BookRequest =>
      log.info("Received BookRequest = {}", request)
      handle(request)
  }

  def handle(request: BookRequest): Unit = request match {
    case s: AddBook => addBook(s)
    case s: GetBook => getBook(s)
    case s: UpdateBook => updateBook(s)
    case s: DeleteBook => deleteBook(s)
  }

}
