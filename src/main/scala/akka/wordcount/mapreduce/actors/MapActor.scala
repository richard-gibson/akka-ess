package akka.wordcount.mapreduce.actors

import akka.actor.Actor
import akka.wordcount.mapreduce.MapData

/**
 * User: Richard
 * Date: 06/09/13
 * Time: 21:53
 */

trait WordFrequencyNode {
  val STOP_WORDS_LIST = List("a", "am", "an", "and", "are", "as", "at",
    "be","do", "go", "if", "in", "is", "it", "of", "on", "the", "to")
  val SENTENCE_SEPARATOR = " "

  def sentenceAsList(sentence:String) =
          sentence.toLowerCase.split(SENTENCE_SEPARATOR).toList

  def getWordFreq [T](xs: List[String]): Map[String,Int] =
    (pack(xs.sortWith(_ < _)
      .filterNot(STOP_WORDS_LIST.contains(_))
    ) map (ys => (ys.head, ys.length))).toMap

  private def pack[T](xs: List[T]): List[List[T]] = xs match {
    case Nil => Nil
    case x =>
      val (first, rest) = x span (_ == x.head)
      first::pack(rest)
  }

}

class MapActor extends Actor with WordFrequencyNode{

  def receive: Receive = {
    case message: String =>
      sender ! evaluateExpression(message)
  }

  def evaluateExpression(message: String): MapData =
    MapData (getWordFreq(sentenceAsList(message)))

}
