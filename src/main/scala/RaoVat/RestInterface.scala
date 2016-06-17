package RaoVat

import RaoVat.QuizProtocol._
import RaoVat.Protocol._
import RaoVat.model.{Database, Post}
import akka.actor._
import akka.util.Timeout
import com.mongodb.casbah.Imports.{MongoConnection => _}
import play.api.libs.json._
import reactivemongo.api._
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.BSONDocument
import reactivemongo.play.json.BSONFormats
import spray.http.StatusCodes
import spray.routing._

import scala.collection.mutable.ListBuffer
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.language.postfixOps

class RestInterface extends HttpServiceActor
  with RestApi {

  def receive = runRoute(routes)
}

trait RestApi extends HttpService with ActorLogging {
  actor: Actor =>

  implicit val timeout = Timeout(10 seconds)

  var quizzes = Vector[Quiz]()
  var result = Vector[Result]()

  def routes: Route =

    pathPrefix("raovat") {
      pathEnd {
        get {
          complete(calculate(585700).toString())
        }
      } ~
        path(Segment) { id =>
          get { complete(calculate(id.toInt).toString())
          }
        }
    }


  //RaoVat logic Started
  //define constant alpha
  val alphaPrice = 3
  val alphaward = 3
  val alphadistrict = 2.5
  val alphacity = 2
  val alphachild = 0.4
  val alphaparent = 0.4
  val alphaattributes_cdx = 0.4
  val alphaattributes_spt = 0.4
  val alphaattributes_hn = 0.5
  val alphaattributes_lnd = 1
  val alphaattributes_st = 0.8
  val alphaattributes_spn = 1

  //define constant Beta
  val betaPrice = 10
  val betaward = 1
  val betadistrict = 1
  val betacity = 1
  val betachild = 1
  val betaparent = 1
  val betaattributes_cdx = 1
  val betaattributes_spt = 1
  val betaattributes_hn = 1
  val betaattributes_lnd = 1
  val betaattributes_st = 1
  val betaattributes_spn = 1

  def connect(): BSONCollection = {


    val driver = new MongoDriver
    val connection = driver.connection(List("localhost"))


    val db = connection("RaoVat")
    db.collection("RaoVat")
  }

  def convertToString(input: BSONDocument): String = {
    Json.stringify(BSONFormats.toJSON(input))
  }


  def getResults(): ListBuffer[Post] = {
    val users = new ListBuffer[Post]
    val ticker = Database.findAllTickers()
    ticker.map { people =>
      for (person <- people) {
        users += person
      }
    }
    Await.ready(ticker, Duration.Inf)

    return users
  }

  def numericCalculate(numeric: Double, max: Double, min: Double): Double = {
    val value = (numeric - min) / (max - min)
    return value
  }

  def formula(alpha: Double, Beta: Double, y: Double): Double = {
    val value = alpha * (1 / (1 + Beta * y))
    return value
  }

  def getMax(listUser: ListBuffer[Post]): Double = {
    var max = 0.0
    listUser.foreach(user => {
      if (user.price > max) max = user.price
    }
    )
    return max
  }

  def getMin(listUser: ListBuffer[Post]): Double = {
    var min = 0.0
    listUser.foreach(user => {
      if (user.price < min) min = user.price
    }
    )
    return min
  }

  def categoriesSumAll(user: Post, tmpUser: Post): Double = {
    //define
    var ward = 0.0
    var attributes_cdx = 0.0
    var attributes_hn = 0.0
    var attributes_lnd = 0.0
    var attributes_spn = 0.0
    var attributes_spt = 0.0
    var attributes_st = 0.0
    var category_child = 0.0
    var category_parent = 0.0
    var city = 0.0
    var district = 0.0
    // calculate each index
    if (user.ward == tmpUser.ward && tmpUser.ward != 0) ward = formula(alphaward, betaward, 0) else ward = formula(alphaward, betaward, 1)
    if (user.attributes_cdx == tmpUser.attributes_cdx && tmpUser.attributes_cdx != 0) attributes_cdx = formula(alphaattributes_cdx, betaattributes_cdx, 0) else attributes_cdx = formula(alphaattributes_cdx, betaattributes_cdx, 1)
    if (user.attributes_hn == tmpUser.attributes_hn && tmpUser.attributes_hn != 0) attributes_hn = formula(alphaattributes_hn, betaattributes_hn, 0) else attributes_hn = formula(alphaattributes_hn, betaattributes_hn, 1)
    if (user.attributes_lnd == tmpUser.attributes_lnd && tmpUser.attributes_lnd != 0) attributes_lnd = formula(alphaattributes_lnd, betaattributes_lnd, 0) else attributes_lnd = formula(alphaattributes_lnd, betaattributes_lnd, 1)
    if (user.attributes_spn == tmpUser.attributes_spn && tmpUser.attributes_spn != 0) attributes_spn = formula(alphaattributes_spn, betaattributes_spn, 0) else attributes_spn = formula(alphaattributes_spn, betaattributes_spn, 1)
    if (user.attributes_spt == tmpUser.attributes_spt && tmpUser.attributes_spt != 0) attributes_spt = formula(alphaattributes_spt, betaattributes_spt, 0) else attributes_spt = formula(alphaattributes_spt, betaattributes_spt, 1)
    if (user.attributes_st == tmpUser.attributes_st && tmpUser.attributes_st != 0) attributes_st = formula(alphaattributes_st, betaattributes_st, 0) else attributes_st = formula(alphaattributes_st, betaattributes_st, 1)
    if (user.category_child == tmpUser.category_child && tmpUser.category_child != 0) category_child = formula(alphachild, betachild, 0) else category_child = formula(alphachild, betachild, 1)
    if (user.category_parent == tmpUser.category_parent && tmpUser.category_parent != 0) category_parent = formula(alphaparent, betaparent, 0) else category_parent = formula(alphaparent, betaparent, 1)
    if (user.city == tmpUser.city && tmpUser.city != 0) city = formula(alphacity, betacity, 0) else city = formula(alphacity, betacity, 1)
    if (user.district == tmpUser.district && tmpUser.district != 0) district = formula(alphadistrict, betadistrict, 0) else district = formula(alphadistrict, betadistrict, 1)
    // return sum all
    return ward + attributes_cdx + attributes_hn + attributes_lnd + attributes_spn + attributes_spt + attributes_st + category_child + category_parent + city + district
  }

  private def calculate(id : Int): ListBuffer[Result] = {
    val resultList = new ListBuffer[Result]
    val userList = getResults()
    //get max price in the List
    val max = getMax(userList)
    //get max price in the List
    val min = getMin(userList)
    //get first user in List to test
    var userTmp = new Post(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
    val ticker = Database.findTicker(id)
    for {
      t <- ticker
    } yield  {
      t match {
        case Some(bson) => userTmp = bson
        case None => ""
      }
    }
    Await.ready(ticker, Duration.Inf)
    userList.foreach(user => {
      val sourceValue = numericCalculate(user.price, max, min)
      val destinyValue = numericCalculate(userTmp.price, max, min)
      val numericSum = formula(alphaPrice, betaPrice, Math.abs(sourceValue - destinyValue))
      val categoriesSum = categoriesSumAll(user, userTmp)
      val sum = numericSum + categoriesSum
      val result = new Result(user.id, sum)
      resultList.+=(result)
    })
    //order by desc and get 10 top item
    var index = 0
    val finalResult = new ListBuffer[Result]
    resultList.sortBy(-_.sum).foreach(
      r => {
        if (index < 10) {
          finalResult.+=(r)
          index += 1
        }
      }
    )

    return finalResult

  }
  //end RaoVat logic


  //code for future (CRUD with reactive mongodb)
  private def createResponder(requestContext: RequestContext) = {
    context.actorOf(Props(new Responder(requestContext)))
  }

  private def createQuiz(quiz: Quiz): Boolean = {
    val doesNotExist = !quizzes.exists(_.id == quiz.id)
    if (doesNotExist) quizzes = quizzes :+ quiz
    doesNotExist
  }

  private def deleteQuiz(id: String): Unit = {
    quizzes = quizzes.filterNot(_.id == id)
  }

  private def getRandomQuestion: Option[Question] = {
    val quiz = new Quiz("test", "is scala ", "YES")
    quizzes = quizzes :+ quiz
    !quizzes.isEmpty match {
      case true =>
        import scala.util.Random
        val idx = (new Random).nextInt(quizzes.size)
        Some(quizzes(idx))
      case _ => None
    }
  }

  private def getQuestion(id: String): Option[Question] = {
    getQuiz(id).map(toQuestion)
  }

  private def getQuiz(id: String): Option[Quiz] = {
    quizzes.find(_.id == id)
  }

  private def isAnswerCorrect(id: String, proposedAnswer: Answer): Boolean = {
    getQuiz(id).exists(_.correctAnswer == proposedAnswer.answer)
  }
}




class Responder(requestContext: RequestContext) extends Actor with ActorLogging {

  def receive = {

    case QuizCreated =>
      requestContext.complete(StatusCodes.Created)
      killYourself

    case QuizDeleted =>
      requestContext.complete(StatusCodes.OK)
      killYourself

    case QuizAlreadyExists =>
      requestContext.complete(StatusCodes.Conflict)
      killYourself


    case QuestionNotFound =>
      requestContext.complete(StatusCodes.NotFound)
      killYourself

    case CorrectAnswer =>
      requestContext.complete(StatusCodes.OK)
      killYourself

    case WrongAnswer =>
      requestContext.complete(StatusCodes.NotFound)
      killYourself
  }

  private def killYourself = self ! PoisonPill

}
