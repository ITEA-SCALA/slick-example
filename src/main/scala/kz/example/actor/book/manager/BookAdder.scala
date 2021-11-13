package kz.example.actor.book.manager

import akka.http.scaladsl.model.StatusCodes
import kz.example.actor.RequestActor
import kz.example.messages.error.ErrorMessages
import kz.example.repository.BookRepository

import scala.util.{Failure, Success}


trait BookAdder {
  this: RequestActor =>

  import kz.example.actor.book.BookManager.AddBook

  def booksRepository: BookRepository

  def addBook(request: AddBook): Unit = {
    booksRepository.add(request.book).onComplete {
      case Success(value) =>
        log.debug("Successfully added book")
        complete(request.book, StatusCodes.Created)

      case Failure(exception) =>
        log.error("Got exception while adding book = {}", exception.toString)
        complete(ErrorMessages.INTERNAL_SERVER_ERROR, StatusCodes.InternalServerError)
      }
  }

}
