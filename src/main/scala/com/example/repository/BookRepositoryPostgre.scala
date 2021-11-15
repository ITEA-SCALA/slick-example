package com.example.repository

import com.example.config.PostgreDB
import com.example.data.{Book, RequestBook}
import slick.jdbc.PostgresProfile.api._
import slick.lifted.ProvenShape

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global


class BookRepositoryPostgre
  extends BookEntity(TableQuery[BookTable])
    with BookRepository
{
  override def find(id: Int) = {
    getBookById(id).flatMap ( res =>
      Future(res.headOption))
  }

  override def list() = {
    PostgreDB.run {
      entity.result
    }
  }

  override def filter(author: String, name: String) = {
    PostgreDB.run {
      entity
        .filter(_.author === author)
        .filter(_.name === name)
        .result
    }
  }

  @Deprecated
  override def insert(book: Book) = {
    exists(book.id).flatMap {
      case res if res => Future(0)
      case _ => save(book)
    }
  }

  def create(req: RequestBook) = {
    val book = Book(req.name, req.author)
    saveAutoInc(req).flatMap( id =>
        Future(book.copy(id = id)))
  }

  override def update(book: Book) = {
    PostgreDB.run {
      entity.filter(_.id === book.id)
        .map(b => (b.name, b.author))
        .update((book.name, book.author))
    }
  }

  override def remove(id: Int) = {
    PostgreDB.run {
      entity.filter(_.id === id)
        .delete
    }
  }

  override def prepareRepository() = {
    PostgreDB.run {
      entity.schema.createIfNotExists
    }
  }
}

abstract class BookEntity[E <: BookTable](val entity: TableQuery[E])
{
  def getBookById(id: Int): Future[Seq[Book]] = {
    val query: Query[E, Book, Seq] = for {
      book <- entity if book.id === id
    } yield book
    PostgreDB.run(query.result)
  }

  def exists(id: Int): Future[Boolean] = {
    val query: Query[E, Book, Seq] = for {
      book <- entity if book.id === id
    } yield book
    PostgreDB.run(query.exists.result)
  }

  def save(book: Book): Future[Int] = {
    PostgreDB.run {
      entity += book  // entities.insertOrUpdate(book)
    }
  }

  def saveAutoInc(req: RequestBook): Future[Int] = {
    PostgreDB.run(entity returning entity.map(_.id) += Book(req.name, req.author))
  }
}

class BookTable(tag: Tag) extends Table[Book](tag, "BOOKS") { // class Books(tag: Tag) extends Table[Book](tag, Some("public"), "BOOKS") {
  def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def name: Rep[String] = column[String]("name")
  def author: Rep[String] = column[String]("author")

//  def * : ProvenShape[Book] = (id, name, author) <> (Book.tupled, Book.unapply)
  def * = (name, author, id) <> (Book.tupled, Book.unapply)
}
