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
    find ~
    insert ~
    update ~
    remove
  }

  /*
   * http://localhost:8082/api/books/30
   */
//  def find: Route = path(IntNumber) { id =>
//    get {
//      onSuccess(repository.find2(id)) { res =>
//        complete(StatusCodes.OK, HttpEntity(ContentTypes.`application/json`, res.toJson.prettyPrint))
////        complete(StatusCodes.OK, HttpEntity(ContentTypes.`application/json`, res(0).toJson.prettyPrint))
//      }
//    }
//  }
  def find: Route = path(IntNumber) { id =>
    get {
      onSuccess(repository.find(id)) { res =>
        complete(res)
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
 * {
 *    "record": 1
 * }
 */
  def insert: Route = pathEndOrSingleSlash {
    entity(as[Book]) { book =>
      post {
        onSuccess(repository.insert(book)) { res =>
          complete(StatusCodes.OK, HttpEntity(ContentTypes.`application/json`, OpSuccess(res).toJson.prettyPrint))
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
 * {
 *    "record": 1
 * }
 */
  def remove: Route = path(IntNumber) { id =>
    delete {
//      onSuccess(repository.remove(id)) {
//        case 1 => complete(StatusCodes.OK, HttpEntity(ContentTypes.`application/json`, OpSuccess(1).toJson.prettyPrint))
//        case _ => complete(StatusCodes.BadRequest, HttpEntity(ContentTypes.`application/json`, OpFailure(s"There was an internal server error").toJson.prettyPrint))
//      }
      onSuccess(repository.remove(id)) { res =>
        complete(StatusCodes.OK, HttpEntity(ContentTypes.`application/json`, OpSuccess(res).toJson.prettyPrint))
      }
    }
  }
}
