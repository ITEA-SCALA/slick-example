//package kz.example.repository.table
//
//import kz.example.domain.Book
//import slick.jdbc.PostgresProfile.api._
//import slick.lifted.ProvenShape
//
//trait BookTable {
//
//  class Books(tag: Tag) extends Table[Book](tag, "BOOKS") { // class Books(tag: Tag) extends Table[Book](tag, Some("public"), "BOOKS") {
//    def id: Rep[Int] = column[Int]("id", O.PrimaryKey)
//    def name: Rep[String] = column[String]("name")
//    def author: Rep[String] = column[String]("author")
//
//    def * : ProvenShape[Book] = (id, name, author) <> (Book.tupled, Book.unapply)
//
//  }
//
//  val books = TableQuery[Books]
//}
