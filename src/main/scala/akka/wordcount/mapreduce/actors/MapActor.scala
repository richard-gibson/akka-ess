package akka.wordcount.mapreduce.actors

import akka.actor.Actor
import akka.wordcount.mapreduce.MapData

/**
 * User: Richard
 * Date: 06/09/13
 * Time: 21:53
 */
class MapActor extends Actor {

  val STOP_WORDS_LIST = List("a", "am", "an", "and", "are", "as", "at",
    "be","do", "go", "if", "in", "is", "it", "of", "on", "the", "to")

     def receive: Receive = {
    case message: String =>
      sender ! evaluateExpression(message)
  }
  def evaluateExpression(line: String): MapData = MapData {
    getWordFreq(line.toLowerCase.split(" ").toList)
}

  def getWordFreq [T](xs: List[String]): Map[String,Int] =
    pack(xs.sortWith(_<_)
          .filterNot(STOP_WORDS_LIST.contains(_))
    ) map(ys => ( ys.head,ys.length )) toMap


  def pack[T](xs: List[T]): List[List[T]] = xs match {
    case Nil => Nil
    case x =>
      val (first, rest) = x span (_ == x.head)
      first::pack(rest)
  }

}
