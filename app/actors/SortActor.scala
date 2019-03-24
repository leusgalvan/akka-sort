package actors

import akka.actor.{Actor, Props}
import akka.pattern.ask

import scala.concurrent.duration._
import scala.annotation.tailrec
import akka.util.Timeout

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global

object SortActor {
  def props: Props = Props(new SortActor)
  final case class SortList(listToSort: List[Int])
  final case class ListSorted(sortedList: List[Int])
}

class SortActor extends Actor {
  import SortActor._

  @tailrec
  private def merge(ls: List[Int], rs: List[Int], acc: List[Int]): List[Int] = {
    (ls, rs) match {
      case(Nil, _) => acc ++ rs
      case(_, Nil) => acc ++ ls
      case(left :: ls1, right :: rs1) =>
        if (left < right) {
          merge(ls1, rs, acc:+left)
        } else {
          merge(ls, rs1, acc:+right)
        }
    }
  }

  implicit val timeout: Timeout = 10 seconds

  private def sort(listToSort: List[Int]): List[Int] = {
    listToSort match {
      case Nil | List(_) => listToSort
      case _ =>
        val (firstHalf, secondHalf) = listToSort.splitAt(listToSort.length / 2)
        val firstActor = context.actorOf(props)
        val secondActor = context.actorOf(props)

        val eventualSortedList = for {
          sortedFirstHalf <- (firstActor ? SortList(firstHalf)).mapTo[ListSorted]
          sortedSecondHalf <- (secondActor ? SortList(secondHalf)).mapTo[ListSorted]
        } yield merge(sortedFirstHalf.sortedList, sortedSecondHalf.sortedList, Nil)

        Await.result(eventualSortedList, timeout.duration)
    }
  }

  @SuppressWarnings(Array("org.wartremover.warts.Any"))
  override def receive: Receive = {
    case SortList(listToSort) =>
      val sortedList = sort(listToSort)
      sender() ! ListSorted(sortedList)
      context.stop(self)
  }
}
