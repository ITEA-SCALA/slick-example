package com.example.repository

import com.example.data.{Book, NewBook}
import scala.concurrent.Future


trait BookRepository {
  def find(id: Int): Future[Option[Book]]
  def insert(book: Book): Future[Int]
  def insert2(newBook: NewBook): Future[Book]
  def update(book: Book): Future[Int]
  def remove(id: Int): Future[Int]

  def prepareRepository(): Future[Unit]
}