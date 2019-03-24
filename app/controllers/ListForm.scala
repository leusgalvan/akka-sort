package controllers

import play.api.data.Mapping

object ListForm {
  import play.api.data.Forms._
  import play.api.data.Form

  final case class Data(listToSort: List[Int])

  def toIntList(s: String): List[Int] = s.split(",").toList.filter(_.nonEmpty).map(_.trim.toInt)

  val form: Form[Data] = Form(
    mapping(
      "listToSort" -> text
    )(toIntList _ andThen Data.apply)(Data.unapply _ andThen {_.map(_.mkString(","))})
  )
}

