package com.example.data

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol


case class Book(id: Int, name: String, author: String)

/**
 * Success message
 * @param record the message
 */
case class OpSuccess(record : Int)

/**
 * Failure message
 * @param error the message
 */
case class OpFailure(error : String)

/**
 * Json formatters
 */
object JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val bookFormat = jsonFormat3(Book)
  implicit val opSuccessFormat = jsonFormat1(OpSuccess)
  implicit val opFailureFormat = jsonFormat1(OpFailure)
}