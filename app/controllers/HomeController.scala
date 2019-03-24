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
  def index(): Action[AnyContent] = Action { implicit request =>
    Ok(views.html.index(ListForm.form))
  }

  def sortList(): Action[AnyContent] = Action.async { implicit request =>
    ListForm.form.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(views.html.index(formWithErrors))),
      data => sortService.sort(data.listToSort).map(s => Ok(s.mkString(", ")))
    )
  }
}
