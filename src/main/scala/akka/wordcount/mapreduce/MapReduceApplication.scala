package akka.wordcount.mapreduce

import akka.util.Timeout
import akka.actor.{Props, ActorSystem}
import akka.wordcount.mapreduce.actors.MasterActor
import akka.pattern.ask
import scala.util.Random
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
  val rand = new Random

  val sentences: Array[String] =  Array(
    "Once an old one with nine holes. — Man.",
    "Once outside, it is coming down, down; it enters; it lies down. — An axe.",
    "It enters from outside with clothes; it is undressed in the outer tent. — The alder tree.2",
    "An old woman is made to break wind by an angry old man. — Bears copulating.",
    "A grass-bound shoulder-blade. — A ring on the finger.3",
    "I have four holes and only one road. — A wooden house.4",
    "I move along — but without trace; I cut — but draw no blood. — A moving boat.",
    "It is round, has an eye, is used by women. After use it is thrown away. — Iron scraper.5",
    "Its eye is poked by women; it gets angry, bites its lip, and ascends skyward. — The lamp.1",
    "I have a headache, my nose bleeds. Stop my nose bleeding! — Fly-agaric"
  )

  1 to 100000 foreach (x=>master ! sentences(rand.nextInt(sentences.length)))

  implicit val timeout = Timeout(5, SECONDS)
  1 to 10  foreach (x=> {
    Thread.sleep(50)
    println(Await.result(master ? Result, timeout.duration))
  })

  _system.shutdown()
}