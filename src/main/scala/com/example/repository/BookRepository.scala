package com.example.repository

import com.example.data.Book
import scala.concurrent.Future


trait BookRepository {
  def exists1(bookId: Int): Future[Boolean]
  def exists2(id: Int): Future[Boolean]
  def find(id: Int): Future[Seq[Book]]
  def save(book: Book): Future[Int]
  def insert(book: Book): Future[Int]
  def update(book: Book): Future[Int]
  def deleteBook(bookId: Int): Future[Int]
  def getBook(bookId: Int): Future[Seq[Book]]

  def prepareRepository(): Future[Unit]
}