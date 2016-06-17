package RaoVat

object Protocol {
  
  import spray.json._

  case class Result(id: Int, sum: Double)
  
  /* json (un)marshalling */
  
  object Result extends DefaultJsonProtocol {
    implicit val format = jsonFormat2(Result.apply)
  }
  
  /* implicit conversions */

  implicit def toResult(result: Result): Result = Result(id = result.id, sum = result.sum)

}
