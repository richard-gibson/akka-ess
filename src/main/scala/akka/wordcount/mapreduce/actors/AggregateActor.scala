package akka.wordcount.mapreduce.actors

import akka.actor.Actor
import akka.wordcount.mapreduce.MapData
import akka.wordcount.mapreduce.Result
import scala.collection.mutable

/**
 * User: Richard
 * Date: 07/09/13
 * Time: 20:45
 */
class AggregateActor extends Actor {
  val finalReducedMap = new mutable.HashMap[String, Int]
  var count = 0

  def receive: Receive = {
    case MapData(mapData) =>
      aggregateInMemoryReduce(mapData)
    case Result =>
      sender ! (count,finalReducedMap)
  }

  def aggregateInMemoryReduce(mappedList: Map[String, Int]):Unit = {
    count+= 1
    for ((key,value) <- mappedList) {
      val oldValue =
        if (finalReducedMap contains key) finalReducedMap(key)
        else 0
      finalReducedMap += (key -> (value + oldValue))
    }
  }
}
