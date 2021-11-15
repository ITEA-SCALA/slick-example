package com.example.routes

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.example.data.{Book, NewBook, OpFailure, OpSuccess}
import com.example.repository.BookRepository
import com.example.data.JsonSupport._
import spray.json._


class BookRoute(repository: BookRepository) {
  def route: Route = pathPrefix("books") {
    find ~
    insert ~
    insert2 ~
    update ~
    remove
  }

  /*
   * http://localhost:8082/api/books/30
   * ***
   * {
   *   "author": "test update author",
   *   "id": 32,
   *   "name": "test update name"
   * }
   */
  def find: Route = path(IntNumber) { id =>
    get {
      onSuccess(repository.find(id)) ( res =>
        complete( res ))
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
 * {
 *    "record": 1
 * }
 */
  def insert: Route = pathEndOrSingleSlash {
    entity(as[Book]) { book =>
      post {
        onSuccess(repository.insert(book)) ( res =>
          complete( OpSuccess(res) ))
      }
    }
  }

  def insert2: Route = path("insert") {
    entity(as[NewBook]) { newBook =>
      post {
        onSuccess(repository.insert2(newBook)) ( res =>
          complete( res ))
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
        onSuccess(repository.update(book)) ( res =>
          complete( OpSuccess(res) ))
      }
    }
  }

/*
 * DELETE
 * http://localhost:8082/api/books/30
 * ***
 * {
 *    "record": 1
 * }
 */
  def remove: Route = path(IntNumber) { id =>
    delete {
      onSuccess(repository.remove(id)) ( res =>
        complete( OpSuccess(res) ))
    }
  }
}
