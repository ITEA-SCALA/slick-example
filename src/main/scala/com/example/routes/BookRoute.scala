package com.example.routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.example.data.{Book, RequestBook, OpFailure, OpSuccess}
import com.example.repository.BookRepository
import com.example.data.JsonSupport._


class BookRoute(repository: BookRepository) {
  def route: Route = pathPrefix("books") {
    find ~
    list ~
    filter ~
//    insert ~
    create ~
    update ~
    remove
  }

  /*
   * GET
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
   * http://localhost:8082/api/books/filter?author=author_19r&name=name_19
   * http://localhost:8082/api/books/filter?name=name_19&author=test update author
   * ***
   * [
   *   {
   *     "author": "author_19",
   *     "id": 5,
   *     "name": "name_19"
   *   },
   *   {
   *     "author": "author_19",
   *     "id": 6,
   *     "name": "name_19"
   *   }
   * ]
   */
  def filter: Route = path("filter") {
    parameters('author.as[String], 'name.as[String]) { (author, name) =>
      get {
        onSuccess(repository.filter(author, name)) ( res =>
        complete( res ))
      }
    }
  }

  /*
   * GET
   * http://localhost:8082/api/books
   * ***
   * {
   *    "author": "test author",
   *    "id": 5,
   *    "name": "test name"
   * },
   * {
   *   "author": "test author",
   *   "id": 6,
   *   "name": "test name"
   * },
   * {
   *   "author": "test author",
   *   "id": 7,
   *   "name": "test name"
   * },
   * ...
   * {
   *   "author": "test update author",
   *   "id": 20,
   *   "name": "test update name"
   * }
   */
  def list: Route = pathEndOrSingleSlash {
    get {
      onSuccess(repository.list()) ( res =>
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
  @Deprecated
  def insert: Route = pathEndOrSingleSlash {
    entity(as[Book]) { book =>
      post {
        onSuccess(repository.insert(book)) ( res =>
          complete( OpSuccess(res) ))
      }
    }
  }

/*
 * POST
 * http://localhost:8082/api/books
 * {
 *   "name": "test name",
 *   "author": "test author"
 * }
 * ***
 * {
 *   "id": 16,
 *   "author": "test author",
 *   "name": "test name"
 * }
 */
  def create: Route = pathEndOrSingleSlash {
    entity(as[RequestBook]) { req =>
      post {
        onSuccess(repository.create(req)) ( res =>
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
