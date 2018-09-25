package actors

import akka.actor.{Actor, Props}

object SortActor {
  def props: Props = Props(new SortActor)
  final case class SortList(listToSort: List[Int])
  final case class ListSorted(sortedList: List[Int])
}

class SortActor extends Actor {
  import SortActor._

  override def receive: Receive = {
    case SortList(listToSort) =>
      sender() ! ListSorted(listToSort.sorted)
  }
}
