package com.example.repository

import com.example.config.PostgreDB
import com.example.data.Book
import slick.jdbc.PostgresProfile.api._
import slick.lifted.ProvenShape
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global


class BookRepositoryPostgre
  extends BookEntity(TableQuery[BookTable])
    with BookRepository {

  override def exists(id: Int) = {
    val query = for {
      book <- entity if book.id === id
    } yield true
    PostgreDB.run(query.exists.result)
  }

  override def save(book: Book): Future[Int] = {
    PostgreDB.run {
      entity += book  // entities.insertOrUpdate(book)
    }
  }

  def insert(book: Book) = {
    exists(book.id).flatMap {
      case res if res => Future(0)
      case _ => save(book)
    }
  }

  override def find(id: Int) = {
    val query = for {
      book <- entity if book.id === id
    } yield book
    PostgreDB.run(query.result)
  }

  override def update(book: Book): Future[Int] = {
    PostgreDB.run {
      entity.filter(_.id === book.id)
        .map(b => (b.name, b.author))
        .update((book.name, book.author))
    }
  }

  override def deleteBook(bookId: Int): Future[Int] = {
    PostgreDB.run {
      entity.filter(_.id === bookId).delete
    }
  }

  override def getBook(bookId: Int): Future[Seq[Book]] = {
    PostgreDB.run {
      entity.filter(_.id === bookId).result
    }
  }

  override def prepareRepository(): Future[Unit] = {
    PostgreDB.run {
      entity.schema.createIfNotExists
    }
  }
}

abstract class BookEntity[E <: BookTable](val entity: TableQuery[E])

class BookTable(tag: Tag) extends Table[Book](tag, "BOOKS") { // class Books(tag: Tag) extends Table[Book](tag, Some("public"), "BOOKS") {
  def id: Rep[Int] = column[Int]("id", O.PrimaryKey)
  def name: Rep[String] = column[String]("name")
  def author: Rep[String] = column[String]("author")

  def * : ProvenShape[Book] = (id, name, author) <> (Book.tupled, Book.unapply)
}
