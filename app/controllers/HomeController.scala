package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import services.SortService

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.{Failure, Success}

@Singleton
class HomeController @Inject()(sortService: SortService, cc: ControllerComponents) extends AbstractController(cc) with play.api.i18n.I18nSupport {
  val timeout: Duration = 10 seconds

  def index() = Action { implicit request =>
    Ok(views.html.index(ListForm.form))
  }

  def sortList() = Action { implicit request: Request[AnyContent] =>
    val listToSort = ListForm.form.bindFromRequest.value.get.listToSort
    val eventualSortedList: Future[List[Int]] = sortService.sort(listToSort)
    Ok(Await.result(eventualSortedList, timeout).mkString(", "))
  }
}
