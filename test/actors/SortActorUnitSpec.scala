package actors

import actors.SortActor.{ListSorted, SortList}
import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActors, TestKit}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

class SortActorUnitSpec
  extends TestKit(ActorSystem("SortActorUnitSpec")) // allows expectMsg and testActor
  with ImplicitSender
  with WordSpecLike // to write "An actor" should "do bla bla" in, etc.
  with Matchers // to allow more readable assertions
  with BeforeAndAfterAll {

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "Sort actor" should {
    "sort an empty list" in {
      val list = Nil
      val expectedList = Nil
      val sortActor = system.actorOf(Props[SortActor])
      sortActor ! SortList(list)
      expectMsg(ListSorted(expectedList))
    }

    "sort a singleton list" in {
      val list = List(5)
      val expectedList = List(5)
      val sortActor = system.actorOf(Props[SortActor])
      sortActor ! SortList(list)
      expectMsg(ListSorted(expectedList))
    }

    "sort a list with many elements" in {
      val list = List(9, 6, 7, 2, 5, 5, 11)
      val expectedList = List(2, 5, 5, 6, 7, 9, 11)
      val sortActor = system.actorOf(Props[SortActor])
      sortActor ! SortList(list)
      expectMsg(ListSorted(expectedList))
    }
  }
}
