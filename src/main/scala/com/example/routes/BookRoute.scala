package com.example.routes

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.example.data.{Book, OpFailure, OpSuccess}
import com.example.repository.BookRepository
import com.example.data.JsonSupport._
import spray.json._


class BookRoute(repository: BookRepository) {
  def route: Route = pathPrefix("books") {
    exists1 ~
    exists2 ~
    find ~
    getBook ~
    save ~
    insert ~
    update ~
    deleteBook
  }

  /*
   * GET
   * http://localhost:8082/api/books/exists1/30
   * ***
   * Vector(Book(30,test update name,test update author))
   */
  def exists1: Route = pathPrefix("exists1") {
    path(IntNumber) { bookId =>
      get {
        onSuccess(repository.exists1(bookId)) { res =>
          complete(s"$res")
        }
      }
    }
  }

  /*
   * GET
   * http://localhost:8082/api/books/exists2/30
   * ***
   * Vector(Book(30,test update name,test update author))
   */
  def exists2: Route = pathPrefix("exists2") {
    path(IntNumber) { id =>
      get {
        onSuccess(repository.exists2(id)) { res =>
          complete(s"$res")
        }
      }
    }
  }

  /*
   * http://localhost:8082/api/books/find/30
   */
  def find: Route = pathPrefix("find") {
    path(IntNumber) { id =>
      get {
        onSuccess(repository.find(id)) { res =>
          complete(s"$res")
        }
      }
    }
  }

  /*
   * GET
   * http://localhost:8082/api/books/30
   * ***
   * Vector(Book(30,test update name,test update author))
   */
  def getBook: Route = path(IntNumber) { bookId =>
    get {
      onSuccess(repository.getBook(bookId)) { res =>
        complete(StatusCodes.OK, HttpEntity(ContentTypes.`application/json`, res.toJson.prettyPrint))
//        complete(StatusCodes.OK, HttpEntity(ContentTypes.`application/json`, res(0).toJson.prettyPrint))
      }
    }
  }

/*
 * POST
 * http://localhost:8082/api/books
 * {
 *   "id": 30,
 *   "name": "test name",
 *   "author": "test author"
 * }
 * ***
 * "1"
 */
  def save: Route = pathEndOrSingleSlash {
    entity(as[Book]) { book =>
      post {
        onSuccess(repository.save(book)) { res =>
          complete(StatusCodes.OK, HttpEntity(ContentTypes.`application/json`, OpSuccess(res).toJson.prettyPrint))
        }
      }
    }
  }

  /*
   * POST
   * http://localhost:8082/api/books/insert
   * {
   *   "id": 30,
   *   "name": "test name",
   *   "author": "test author"
   * }
   * ***
   * "1"
   */
  def insert: Route = path("insert") {
    entity(as[Book]) { book =>
      post {
        onSuccess(repository.insert(book)) { res =>
          complete(s"$res")
        }
      }
    }
  }

  /*
   * PUT
   * http://localhost:8082/api/books
   * {
   *   "id": 30,
   *   "name": "test update name",
   *   "author": "test update author"
   * }
   * ***
   * "1"
   */
  def update: Route = pathEndOrSingleSlash {
    entity(as[Book]) { book =>
      put {
        onSuccess(repository.update(book)) { res =>
          complete(s"$res")
        }
      }
    }
  }

/*
 * DELETE
 * http://localhost:8082/api/books/30
 * ***
 * "1"
 */
  def deleteBook: Route = path(IntNumber) { bookId =>
    delete {
      onSuccess(repository.deleteBook(bookId)) {
        case 1 => complete(StatusCodes.OK, HttpEntity(ContentTypes.`application/json`, OpSuccess(1).toJson.prettyPrint))
        case _ => complete(StatusCodes.BadRequest, HttpEntity(ContentTypes.`application/json`, OpFailure(s"There was an internal server error").toJson.prettyPrint))
      }
    }
  }
}
