package akka.wordcount.mapreduce.actors

import akka.actor.{Props, Actor}
import akka.wordcount.mapreduce.{MapData,Result}
import akka.routing.RoundRobinRouter

/**
 * User: Richard
 * Date: 07/09/13
 * Time: 21:07
 */
class MasterActor extends Actor {
  val mapActor = context.actorOf(Props[MapActor].withRouter(
    RoundRobinRouter(nrOfInstances = 5)), name = "map")
  val aggregateActor = context.actorOf(Props[AggregateActor],
    name = "aggregate")

  def receive: Receive = {
    case line: String => mapActor ! line
    case mapData: MapData => aggregateActor ! mapData
    case Result => aggregateActor forward Result
  }
}
