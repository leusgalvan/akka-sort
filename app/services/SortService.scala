package services

import actors.SortActor._
import akka.actor.{ActorRef, ActorSystem}
import javax.inject.{Inject, Singleton}
import akka.pattern.ask

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import akka.util.Timeout
import com.google.inject.ImplementedBy

import scala.concurrent.duration._

@ImplementedBy(classOf[SortServiceImpl])
trait SortService {
  def sort(list: List[Int]): Future[List[Int]]
}

@Singleton
class SortServiceImpl @Inject() (system: ActorSystem) extends SortService {
  implicit val timeout: Timeout = 10 seconds

  def sort(listToSort: List[Int]): Future[List[Int]] = {
    val sortActor = system.actorOf(props, "sort-actor")
    val eventualResult = (sortActor ? SortList(listToSort)).mapTo[ListSorted]
    eventualResult.map(_.sortedList)
  }
}