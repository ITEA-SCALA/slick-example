package kz.example.actor

import akka.actor.{Actor, ActorLogging}
import kz.example.routers.Request
import kz.example.utils.Serializers

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration._


trait RequestActor
  extends Actor
    with ActorLogging
    with Serializers
    with Request {

  context.setReceiveTimeout(60 seconds)

  implicit val ec: ExecutionContextExecutor = context.dispatcher

}
