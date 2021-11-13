package kz.example.routers

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import kz.example.repository.BookRepository


class BookRoute(repository: BookRepository) {
  def route: Route = pathPrefix("book") {
    getBook
  }

  /*
   * GET
   * http://localhost:8082/api/book/13
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
}
