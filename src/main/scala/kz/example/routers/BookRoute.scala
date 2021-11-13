package kz.example.routers

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import kz.example.domain.Book
import kz.example.repository.BookRepository
import kz.example.utils.Serializers


class BookRoute(repository: BookRepository) extends Serializers {
  def route: Route = pathPrefix("books") {
    getBook ~
    addBook
  }

  /*
   * GET
   * http://localhost:8082/api/book/13
   * http://localhost:8082/books/13
   * ***
   * Vector(Book(13,test update name,test update author))
   */
  def getBook: Route = path(IntNumber) { bookId =>
    get {
      onSuccess(repository.getBook(bookId)) { result =>
        complete(s"$result")
      }
    }
  }

/*
 * POST
 * http://localhost:8082/books
 * {
 *   "id": 17,
 *   "name": "test name",
 *   "author": "test author"
 * }
 * ***
 * "1"
 */
  def addBook: Route = pathEndOrSingleSlash {
    entity(as[Book]) { book =>
      post {
        onSuccess(repository.add(book)) { result =>
          complete(s"$result")
        }
      }
    }
  }
}
