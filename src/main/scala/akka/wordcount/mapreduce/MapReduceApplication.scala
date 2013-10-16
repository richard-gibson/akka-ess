package akka.wordcount.mapreduce

import akka.util.Timeout
import akka.actor.{Props, ActorSystem}
import akka.wordcount.mapreduce.actors.MasterActor
import akka.pattern.ask
import java.util.concurrent.TimeUnit.SECONDS
import scala.concurrent.Await


/**
 * User: Richard
 * Date: 06/09/13
 * Time: 21:41
 */

  sealed trait MapReduceMessage
  case class MapData(dataList:  Map[String, Int]) extends
  MapReduceMessage
  case class Result() extends MapReduceMessage

object MapReduceApplication extends App {
  val _system = ActorSystem("MapReduceApp")
  val master = _system.actorOf(Props[MasterActor], "master")

  implicit val timeout = Timeout(5, SECONDS)
  println(Await.result(master ? Result, timeout.duration))
  master ! "The quick brown fox tried to jump over the lazy dog and fell on the dog"
  Thread.sleep(500)
  println(Await.result(master ? Result, timeout.duration))
  master ! "Dog is man's best friend"
  Thread.sleep(500)
  println(Await.result(master ? Result, timeout.duration))
  master ! "Dog and Fox belong to the same family"


  Thread.sleep(500)
  val future = master ? Result
  val result = Await.result(future, timeout.duration)
  println(result)
  _system.shutdown()
}