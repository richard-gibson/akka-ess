package akka.wordcount.mapreduce.actors

/**
 * User: Richard
 * Date: 20/10/13
 * Time: 22:13
 */
trait WordFrequencyMapper extends Mapper {
  val STOP_WORDS_LIST = List("a", "am", "an", "and", "are", "as", "at",
    "be","do", "go", "if", "in", "is", "it", "of", "on", "the", "to")
  val SENTENCE_SEPARATOR = " "

  private def sentenceAsList(sentence:String) =
          sentence.toLowerCase.split(SENTENCE_SEPARATOR).toList

  private def getWordFreq [T](xs: List[String]): Map[String,Int] =
    (pack(xs.sortWith(_ < _)
      .filterNot(STOP_WORDS_LIST.contains(_))
    ) map (ys => (ys.head, ys.length))).toMap

  private def pack[T](xs: List[T]): List[List[T]] = xs match {
    case Nil => Nil
    case x =>
      val (first, rest) = x span (_ == x.head)
      first::pack(rest)
  }

  def dataToMap(data: String):Map[String,Int] = getWordFreq(sentenceAsList(data))

}
