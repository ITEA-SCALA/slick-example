package com.example.repository

import com.example.data.{Book, RequestBook}
import scala.concurrent.Future


trait BookRepository {
  def find(id: Int): Future[Option[Book]]
  def list(): Future[Seq[Book]]
  def filter(author: String, name: String): Future[Seq[Book]]
  def insert(book: Book): Future[Int]
  def create(req: RequestBook): Future[Book]
  def update(book: Book): Future[Int]
  def remove(id: Int): Future[Int]

  def prepareRepository(): Future[Unit]
}