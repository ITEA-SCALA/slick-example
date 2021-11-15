package com.example.repository

import com.example.data.Book
import scala.concurrent.Future


trait BookRepository {
  def exists(id: Int): Future[Boolean]
  def find(id: Int): Future[Seq[Book]]
  def insert(book: Book): Future[Int]
  def save(book: Book): Future[Int]
  def update(book: Book): Future[Int]
  def remove(id: Int): Future[Int]

  def prepareRepository(): Future[Unit]
}