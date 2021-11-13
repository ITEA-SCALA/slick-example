package kz.example.routers

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import kz.example.domain.Book
import kz.example.repository.BookRepository
import kz.example.utils.Serializers


class BookRoute(repository: BookRepository) extends Serializers {
  def route: Route = pathPrefix("books") {
    getBook ~
    add ~
    update ~
    deleteBook
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
  def add: Route = pathEndOrSingleSlash {
    entity(as[Book]) { book =>
      post {
        onSuccess(repository.add(book)) { result =>
          complete(s"$result")
        }
      }
    }
  }

  /*
   * PUT
   * http://localhost:8082/books
   * {
   *   "id": 17,
   *   "name": "test update name",
   *   "author": "test update author"
   * }
   * ***
   * "1"
   */
  def update: Route = pathEndOrSingleSlash {
    entity(as[Book]) { book =>
      put {
        onSuccess(repository.update(book)) { result =>
          complete(s"$result")
        }
      }
    }
  }

/*
 * DELETE
 * http://localhost:8082/books/17
 * ***
 * "1"
 */
  def deleteBook: Route = path(IntNumber) { bookId =>
    delete {
      onSuccess(repository.deleteBook(bookId)) { result =>
        complete(s"$result")
      }
    }
  }
}
