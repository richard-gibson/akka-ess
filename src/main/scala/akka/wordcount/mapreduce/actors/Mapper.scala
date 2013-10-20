package akka.wordcount.mapreduce.actors

/**
 * User: Richard
 * Date: 06/09/13
 * Time: 21:53
 */

trait Mapper {
  def dataToMap(data: String):Map[String,Int]
}
