package akka.wordcount.mapreduce.actors

import akka.actor.Actor
import akka.wordcount.mapreduce.MapData

class MapActor extends Actor with WordFrequencyMapper{

  def receive: Receive = {
    case message: String =>
      sender ! evaluateExpression(message)
  }

  def evaluateExpression(message: String): MapData =
    MapData (dataToMap(message))

}
