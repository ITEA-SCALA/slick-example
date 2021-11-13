package kz.example.repository

import kz.example.config.db
import kz.example.domain.Book
import slick.jdbc.PostgresProfile.api._
import slick.lifted.ProvenShape
import scala.concurrent.Future


class BookRepositoryImpl()
  extends BookRepository
    with BookTable {

  override def save(book: Book): Future[Int] = {
    db.run(
      books += book
    )
  }

  override def update(book: Book): Future[Int] = {
    db.run(
      books.filter(_.id === book.id)
        .map(b => (b.name, b.author))
        .update((book.name, book.author))
    )
  }

  override def deleteBook(bookId: Int): Future[Int] = {
    db.run(
      books.filter(_.id === bookId).delete
    )
  }

  override def getBook(bookId: Int): Future[Seq[Book]] = {
    db.run(
      books.filter(_.id === bookId).result
    )
  }

  override def prepareRepository(): Future[Unit] = {
    db.run(
      books.schema.createIfNotExists
    )
  }
}

trait BookRepository {
  def save(book: Book): Future[Int]
  def update(book: Book): Future[Int]
  def deleteBook(bookId: Int): Future[Int]
  def getBook(bookId: Int): Future[Seq[Book]]

  def prepareRepository(): Future[Unit]
}

trait BookTable {

  class Books(tag: Tag) extends Table[Book](tag, "BOOKS") { // class Books(tag: Tag) extends Table[Book](tag, Some("public"), "BOOKS") {
    def id: Rep[Int] = column[Int]("id", O.PrimaryKey)
    def name: Rep[String] = column[String]("name")
    def author: Rep[String] = column[String]("author")

    def * : ProvenShape[Book] = (id, name, author) <> (Book.tupled, Book.unapply)
  }

  val books = TableQuery[Books]
}